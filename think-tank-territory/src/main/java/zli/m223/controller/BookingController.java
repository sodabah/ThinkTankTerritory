package zli.m223.controller;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import zli.m223.model.Booking;
import zli.m223.service.BookingService;

@Path("/bookings")
@Tag(name = "bookings", description = "Handling of bookings")
@RolesAllowed({ "User", "Admin" })
public class BookingController {

    @Inject
    BookingService bookingService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Index all bookings.", description = "Returns a list of all bookings.")
    public List<Booking> index() {
        return bookingService.findAll();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON) 
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Creates a new booking.", description = "Creates a new booking and returns the newly added booking.")
    public Booking create(@Valid Booking booking) {

        return bookingService.createBooking(booking);
    }
    @Path("/{id}")
    @DELETE
    @Operation(summary = "Deletes an booking.", description = "Deletes an booking by its id.")
    public void delete(@PathParam("id") Long id) {
        bookingService.deleteBooking(id);
    }

    @Path("/{id}")
    @PUT
    @Operation(summary = "Updates an booking.", description = "Updates an booking by its id.")
    public Booking update(@PathParam("id") Long id, @Valid Booking booking) {
        return bookingService.updateBooking(id, booking);
    }

}
