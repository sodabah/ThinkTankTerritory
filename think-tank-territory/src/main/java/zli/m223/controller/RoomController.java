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

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

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
    @Operation(
        summary = "Index all rooms.", 
        description = "Returns a list of all rooms."
    )
    public List<Room> index() {
        return roomService.findAll();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Creates a new room.", 
        description = "Creates a new room and returns the newly added room."
    )
    public Room create(Room room) {
       return roomService.createRoom(room);
    }

    @Path("/{id}")
    @DELETE
    @Operation(
        summary = "Deletes a room.",
        description = "Deletes a room by its id."
    )
    public void delete(@PathParam("id") Long id) {
        roomService.deleteRoom(id);
    }

    @Path("/{id}")
    @PUT
    @Operation(
        summary = "Updates a room.",
        description = "Updates a room by its id."
    )
    public Room update(@PathParam("id") Long id, Room room) {
        return roomService.updateRoom(id, room);
    }

}
