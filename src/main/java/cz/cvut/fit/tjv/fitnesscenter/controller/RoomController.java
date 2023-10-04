package cz.cvut.fit.tjv.fitnesscenter.controller;

import cz.cvut.fit.tjv.fitnesscenter.business.RoomService;
import cz.cvut.fit.tjv.fitnesscenter.controller.dto.Mapper;
import cz.cvut.fit.tjv.fitnesscenter.model.Room;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms")
public class RoomController extends AbstractController<Room> {
    RoomController(RoomService roomService, Mapper<Room> roomMapper) {
        super(roomService, roomMapper);
    }
}
