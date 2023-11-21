package zli.m223.service;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.runtime.StartupEvent;
import zli.m223.model.ApplicationUser;
import zli.m223.model.Booking;
import zli.m223.model.Room;

import java.util.Date;
import java.util.HashSet;

@IfBuildProfile("dev")
@ApplicationScoped
public class TestDataInitializer {

    @Inject
    EntityManager entityManager;

    @Transactional
    void onStart(@Observes StartupEvent ev) {
        ApplicationUser user1 = new ApplicationUser();
        user1.setFirstname("Max");
        user1.setLastname("Mustermann");
        user1.setEmail("max@example.com");
        user1.setPassword("password123");
        user1.setRole("User");

        ApplicationUser user2 = new ApplicationUser();
        user2.setFirstname("Erika");
        user2.setLastname("Musterfrau");
        user2.setEmail("erika@example.com");
        user2.setPassword("password123");
        user2.setRole("Admin");

        Room room1 = new Room();
        room1.setRoomName("Konferenzraum");
        room1.setDescription("Ein großer Raum für Meetings und Konferenzen");

        Room room2 = new Room();
        room2.setRoomName("Arbeitsplatz");
        room2.setDescription("Ein ruhiger Arbeitsplatz");

        Booking booking1 = new Booking();
        booking1.setUser(user1);
        booking1.setBookingDate(new Date());
        booking1.setBookingType("Ganzer Tag");
        booking1.setStatus("Angefragt");
        booking1.setRooms(new HashSet<>());
        booking1.getRooms().add(room1);

        Booking booking2 = new Booking();
        booking2.setUser(user2);
        booking2.setBookingDate(new Date());
        booking2.setBookingType("Halber Tag");
        booking2.setStatus("Bestätigt");
        booking2.setRooms(new HashSet<>());
        booking2.getRooms().add(room2);

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(room1);
        entityManager.persist(room2);
        entityManager.persist(booking1);
        entityManager.persist(booking2);
    }
}

