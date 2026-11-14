package com.fsilva.roomreservation.repository;

import com.fsilva.roomreservation.domain.reservation.ReservationStatus;
import com.fsilva.roomreservation.domain.room.Room;
import com.fsilva.roomreservation.domain.room.RoomStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<Room, Long> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    List<Room> findByStatusOrderByNameAsc(RoomStatus status);

    List<Room> findAllByOrderByNameAsc();

    @Query("""
            select r from Room r
            where r.status = :roomStatus
              and r.capacity >= :capacity
              and not exists (
                  select 1 from Reservation res
                  where res.room = r
                    and res.status = :reservationStatus
                    and res.startTime < :end
                    and res.endTime > :start
              )
            order by r.name asc
            """)
    List<Room> findAvailable(
            @Param("roomStatus") RoomStatus roomStatus,
            @Param("reservationStatus") ReservationStatus reservationStatus,
            @Param("capacity") Integer capacity,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}
