package cz.cvut.fit.tjv.fitnesscenter.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.*;

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

    @Email
    private String emailAddress;
    private String phoneNumber;

    private Boolean employee;
    private Boolean customer;
}
