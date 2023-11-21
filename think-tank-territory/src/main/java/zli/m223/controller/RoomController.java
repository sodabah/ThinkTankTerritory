package zli.m223.controller;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import zli.m223.exceptions.RoomNotFoundException;
import zli.m223.model.Room;
import zli.m223.service.RoomService;

@Path("/rooms")
@Tag(name = "Rooms", description = "Handling of rooms")
@RolesAllowed({ "User", "Admin" })
public class RoomController {

    @Inject
    RoomService roomService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Index all rooms.", description = "Returns a list of all rooms.")
    public List<Room> index() {
        return roomService.findAll();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Creates a new room.", description = "Creates a new room and returns the newly added room.")
    public Room create(Room room) {
        return roomService.createRoom(room);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        try {
            roomService.deleteRoom(id);
            return Response.ok().build();
        } catch (RoomNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Raum nicht gefunden").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Interner Serverfehler").build();
        }
    }

    @GET
    @Path("/available-rooms")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Room> getAvailableRooms() {
        return roomService.findAvailableRooms();
    }

    @Path("/{id}")
    @PUT
    @Operation(summary = "Updates a room.", description = "Updates a room by its id.")
    public Room update(@PathParam("id") Long id, Room room) {
        return roomService.updateRoom(id, room);
    }

}
