package service.booking.roomapi.service;

import service.booking.roomapi.dto.RoomResponseDto;
import service.booking.roomapi.dto.UpdateRoomDto;
import service.booking.roomapi.entity.Room;
import service.booking.roomapi.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository repository;

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
            return null;
        }

        Room room = optionalRoom.get();

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

            return new RoomResponseDto(
                    returnedRoom.getId(),
                    returnedRoom.getRoomNumber(),
                    returnedRoom.getRoomType(),
                    returnedRoom.getRoomPrice(),
                    returnedRoom.getMaxGuests(),
                    returnedRoom.isExtraBedAvailable()

            );
        } catch (Exception e) {
            return null;
        }
    }

    public RoomResponseDto updateRoom(Long id, UpdateRoomDto dto) {

        Optional<Room> optionalRoom = repository.findById(id);

        if (optionalRoom.isEmpty()) {
            return null;
        }

        Room fetchedRoom = optionalRoom.get();

        fetchedRoom.setRoomNumber(dto.getRoomNumber());

        fetchedRoom.setRoomType(dto.getRoomType());

        fetchedRoom.setRoomPrice(dto.getRoomPrice());

        try {
            Room resultRoom = repository.save(fetchedRoom);

            return new RoomResponseDto(
                    resultRoom.getId(),
                    resultRoom.getRoomNumber(),
                    resultRoom.getRoomType(),
                    resultRoom.getRoomPrice(),
                    resultRoom.getMaxGuests(),
                    resultRoom.isExtraBedAvailable());
        } catch (Exception e) {
            return null;
        }
    }

    public boolean deleteRoom(Long id) {
        Optional<Room> optionalRoom = repository.findById(id);

        if (optionalRoom.isEmpty()) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }
}
