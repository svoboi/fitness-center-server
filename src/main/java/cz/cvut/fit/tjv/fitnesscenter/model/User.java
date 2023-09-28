package cz.cvut.fit.tjv.fitnesscenter.model;

import jakarta.persistence.*;
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

    public void addLeadClass(GroupClass groupClass) {
        leadClasses.add(groupClass);
    }

    public void removeLeadClass(GroupClass groupClass) {
        leadClasses.remove(groupClass);
    }

    @PreRemove
    public void removeTrainersFromGroupClasses () {
        for (GroupClass leadClass: leadClasses) {
            leadClass.removeTrainer(this);
        }
    }
}
