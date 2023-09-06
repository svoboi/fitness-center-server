package cz.cvut.fit.tjv.fitnesscenter.controller;

import cz.cvut.fit.tjv.fitnesscenter.business.GroupClassService;
import cz.cvut.fit.tjv.fitnesscenter.model.GroupClass;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/groupClasses")
public class GroupClassController extends AbstractController<GroupClass> {
    GroupClassController(GroupClassService groupClassService) {
        service = groupClassService;
    }
//    public GroupClassService groupClassService;
//
//    @PostMapping
//    public ResponseEntity<Object> createProduct(@RequestBody GroupClass groupClass) {
//        try {
//            return new ResponseEntity<>(groupClassService.create(groupClass), HttpStatus.CREATED);
//        }
//        catch (EntityStateException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @GetMapping
//    public ResponseEntity<Collection<GroupClass>> readAll() {
//        return new ResponseEntity<>(groupClassService.findAll(), HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}")
//    public  ResponseEntity<Object> readByID(@PathVariable Long id) {
//        var result = groupClassService.findById(id);
//        if (result.isPresent())
//            return new ResponseEntity<>(result.get(), HttpStatus.OK);
//        else
//            return new ResponseEntity<>("Class with id " + id + " doen't exist", HttpStatus.NOT_FOUND);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Object> update(@RequestBody GroupClass groupClass, @PathVariable Long id) {
//        if (!groupClass.getId().equals(id)) {
//            return new ResponseEntity<>("Conflicting id in requests path and in body", HttpStatus.CONFLICT);
//        }
//        try {
//            return new ResponseEntity<>(groupClassService.update(groupClass), HttpStatus.OK);
//        }
//        catch (EntityStateException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Object> delete(@PathVariable Long id) {
//        if (groupClassService.findById(id).isPresent()) {
//            groupClassService.deleteById(id);
//            return new ResponseEntity<>("Class was deleted successfully", HttpStatus.OK);
//        }
//        return new ResponseEntity<>("Class with id " + id + " doesn't exist", HttpStatus.BAD_REQUEST);
//    }
}
