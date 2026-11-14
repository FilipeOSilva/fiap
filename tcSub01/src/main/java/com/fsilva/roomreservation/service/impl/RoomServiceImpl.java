package com.fsilva.roomreservation.service.impl;

import com.fsilva.roomreservation.domain.reservation.ReservationStatus;
import com.fsilva.roomreservation.domain.room.Room;
import com.fsilva.roomreservation.domain.room.RoomResource;
import com.fsilva.roomreservation.domain.room.RoomStatus;
import com.fsilva.roomreservation.dto.room.RoomRequest;
import com.fsilva.roomreservation.dto.room.RoomResponse;
import com.fsilva.roomreservation.infra.exceptions.BusinessException;
import com.fsilva.roomreservation.infra.exceptions.ConflictException;
import com.fsilva.roomreservation.infra.exceptions.ResourceNotFoundException;
import com.fsilva.roomreservation.mapper.RoomMapper;
import com.fsilva.roomreservation.repository.RoomRepository;
import com.fsilva.roomreservation.service.RoomService;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Override
    @Transactional
    public RoomResponse create(RoomRequest request) {
        if (roomRepository.existsByNameIgnoreCase(request.getName().trim())) {
            throw new ConflictException("Já existe uma sala com esse nome");
        }

        Room room = new Room();
        applyRequest(room, request, true);
        try {
            room = roomRepository.saveAndFlush(room);
        } catch (DataIntegrityViolationException exception) {
            throw new ConflictException("Já existe uma sala com esse nome");
        }
        return roomMapper.toResponse(room);
    }

    @Override
    @Transactional
    public RoomResponse update(Long id, RoomRequest request) {
        Room room = getEntity(id);
        if (roomRepository.existsByNameIgnoreCaseAndIdNot(request.getName().trim(), id)) {
            throw new ConflictException("Já existe uma sala com esse nome");
        }

        applyRequest(room, request, false);
        try {
            room = roomRepository.saveAndFlush(room);
        } catch (DataIntegrityViolationException exception) {
            throw new ConflictException("Já existe uma sala com esse nome");
        }
        return roomMapper.toResponse(room);
    }

    @Override
    @Transactional(readOnly = true)
    public RoomResponse findById(Long id) {
        return roomMapper.toResponse(getEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> findAll(RoomStatus status, RoomResource resource, Integer minCapacity) {
        List<Room> rooms = status == null
                ? roomRepository.findAllByOrderByNameAsc()
                : roomRepository.findByStatusOrderByNameAsc(status);

        return rooms.stream()
                .filter(room -> minCapacity == null || room.getCapacity() >= minCapacity)
                .filter(room -> resource == null || room.getResources().contains(resource))
                .map(roomMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> findAvailable(
            LocalDateTime start, LocalDateTime end, RoomResource resource, Integer minCapacity) {
        if (start == null || end == null) {
            throw new BusinessException("Data/hora de início e fim são obrigatórias");
        }
        if (!end.isAfter(start)) {
            throw new BusinessException("O horário de término deve ser posterior ao de início");
        }

        int capacity = minCapacity == null ? 1 : Math.max(1, minCapacity);
        return roomRepository
                .findAvailable(RoomStatus.ATIVA, ReservationStatus.CONFIRMADA, capacity, start, end)
                .stream()
                .filter(room -> resource == null || room.getResources().contains(resource))
                .map(roomMapper::toResponse)
                .toList();
    }

    private void applyRequest(Room room, RoomRequest request, boolean creating) {
        room.setName(request.getName().trim());
        room.setCapacity(request.getCapacity());
        room.setLocation(request.getLocation().trim());

        if (request.getResources() != null) {
            room.setResources(new HashSet<>(request.getResources()));
        } else if (creating) {
            room.setResources(new HashSet<>());
        }

        if (request.getStatus() != null) {
            room.setStatus(request.getStatus());
        } else if (creating) {
            room.setStatus(RoomStatus.ATIVA);
        }
    }

    private Room getEntity(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sala não encontrada: " + id));
    }
}
