package zli.m223.controller;

import java.util.List;

import javax.annotation.security.PermitAll;
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

import zli.m223.exceptions.DuplicateEmailException;
import zli.m223.model.ApplicationUser;
import zli.m223.service.ApplicationUserService;


@Path("/users")
@Tag(name = "Users", description = "Handling of users")
@RolesAllowed({ "User", "Admin" })
public class ApplicationUserController {
  
  @Inject
  ApplicationUserService userService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(
      summary = "Index all users.", 
      description = "Returns a list of all users."
  )
  public List<ApplicationUser> index() {
      return userService.findAll();
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(
      summary = "Creates a new user. Also known as registration.", 
      description = "Creates a new user and returns the newly added user."
  )
  @PermitAll
  public Response create(ApplicationUser user) {
    try{
        ApplicationUser createdUser = userService.createUser(user);
        return Response.status(Response.Status.CREATED).entity(createdUser).build();
    }catch(DuplicateEmailException e){
        return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
    }  
}

  @Path("/{id}")
  @DELETE
  @Operation(
      summary = "Deletes an user.",
      description = "Deletes an user by its id."
  )
  public void delete(@PathParam("id") Long id) {
      userService.deleteUser(id);
  }

  @Path("/{id}")
  @PUT
  @Operation(
      summary = "Updates an user.",
      description = "Updates an user by its id."
  )
  public ApplicationUser update(@PathParam("id") Long id, ApplicationUser user) {
      return userService.updateUser(id, user);
  }
}
