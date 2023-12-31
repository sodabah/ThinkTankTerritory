package zli.m223.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import zli.m223.model.Room;

@ApplicationScoped
public class RoomService {
    @Inject
    EntityManager entityManager;

    @Transactional
    public Room createRoom(Room room) {
        return entityManager.merge(room);
    }

    @Transactional
    public void deleteRoom(Long id) {
        var entity = entityManager.find(Room.class, id);
        entityManager.remove(entity);
    }

    @Transactional
    public Room updateRoom(Long id, Room room) {
        room.setId(id);
        return entityManager.merge(room);
    }

    public List<Room> findAll() {
        var query = entityManager.createQuery("FROM Room", Room.class);
        return query.getResultList();
    }


    public List<Room> findAvailableRooms() {
        TypedQuery<Room> query = entityManager.createQuery(
            "SELECT r FROM Room r WHERE r.id NOT IN (SELECT br.room.id FROM BookingRoom br WHERE br.booking.bookingDate = CURRENT_DATE)", 
            Room.class
        );
        return query.getResultList();
    }
}