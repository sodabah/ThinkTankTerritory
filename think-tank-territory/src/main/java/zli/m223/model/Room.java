package zli.m223.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String roomName;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "room")
    private Set<BookingRoom> bookingRooms;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<BookingRoom> getBookingRooms() {
        return bookingRooms;
    }

    public void setBookingRooms(Set<BookingRoom> bookingRooms) {
        this.bookingRooms = bookingRooms;
    }
}
