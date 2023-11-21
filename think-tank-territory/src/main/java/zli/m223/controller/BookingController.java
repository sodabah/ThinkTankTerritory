package zli.m223.controller;

import java.util.List;
import java.util.Optional;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import zli.m223.model.ApplicationUser;
import zli.m223.model.Booking;
import zli.m223.service.ApplicationUserService;
import zli.m223.service.BookingService;

@Path("/bookings")
@Tag(name = "bookings", description = "Handling of bookings")
@RolesAllowed({ "User", "Admin" })
public class BookingController {

    @Inject
    BookingService bookingService;

    @Inject
    JsonWebToken jwt;

    @Inject
    ApplicationUserService applicationUserService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Index bookings.", description = "Returns all bookings for Admins or only user bookings for regular users.")
    public Response index() {
        try {
            String userEmail = jwt.getName();
            Optional<ApplicationUser> userOpt = applicationUserService.findByEmail(userEmail);

            if (userOpt.isPresent()) {
                ApplicationUser user = userOpt.get();
                List<Booking> bookings;

                if ("Admin".equals(user.getRole())) {
                    bookings = bookingService.findAll();
                } else {
                    bookings = bookingService.findBookingsByUser(user);
                }

                return Response.ok(bookings).build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).entity("Access denied").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server Error").build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBooking(Booking booking) {
        try {
            String userEmail = jwt.getName();
            ApplicationUser user = applicationUserService.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found for email: " + userEmail));
            booking.setUser(user);
            if (booking.getStatus() == null) {
                booking.setStatus("Angefragt");
            }
            Booking createdBooking = bookingService.createBooking(booking);
            return Response.status(Response.Status.CREATED).entity(createdBooking).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/{id}")
    @DELETE
    @Operation(summary = "Deletes a booking.", description = "Deletes a booking by its id.")
    public Response delete(@Context SecurityContext ctx, @PathParam("id") Long id) {
        try {
            String userEmail = ctx.getUserPrincipal().getName();
            Optional<ApplicationUser> loggedUser = applicationUserService.findByEmail(userEmail);
            Optional<Booking> bookingToDelete = bookingService.findBookingById(id);

            if (!loggedUser.isPresent()) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("User not authorized").build();
            }

            if (loggedUser.get().getRole().equals("Admin")
                    || (bookingToDelete.isPresent() && bookingToDelete.get().getUser().getEmail().equals(userEmail))) {
                bookingService.deleteBooking(id);
                return Response.status(Response.Status.NO_CONTENT).build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).entity("Access denied").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server Error").build();
        }
    }

    @Path("/{id}")
    @PUT
    @Operation(summary = "Updates an booking.", description = "Updates an booking by its id.")
    public Booking update(@PathParam("id") Long id, @Valid Booking booking) {
        return bookingService.updateBooking(id, booking);
    }

    @RolesAllowed({ "User", "Admin" })
    @Path("/{id}/status")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBookingStatus(@PathParam("id") Long id, Booking updatedBooking) {
        try {
            Booking existingBooking = bookingService.findBookingById(id)
                    .orElse(null);

            if (existingBooking == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Booking not found").build();
            }

            String userEmail = jwt.getName();
            if (jwt.getGroups().contains("Admin") || existingBooking.getUser().getEmail().equals(userEmail)) {
                existingBooking.setStatus(updatedBooking.getStatus());
                Booking mergedBooking = bookingService.updateBooking(id, existingBooking);
                return Response.ok(mergedBooking).build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).entity("Access denied").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server Error").build();
        }
    }

}
