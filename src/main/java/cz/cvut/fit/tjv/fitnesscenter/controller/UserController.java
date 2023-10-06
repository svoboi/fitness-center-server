package cz.cvut.fit.tjv.fitnesscenter.controller;

import cz.cvut.fit.tjv.fitnesscenter.business.UserService;
import cz.cvut.fit.tjv.fitnesscenter.controller.dto.UserMapper;
import cz.cvut.fit.tjv.fitnesscenter.model.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController extends AbstractController<User> {
    UserController(UserService userService, UserMapper userMapper) {
        super(userService, userMapper);
    }

}
