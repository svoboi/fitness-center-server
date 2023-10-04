package cz.cvut.fit.tjv.fitnesscenter.controller;

import cz.cvut.fit.tjv.fitnesscenter.business.GroupClassService;
import cz.cvut.fit.tjv.fitnesscenter.controller.dto.GroupClassMapper;
import cz.cvut.fit.tjv.fitnesscenter.model.GroupClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping("/groupClasses")
public class GroupClassController extends AbstractController<GroupClass> {
    GroupClassController(GroupClassService groupClassService, GroupClassMapper groupClassMapper) {
        super(groupClassService, groupClassMapper);
    }

    @GetMapping("/room/{id}")
    public ResponseEntity<List<Object>> allClassesByRoom(@PathVariable Long id) {
        return new ResponseEntity<>(((GroupClassService) service).findAllByRoom(id)
                .stream().map(mapper::toDto).collect(toList()), HttpStatus.OK);
    }
}
