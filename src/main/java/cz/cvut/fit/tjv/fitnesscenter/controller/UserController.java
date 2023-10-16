package cz.cvut.fit.tjv.fitnesscenter.controller;

import cz.cvut.fit.tjv.fitnesscenter.business.UserService;
import cz.cvut.fit.tjv.fitnesscenter.controller.dto.TimeFrameDTO;
import cz.cvut.fit.tjv.fitnesscenter.controller.dto.UserMapper;
import cz.cvut.fit.tjv.fitnesscenter.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController extends AbstractController<User> {
    UserController(UserService userService, UserMapper userMapper) {
        super(userService, userMapper);
    }

    @GetMapping("/hours/{id}")
    public ResponseEntity<Map<String, Object>> allClassesByRoom(@RequestBody @Validated TimeFrameDTO timeFrame, @PathVariable Long id) {
        Map<String, Object> body = new HashMap<>();
        body.put("hours", ((UserService) service).countHoursByUserAndMonth(id, timeFrame.getTimeFrom(), timeFrame.getTimeTo()));
//        body.put("timeFrom", timeFrame.getTimeFrom());
//        body.put("timeTo", timeFrame.getTimeTo());
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}
