package cz.cvut.fit.tjv.fitnesscenter.controller.dto;

import cz.cvut.fit.tjv.fitnesscenter.model.User;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Vector;

@Component
public class UserMapper extends Mapper<User> {
    @Override
    public Object toDto(User user) {
        Vector<Map<String, Long>> leadClasses = new Vector<>();
        if (user.getLeadClasses() != null) {
            for (var el : user.getLeadClasses()) {
                leadClasses.add(Map.of("id", el.getId()));
            }
        }

        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(),
                user.getUsername(), user.getEmailAddress(), user.getPhoneNumber(),
                user.getEmployee(), user.getCustomer(), leadClasses);
    }
}