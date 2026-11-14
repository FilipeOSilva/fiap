package com.fsilva.roomreservation.service.impl;

import com.fsilva.roomreservation.domain.reservation.Reservation;
import com.fsilva.roomreservation.domain.reservation.ReservationStatus;
import com.fsilva.roomreservation.domain.room.Room;
import com.fsilva.roomreservation.domain.room.RoomStatus;
import com.fsilva.roomreservation.domain.user.User;
import com.fsilva.roomreservation.domain.user.UserRole;
import com.fsilva.roomreservation.dto.reservation.ReservationRequest;
import com.fsilva.roomreservation.dto.reservation.ReservationResponse;
import com.fsilva.roomreservation.dto.reservation.ReservationUpdateRequest;
import com.fsilva.roomreservation.infra.exceptions.BusinessException;
import com.fsilva.roomreservation.infra.exceptions.ConflictException;
import com.fsilva.roomreservation.infra.exceptions.ForbiddenException;
import com.fsilva.roomreservation.infra.exceptions.ResourceNotFoundException;
import com.fsilva.roomreservation.mapper.ReservationMapper;
import com.fsilva.roomreservation.repository.ReservationRepository;
import com.fsilva.roomreservation.repository.RoomRepository;
import com.fsilva.roomreservation.repository.UserRepository;
import com.fsilva.roomreservation.service.NotificationService;
import com.fsilva.roomreservation.service.ReservationService;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private static final Duration MAX_DURATION = Duration.ofHours(8);

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final ReservationMapper reservationMapper;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public ReservationResponse create(ReservationRequest request) {
        validatePeriod(request.getStartTime(), request.getEndTime());

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado: " + request.getUserId()));
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sala não encontrada: " + request.getRoomId()));
        ensureRoomIsActive(room);

        try {
            ensureRoomIsFree(room.getId(), request.getStartTime(), request.getEndTime(), null);
        } catch (ConflictException exception) {
            notifyRejection(user, room, request, exception.getMessage());
            throw exception;
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setRoom(room);
        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getEndTime());
        reservation.setStatus(ReservationStatus.CONFIRMADA);

        Reservation saved;
        try {
            saved = persist(reservation);
        } catch (ConflictException exception) {
            notifyRejection(user, room, request, exception.getMessage());
            throw exception;
        }
        notificationService.notifyConfirmed(saved);
        return reservationMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ReservationResponse update(Long id, Long actingUserId, ReservationUpdateRequest request) {
        Reservation reservation = getEntity(id);
        User actor = getActor(actingUserId);
        ensureCanManage(reservation, actor);
        ensureNotCancelled(reservation);
        validatePeriod(request.getStartTime(), request.getEndTime());

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sala não encontrada: " + request.getRoomId()));
        ensureRoomIsActive(room);
        ensureRoomIsFree(room.getId(), request.getStartTime(), request.getEndTime(), reservation.getId());

        reservation.setRoom(room);
        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getEndTime());
        reservation.setUpdatedAt(LocalDateTime.now());

        Reservation saved = persist(reservation);
        notificationService.notifyAltered(saved);
        return reservationMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ReservationResponse cancel(Long id, Long actingUserId) {
        Reservation reservation = getEntity(id);
        User actor = getActor(actingUserId);
        ensureCanManage(reservation, actor);

        if (reservation.getStatus() == ReservationStatus.CANCELADA) {
            throw new BusinessException("Reserva já está cancelada");
        }
        if (reservation.getStatus() != ReservationStatus.CONFIRMADA) {
            throw new BusinessException("Somente reservas confirmadas podem ser canceladas");
        }

        reservation.setStatus(ReservationStatus.CANCELADA);
        reservation.setUpdatedAt(LocalDateTime.now());
        Reservation saved = reservationRepository.save(reservation);
        notificationService.notifyCancelled(saved);
        return reservationMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponse findById(Long id) {
        return reservationMapper.toResponse(getEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll(Sort.by(Sort.Direction.DESC, "startTime")).stream()
                .map(reservationMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponse> findByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuário não encontrado: " + userId);
        }
        return reservationRepository.findByUserIdOrderByStartTimeDesc(userId).stream()
                .map(reservationMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponse> findByRoom(Long roomId) {
        if (!roomRepository.existsById(roomId)) {
            throw new ResourceNotFoundException("Sala não encontrada: " + roomId);
        }
        return reservationRepository.findByRoomIdOrderByStartTimeAsc(roomId).stream()
                .map(reservationMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void sendDueReminders() {
        LocalDateTime now = LocalDateTime.now();
        List<Reservation> due = reservationRepository
                .findByStatusAndReminderSentFalseAndStartTimeBetween(
                        ReservationStatus.CONFIRMADA, now, now.plusMinutes(15));

        for (Reservation reservation : due) {
            notificationService.notifyReminder(reservation);
            reservation.setReminderSent(true);
        }
    }

    private void notifyRejection(
            User user, Room room, ReservationRequest request, String reason) {
        try {
            notificationService.notifyRejected(
                    user.getId(), room.getName(), request.getStartTime(), request.getEndTime(), reason);
        } catch (RuntimeException exception) {
            log.warn("Falha ao registrar notificação de rejeição para o usuário {}", user.getId(), exception);
        }
    }

    private Reservation persist(Reservation reservation) {
        try {
            return reservationRepository.saveAndFlush(reservation);
        } catch (DataIntegrityViolationException exception) {
            throw new ConflictException("Sala indisponível no período solicitado");
        }
    }

    private void ensureRoomIsFree(
            Long roomId, LocalDateTime start, LocalDateTime end, Long ignoredReservationId) {
        boolean conflict = ignoredReservationId == null
                ? reservationRepository.existsByRoomIdAndStatusAndStartTimeLessThanAndEndTimeGreaterThan(
                        roomId, ReservationStatus.CONFIRMADA, end, start)
                : reservationRepository
                        .existsByRoomIdAndStatusAndStartTimeLessThanAndEndTimeGreaterThanAndIdNot(
                                roomId, ReservationStatus.CONFIRMADA, end, start, ignoredReservationId);

        if (conflict) {
            throw new ConflictException("Sala indisponível no período solicitado");
        }
    }

    private void ensureRoomIsActive(Room room) {
        if (room.getStatus() != RoomStatus.ATIVA) {
            throw new BusinessException("Sala inativa não pode ser reservada");
        }
    }

    private void ensureNotCancelled(Reservation reservation) {
        if (reservation.getStatus() == ReservationStatus.CANCELADA) {
            throw new BusinessException("Reserva cancelada não pode ser alterada");
        }
    }

    private void ensureCanManage(Reservation reservation, User actor) {
        boolean isOwner = reservation.getUser().getId().equals(actor.getId());
        if (!isOwner && actor.getRole() != UserRole.ADMIN) {
            throw new ForbiddenException("Usuário não tem permissão para gerenciar esta reserva");
        }
    }

    private void validatePeriod(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new BusinessException("Data/hora de início e fim são obrigatórias");
        }
        if (!end.isAfter(start)) {
            throw new BusinessException("O horário de término deve ser posterior ao de início");
        }
        if (start.isBefore(LocalDateTime.now())) {
            throw new BusinessException("O horário de início não pode estar no passado");
        }
        if (Duration.between(start, end).compareTo(MAX_DURATION) > 0) {
            throw new BusinessException("A reserva não pode ultrapassar 8 horas");
        }
    }

    private Reservation getEntity(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada: " + id));
    }

    private User getActor(Long actingUserId) {
        return userRepository.findById(actingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado: " + actingUserId));
    }
}
