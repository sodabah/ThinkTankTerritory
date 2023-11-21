package zli.m223.service;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import io.smallrye.jwt.build.Jwt;
import zli.m223.model.ApplicationUser;

@ApplicationScoped
public class SessionService {

  @Inject
  ApplicationUserService applicationUserService;

  public Response authenticate(ApplicationUser user) {
    Optional<ApplicationUser> loggedUser = applicationUserService.findByEmail(user.getEmail());

    try {
      if (loggedUser.isPresent() && loggedUser.get().getPassword().equals(user.getPassword())) {
        String token = Jwt
            .issuer("https://zli.example.com/")
            .upn(user.getEmail())
            .groups(new HashSet<>(Arrays.asList("User", "Admin")))
            .expiresIn(Duration.ofHours(24))
            .sign();
        return Response
            .ok(loggedUser.get())
            .cookie(new NewCookie("thinkTankTerritory", token))
            .header("Authorization", "Bearer " + token)
            .build();
      }
    } catch (Exception e) {
      System.err.println("Couldn't validate password.");
    }

    return Response.status(Response.Status.FORBIDDEN).build();
  }
}
