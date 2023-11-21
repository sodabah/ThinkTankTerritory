package zli.m223.controller;

import java.util.List;
import java.util.Optional;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.core.SecurityContext;
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

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import zli.m223.exceptions.DuplicateEmailException;
import zli.m223.model.ApplicationUser;
import zli.m223.service.ApplicationUserService;

@Path("/users")
@Tag(name = "Users", description = "Handling of users")
public class ApplicationUserController {

    @Inject
    ApplicationUserService userService;

    @Inject
    JsonWebToken jwt;


    @RolesAllowed({"Admin"})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Index all users.", description = "Returns a list of all users.")
    public Response index(@Context SecurityContext ctx) {
        try {
            String userEmail = ctx.getUserPrincipal().getName();
            Optional<ApplicationUser> loggedUser = userService.findByEmail(userEmail);
            if (loggedUser.isPresent() && loggedUser.get().getRole().equals("Admin")) {
                List<ApplicationUser> users = userService.findAll();
                return Response.ok(users).build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).entity("Access denied").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server Error").build();
        }
    }

    @Path("/register")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "registers new user.", description = "Creates a new user and returns the user.")
    @PermitAll
    public Response register(ApplicationUser user) {
        try {
            ApplicationUser createdUser = userService.createUser(user);
            return Response.status(Response.Status.CREATED).entity(createdUser).build();
            } catch (DuplicateEmailException e) {
                return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
            } catch (Exception e) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid input").build();
            }
        }


        @RolesAllowed("Admin")
        @Path("/{id}")
        @DELETE
        @Operation(summary = "Deletes a user.", description = "Deletes a user by its id.")
        public Response delete(@Context SecurityContext ctx, @PathParam("id") Long id, ApplicationUser user) {
            try {
                String userEmail = ctx.getUserPrincipal().getName();
                Optional<ApplicationUser> loggedUser = userService.findByEmail(userEmail);
                if (loggedUser.isPresent() && loggedUser.get().getRole().equals("Admin")) {
                    userService.deleteUser(id);
                    return Response.status(Response.Status.NO_CONTENT).build(); 
                } else {
                    return Response.status(Response.Status.FORBIDDEN).entity("Access denied").build(); 
                }
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server Error").build(); 
            }
        }

        @RolesAllowed("Admin")
        @PUT
        @Path("/{id}")
        public Response update(@Context SecurityContext ctx, @PathParam("id") Long id, ApplicationUser user) {
            try {
                String userEmail = ctx.getUserPrincipal().getName();
                Optional<ApplicationUser> loggedUser = userService.findByEmail(userEmail);
        
                if (loggedUser.isPresent() && loggedUser.get().getRole().equals("Admin")) {
                    ApplicationUser updatedUser = userService.updateUser(id, user);
                    if (updatedUser == null) {
                        return Response.status(Response.Status.NOT_FOUND)
                                .entity("User with ID " + id + " not found")
                                .build();
                    }
                    return Response.ok(updatedUser).build();
                } else {
                    return Response.status(Response.Status.FORBIDDEN).entity("Access denied").build();
                }
            } catch (IllegalArgumentException e) {
                return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server Error").build();
            }
        }
        

}
