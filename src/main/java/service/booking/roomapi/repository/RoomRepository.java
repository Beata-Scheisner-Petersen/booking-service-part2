package service.booking.roomapi.repository;

import service.booking.roomapi.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findRoomById(Long id);

    
}
