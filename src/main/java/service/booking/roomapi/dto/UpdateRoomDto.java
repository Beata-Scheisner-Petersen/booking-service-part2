package service.booking.roomapi.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class UpdateRoomDto {

    @Positive(message = "Room number cant be 0 or below")
    private int roomNumber;

    @Positive(message = "Room size can't be 0 or below")
    private int roomSize;

    private String roomType;

    @PositiveOrZero(message = "Price can't be negative")
    private BigDecimal roomPrice;


    public UpdateRoomDto() {
    }

    public int getRoomNumber() {
        return roomNumber;
    }
    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getRoomSize() {
        return roomSize;
    }
    public void setRoomSize(int roomSize) {
        this.roomSize = roomSize;
    }

    public BigDecimal getRoomPrice() {
        return roomPrice;
    }
    public void setRoomPrice(BigDecimal roomPrice) {
        this.roomPrice = roomPrice;
    }

    public String getRoomType() {
        return this.roomType;
    }
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
}

