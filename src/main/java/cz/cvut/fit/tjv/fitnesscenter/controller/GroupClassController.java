package cz.cvut.fit.tjv.fitnesscenter.controller;

import cz.cvut.fit.tjv.fitnesscenter.business.GroupClassService;
import cz.cvut.fit.tjv.fitnesscenter.controller.dto.GroupClassMapper;
import cz.cvut.fit.tjv.fitnesscenter.controller.dto.UserIdObject;
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

    @PutMapping("/{classId}/trainers")
    public ResponseEntity<Object> addTrainer(@PathVariable Long classId, @RequestBody @Validated UserIdObject trainerIdObject) {
        return new ResponseEntity<>(
                mapper.toDto(
                        ((GroupClassService) service).addTrainer(classId, trainerIdObject.getUserId())
                ),
                HttpStatus.OK);
    }

    @DeleteMapping("/{classId}/trainers")
    public ResponseEntity<Object> removeTrainer(@PathVariable Long classId, @RequestBody @Validated UserIdObject trainerIdObject) {
        ((GroupClassService) service).removeTrainer(classId, trainerIdObject.getUserId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
