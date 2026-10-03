package service.booking.roomapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import service.booking.roomapi.dto.RoomResponseDto;
import service.booking.roomapi.dto.UpdateRoomDto;
import service.booking.roomapi.entity.Room;
import service.booking.roomapi.repository.RoomRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository repository;
    final Logger logger = LoggerFactory.getLogger(RoomService.class);

    public RoomService(RoomRepository repository) {
        this.repository = repository;
    }

    //Rooms are sorted ascending by their roomNumber
    public List<Room> getAllRooms() {
        return repository.findAll();
    }

    public RoomResponseDto getRoomById(long id) {

        Optional<Room> optionalRoom = repository.findById(id);

        if (optionalRoom.isEmpty()) {
            logger.error("Room with id: {}, was not found in getRoomById", id);
            return null;
        }

        Room room = optionalRoom.get();

        logger.info("getting room with id: {}", id);
        return new RoomResponseDto(
                room.getId(),
                room.getRoomNumber(),
                room.getRoomType(),
                room.getRoomPrice(),
                room.getMaxGuests(),
                room.isExtraBedAvailable()
        );
    }

    public RoomResponseDto addRoom(int roomNumber,
                                   String roomType,
                                   BigDecimal roomPrice,
                                   int maxGuests,
                                   boolean extraBedAvailable)
    {
        try {
            Room returnedRoom = repository.save(
                    new Room(
                            roomNumber,
                            roomType,
                            roomPrice,
                            maxGuests,
                            extraBedAvailable
                    )
            );

            logger.info("New room with id {} is added.", returnedRoom.getId());
            return new RoomResponseDto(
                    returnedRoom.getId(),
                    returnedRoom.getRoomNumber(),
                    returnedRoom.getRoomType(),
                    returnedRoom.getRoomPrice(),
                    returnedRoom.getMaxGuests(),
                    returnedRoom.isExtraBedAvailable()

            );
        } catch (Exception e) {
            logger.error("Error in adding a new room occurred: \n{}", e.getCause().toString());
            return null;
        }
    }

    public RoomResponseDto updateRoom(Long id, UpdateRoomDto dto) {

        Optional<Room> optionalRoom = repository.findById(id);

        if (optionalRoom.isEmpty()) {
            logger.error("Room with id: {}, was not found in updateRoom", id);
            return null;
        }

        Room fetchedRoom = optionalRoom.get();

        fetchedRoom.setRoomNumber(dto.getRoomNumber());

        fetchedRoom.setRoomType(dto.getRoomType());

        fetchedRoom.setRoomPrice(dto.getRoomPrice());

        try {
            Room resultRoom = repository.save(fetchedRoom);
            logger.info("Room with id {} is updated", id);

            return new RoomResponseDto(
                    resultRoom.getId(),
                    resultRoom.getRoomNumber(),
                    resultRoom.getRoomType(),
                    resultRoom.getRoomPrice(),
                    resultRoom.getMaxGuests(),
                    resultRoom.isExtraBedAvailable());
        } catch (Exception e) {
            logger.error("Error in update a room occurred: \n{}", e.getCause().toString());
            return null;
        }
    }

    public boolean deleteRoom(Long id) {
        Optional<Room> optionalRoom = repository.findById(id);

        if (optionalRoom.isEmpty()) {
            logger.error("Room with id: {}, was not found in deleteRoom", id);
            return false;
        }

        logger.info("Room with id {} is deleted", id);
        repository.deleteById(id);
        return true;
    }
}
