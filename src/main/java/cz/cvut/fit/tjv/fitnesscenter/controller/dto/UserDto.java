package cz.cvut.fit.tjv.fitnesscenter.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.Vector;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    public Long id;

    private String firstName;
    private String lastName;

    private String username;

    private String emailAddress;
    private String phoneNumber;

    private Boolean employee;
    private Boolean customer;

    private Vector<Map<String, Long>> leadClasses;
}
