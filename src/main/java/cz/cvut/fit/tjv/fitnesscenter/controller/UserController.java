package cz.cvut.fit.tjv.fitnesscenter.controller;

import cz.cvut.fit.tjv.fitnesscenter.business.UserService;
import cz.cvut.fit.tjv.fitnesscenter.controller.dto.Mapper;
import cz.cvut.fit.tjv.fitnesscenter.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController extends AbstractController<User> {
    UserController(UserService userService, Mapper<User> userMapper) {
        super(userService, userMapper);
    }

    @GetMapping("/{id}/hoursBetween")
    public ResponseEntity<Map<String, Object>> allClassesByRoom(@PathVariable Long id,
                                                                @RequestParam LocalDateTime timeFrom,
                                                                @RequestParam LocalDateTime timeTo) {
        Map<String, Object> body = new HashMap<>();
        body.put("hours", ((UserService) service).countHoursByUserAndTimeFrame(id, timeFrom, timeTo));
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}
