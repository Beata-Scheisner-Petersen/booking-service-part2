package service.booking.roomapi.controller;

import jakarta.validation.Valid;

import service.booking.roomapi.dto.AddNewRoomDto;
import service.booking.roomapi.dto.RoomResponseDto;
import service.booking.roomapi.dto.UpdateRoomDto;
import service.booking.roomapi.service.RoomService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoomController {

    private final RoomService service;

    public RoomController(RoomService roomService) {
        this.service = roomService;
    }

    @GetMapping("/api/room")
    public ResponseEntity<?> getRooms() {
        if (service.getAllRooms().isEmpty()) {
            return ResponseEntity.status(404).body("No rooms found");
        }
        return ResponseEntity.ok(service.getAllRooms());
    }

    @PostMapping("/api/room/add")
    public ResponseEntity<?> addRoom(
            @Valid @RequestBody AddNewRoomDto addNewRoomDto) {

        RoomResponseDto roomDto = service.addRoom(
                addNewRoomDto.getRoomNumber(),
                addNewRoomDto.getRoomType(),
                addNewRoomDto.getRoomPrice(),
                addNewRoomDto.getMaxGuests(),
                addNewRoomDto.isExtraBedAvailable()
        );

        if (roomDto == null) {
            return ResponseEntity.status(404).body("Something went wrong");
        }
        return ResponseEntity.ok(roomDto);
    }

    @GetMapping("/api/room/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable Integer id) {
        RoomResponseDto resultDto = service.getRoomById(id);

        if (resultDto == null) {
            return ResponseEntity.status(404).body("Room not found");
        }
        return ResponseEntity.ok(resultDto);
    }

    @PostMapping("/api/room/update/{id}")
    public ResponseEntity<?> roomUpdate(@PathVariable Long id,
                                        @Valid @RequestBody UpdateRoomDto roomDto) {

        RoomResponseDto resultDto = service
                .updateRoom(
                        id,
                        roomDto
                );

        if (resultDto == null) {
            return ResponseEntity.status(404).body("Room could not be found");
        }
        return ResponseEntity.ok(resultDto);
    }

    @DeleteMapping("api/room/delete/{id}")
    public ResponseEntity<?> roomDelete(@PathVariable Long id) {
        if (!service.deleteRoom(id)) {
            return ResponseEntity.status(404).body("Room could not be Deleted/Found");
        }
        return ResponseEntity.ok().body("Room deleted successfully");
    }
}
