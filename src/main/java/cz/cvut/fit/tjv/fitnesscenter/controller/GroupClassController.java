package cz.cvut.fit.tjv.fitnesscenter.controller;

import cz.cvut.fit.tjv.fitnesscenter.business.GroupClassService;
import cz.cvut.fit.tjv.fitnesscenter.controller.dto.GroupClassMapper;
import cz.cvut.fit.tjv.fitnesscenter.controller.dto.UsernameObject;
import cz.cvut.fit.tjv.fitnesscenter.model.GroupClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/groupClasses")
public class GroupClassController extends AbstractController<GroupClass> {
    GroupClassController(GroupClassService groupClassService, GroupClassMapper groupClassMapper) {
        super(groupClassService, groupClassMapper);
    }

    @GetMapping("/{classId}/trainers")
    public ResponseEntity<Object> trainersUsernamesInGroupClass(@PathVariable Long classId) {
        return new ResponseEntity<>(
                ((GroupClassService) service).trainersUsernamesInGroupClass(classId),
                HttpStatus.OK);
    }

    @PutMapping("/{classId}/trainers")
    public ResponseEntity<Object> addTrainer(@PathVariable Long classId, @RequestBody @Validated UsernameObject trainerUsernameObject) {
        return new ResponseEntity<>(
                mapper.toDto(
                        ((GroupClassService) service).addTrainer(classId, trainerUsernameObject.getUsername())
                ),
                HttpStatus.OK);
    }

    @DeleteMapping("/{classId}/trainers")
    public ResponseEntity<Object> removeTrainer(@PathVariable Long classId, @RequestBody @Validated UsernameObject trainerUsernameObject) {
        ((GroupClassService) service).removeTrainer(classId, trainerUsernameObject.getUsername());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
