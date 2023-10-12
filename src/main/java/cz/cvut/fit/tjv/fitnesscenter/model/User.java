package cz.cvut.fit.tjv.fitnesscenter.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "person")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue
    public Long id;

    @NotBlank(message = "firstName is required.")
    private String firstName;

    @NotBlank(message = "lastName is required.")
    private String lastName;

    @NotBlank(message = "username is required.")
    private String username;

    @NotBlank(message = "password is required.")
    private String password;

    @Email
    @NotBlank(message = "emailAddress is required.")
    private String emailAddress;

    @NotBlank(message = "phoneNumber is required.")
    private String phoneNumber;

    @NotNull(message = "employee is required.")
    private Boolean employee;

    @NotNull(message = "customer is required.")
    private Boolean customer;

    @ManyToMany(mappedBy = "trainers", fetch = FetchType.EAGER)
    private Set<GroupClass> leadClasses = new HashSet<>();

    public void addLeadClass(GroupClass groupClass) {
        leadClasses.add(groupClass);
    }

    public void removeLeadClass(GroupClass groupClass) {
        leadClasses.remove(groupClass);
    }

    @PreRemove
    public void removeTrainersFromGroupClasses() {
        for (GroupClass leadClass : leadClasses) {
            leadClass.removeTrainer(this);
        }
    }
}
