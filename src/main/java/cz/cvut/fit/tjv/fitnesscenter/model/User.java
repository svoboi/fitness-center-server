package cz.cvut.fit.tjv.fitnesscenter.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "person")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue
    public Long id;

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;

    @NotBlank
    private String username;
    @NotBlank
    private String password;

    @Email
    @NotBlank
    private String emailAddress;
    @NotBlank
    private String phoneNumber;

    @NotNull
    private Boolean employee;
    @NotNull
    private Boolean customer;

    @ManyToMany(mappedBy = "trainers")
    private Set<GroupClass> leadClasses = new HashSet<>();
}
