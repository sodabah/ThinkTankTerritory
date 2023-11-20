package zli.m223.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

    @Column(nullable = false)
    private Date bookingDate;

    @Column(nullable = false)
    private String bookingType;

    @OneToMany(mappedBy = "booking")
    private Set<BookingRoom> bookingRooms;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public Set<BookingRoom> getBookingRooms() {
        return bookingRooms;
    }

    public void setBookingRooms(Set<BookingRoom> bookingRooms) {
        this.bookingRooms = bookingRooms;
    }
}
