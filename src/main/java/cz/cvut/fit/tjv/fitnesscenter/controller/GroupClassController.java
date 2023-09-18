package cz.cvut.fit.tjv.fitnesscenter.controller;

import cz.cvut.fit.tjv.fitnesscenter.business.GroupClassService;
import cz.cvut.fit.tjv.fitnesscenter.model.GroupClass;
import cz.cvut.fit.tjv.fitnesscenter.model.Room;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
@RequestMapping("/groupClasses")
public class GroupClassController extends AbstractController<GroupClass> {
    GroupClassController(GroupClassService groupClassService) {
        service = groupClassService;
    }

    @GetMapping("/room")
    public ResponseEntity<Collection<GroupClass>>allClassesByRoom(@RequestBody Room room) {
            return new ResponseEntity<>(((GroupClassService)service).findAllByRoom(room), HttpStatus.OK);
    }
}
