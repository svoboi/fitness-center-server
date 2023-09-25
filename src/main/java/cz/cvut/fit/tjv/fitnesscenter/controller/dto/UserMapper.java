package cz.cvut.fit.tjv.fitnesscenter.controller.dto;

import cz.cvut.fit.tjv.fitnesscenter.model.GroupClass;
import cz.cvut.fit.tjv.fitnesscenter.model.User;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper extends Mapper<User> {
    @Override
    public Object toDto(User user) {
        Set<Long> leadClasses = new HashSet<>();
        if (user.getLeadClasses() != null) {
            leadClasses = user
                    .getLeadClasses()
                    .stream()
                    .map(GroupClass::getId)
                    .collect(Collectors.toSet());
        }

        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(),
                user.getUsername(), user.getEmailAddress(), user.getPhoneNumber(),
                user.getEmployee(), user.getCustomer(), leadClasses);
    }
}