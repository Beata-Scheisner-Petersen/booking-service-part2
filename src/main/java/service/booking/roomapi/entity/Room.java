package service.booking.roomapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@Entity
@Table(name = "room")
public class Room {

    //Variables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "room_number", nullable = false, unique = true)
    @Positive(message = "Room number can't be 0 or Negative")
    private int roomNumber;

    @Column(name = "room_type", nullable = false)
    private String roomType;

    @Column(name = "room_price", nullable = false)
    @PositiveOrZero
    private BigDecimal roomPrice;

    @Column(name = "max_guests", nullable = false)
    @Positive
    private int maxGuests;

    @Column(name = "extra_bed_available", nullable = false)
    private boolean extraBedAvailable;

    //Constructors
    public Room() {
    }

    public Room(int roomNumber, String roomType, BigDecimal roomPrice, int maxGuests, boolean extraBedAvailable) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.maxGuests = maxGuests;
        this.extraBedAvailable=extraBedAvailable;
    }

    //Get - Set
    public Long getId() {
        return id;
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

    public int getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }

    public boolean isExtraBedAvailable() {
        return extraBedAvailable;
    }

    public void setExtraBedAvailable(boolean extraBedAvailable) {
        this.extraBedAvailable = extraBedAvailable;
    }
}
