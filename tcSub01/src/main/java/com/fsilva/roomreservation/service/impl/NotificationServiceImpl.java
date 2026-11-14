package com.fsilva.roomreservation.service.impl;

import com.fsilva.roomreservation.domain.notification.Notification;
import com.fsilva.roomreservation.domain.notification.NotificationType;
import com.fsilva.roomreservation.domain.reservation.Reservation;
import com.fsilva.roomreservation.domain.user.User;
import com.fsilva.roomreservation.domain.user.UserRole;
import com.fsilva.roomreservation.dto.notification.NotificationResponse;
import com.fsilva.roomreservation.infra.exceptions.ForbiddenException;
import com.fsilva.roomreservation.infra.exceptions.ResourceNotFoundException;
import com.fsilva.roomreservation.mapper.NotificationMapper;
import com.fsilva.roomreservation.repository.NotificationRepository;
import com.fsilva.roomreservation.repository.UserRepository;
import com.fsilva.roomreservation.service.NotificationService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> findByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuário não encontrado: " + userId);
        }
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(notificationMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public NotificationResponse markAsRead(Long id, Long actingUserId) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notificação não encontrada: " + id));
        User actor = userRepository.findById(actingUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + actingUserId));

        if (actor.getRole() != UserRole.ADMIN && !notification.getUser().getId().equals(actor.getId())) {
            throw new ForbiddenException("Usuário não tem permissão para acessar esta notificação");
        }

        notification.setRead(true);
        return notificationMapper.toResponse(notificationRepository.save(notification));
    }

    @Override
    @Transactional
    public void notifyConfirmed(Reservation reservation) {
        create(reservation, NotificationType.RESERVA_CONFIRMADA,
                "Reserva confirmada para a sala %s em %s (até %s).".formatted(
                        reservation.getRoom().getName(),
                        reservation.getStartTime().format(FORMATTER),
                        reservation.getEndTime().format(FORMATTER)));
    }

    @Override
    @Transactional
    public void notifyAltered(Reservation reservation) {
        create(reservation, NotificationType.RESERVA_ALTERADA,
                "Reserva alterada para a sala %s em %s (até %s).".formatted(
                        reservation.getRoom().getName(),
                        reservation.getStartTime().format(FORMATTER),
                        reservation.getEndTime().format(FORMATTER)));
    }

    @Override
    @Transactional
    public void notifyCancelled(Reservation reservation) {
        create(reservation, NotificationType.RESERVA_CANCELADA,
                "Reserva cancelada para a sala %s que ocorreria em %s.".formatted(
                        reservation.getRoom().getName(),
                        reservation.getStartTime().format(FORMATTER)));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void notifyRejected(
            Long userId,
            String roomName,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String reason) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + userId));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(NotificationType.RESERVA_REJEITADA);
        notification.setMessage("Reserva rejeitada para a sala %s em %s (até %s): %s".formatted(
                roomName,
                startTime.format(FORMATTER),
                endTime.format(FORMATTER),
                reason));
        notification.setRead(false);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void notifyReminder(Reservation reservation) {
        create(reservation, NotificationType.LEMBRETE,
                "Lembrete: sua reserva na sala %s começa em %s.".formatted(
                        reservation.getRoom().getName(),
                        reservation.getStartTime().format(FORMATTER)));
    }

    private void create(Reservation reservation, NotificationType type, String message) {
        Notification notification = new Notification();
        notification.setUser(reservation.getUser());
        notification.setReservation(reservation);
        notification.setType(type);
        notification.setMessage(message);
        notification.setRead(false);
        notificationRepository.save(notification);
    }
}
