package cz.cvut.fit.tjv.fitnesscenter.controller;

import cz.cvut.fit.tjv.fitnesscenter.business.UserService;
import cz.cvut.fit.tjv.fitnesscenter.controller.dto.Mapper;
import cz.cvut.fit.tjv.fitnesscenter.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController extends AbstractController<User> {
    UserController(UserService userService, Mapper<User> userMapper) {
        super(userService, userMapper);
    }

    @GetMapping("/available/{username}")
    public ResponseEntity<Object> availabilityOfUsername(@PathVariable String username) {
        if (((UserService) service).isUsernameAvailable(username)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        Map<String, Object> body = new HashMap<>();
        body.put("message", "This username is taken.");
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
}
