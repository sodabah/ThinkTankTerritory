package zli.m223.service;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import zli.m223.model.ApplicationUser;
import zli.m223.model.Booking;

@ApplicationScoped
public class BookingService {
    @Inject
    EntityManager entityManager;

    @Transactional
    public Booking createBooking(Booking booking){
        return entityManager.merge(booking);
    }

    @Transactional
    public void deleteBooking(Long id){
        var booking = entityManager.find(Booking.class, id);
        entityManager.remove(booking);
    }

    @Transactional
    public Booking updateBooking(Long id, Booking booking){
        booking.setId(id);
        return entityManager.merge(booking);
    }

     @Transactional
    public Booking updateBookingStatus(Long id, String status) {
        Booking booking = entityManager.find(Booking.class, id);
        if (booking != null) {
            booking.setStatus(status);
            entityManager.merge(booking);
        }
        return booking;
    }
    
    public List<Booking> findAll(){
        var query = entityManager.createQuery("FROM Booking", Booking.class);
        return query.getResultList();
    }

    public List<Booking> findBookingsByUser(ApplicationUser user) {
    return entityManager.createQuery("SELECT b FROM Booking b WHERE b.user = :user", Booking.class)
                        .setParameter("user", user)
                        .getResultList();
}

    public Optional<Booking> findBookingById(Long id) {
        return entityManager
        .createQuery("SELECT b FROM Booking b WHERE b.id= :id", Booking.class)
        .setParameter("id",id)
        .getResultStream()
        .findFirst();
    }

}
