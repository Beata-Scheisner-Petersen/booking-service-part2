package service.booking.roomapi.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class AddNewRoomDto {

    @Positive(message = "Room number cant be 0 or below")
    private int roomNumber;

    private String roomType;

    @PositiveOrZero(message = "Price can't be negative")
    private BigDecimal roomPrice;

    @Column(name = "max_guests", nullable = false)
    @Positive
    private int maxGuests;

    @Column(name = "extra_bed_available", nullable = false)
    private boolean extraBedAvailable;


    public AddNewRoomDto() {
    }

    public int getRoomNumber() {
        return roomNumber;
    }
    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
    public String getRoomType() {
        return roomType;
    }
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
    public BigDecimal getRoomPrice() {
        return roomPrice;
    }
    public void setRoomPrice(BigDecimal roomPrice) {
        this.roomPrice = roomPrice;
    }
    public int getMaxGuests() {return maxGuests;}
    public void setMaxGuests(int maxGuests) {this.maxGuests = maxGuests;}

    public boolean isExtraBedAvailable() {
        return extraBedAvailable;
    }

    public void setExtraBedAvailable(boolean extraBedAvailable) {
        this.extraBedAvailable = extraBedAvailable;
    }
}
