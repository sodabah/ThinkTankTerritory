package zli.m223.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotBlank;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Entity
@NamedQueries({
  @NamedQuery(name = "ApplicationUser.findByEmail", query = "SELECT u FROM ApplicationUser u WHERE u.email = :email")
})
public class ApplicationUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(readOnly = true)
  private Long id;

  @Column(nullable = false)
  private String firstname;

  @Column (nullable = false)
  private String lastname;

  @NotBlank
  @Column(nullable = false, unique = true)
  private String email;

  @NotBlank
  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String role;

  

public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstname(){
    return firstname;
  }

  public void setFirstname(String firstname){
    this.firstname = firstname;
  }


  public void setLastname(String lastname){
    this.lastname = lastname;
  }

  public String getLastname(){
    return lastname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRole(){
    return role;
  }
  
  public void setRole(String role) {
    this.role = role;
  }
}
