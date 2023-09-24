package cz.cvut.fit.tjv.fitnesscenter.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Email;
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

    private String firstName;
    private String lastName;

    private String username;
    private String password;

    @Email
    private String emailAddress;
    private String phoneNumber;

    private Boolean employee;
    private Boolean customer;

    @ManyToMany(mappedBy = "trainers")
    private Set<GroupClass> leadClasses = new HashSet<>();
}
