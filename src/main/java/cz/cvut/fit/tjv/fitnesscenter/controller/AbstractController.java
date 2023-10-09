package cz.cvut.fit.tjv.fitnesscenter.controller;

import com.fasterxml.jackson.databind.node.TextNode;
import cz.cvut.fit.tjv.fitnesscenter.business.EntityStateException;
import cz.cvut.fit.tjv.fitnesscenter.business.ServiceInterface;
import cz.cvut.fit.tjv.fitnesscenter.controller.dto.Mapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractController<EntityType> {
    public ServiceInterface<EntityType> service;
    public Mapper<EntityType> mapper;

    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody EntityType entity) {
        try {
            return new ResponseEntity<>(mapper.toDto(service.create(entity)), HttpStatus.CREATED);
        } catch (EntityStateException e) {
            return new ResponseEntity<>(new TextNode(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Object>> readAll() {
        return new ResponseEntity<>(service.findAll().stream().map(mapper::toDto).collect(toList()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> readByID(@PathVariable Long id) {
        var result = service.findById(id);
        if (result.isPresent())
            return new ResponseEntity<>(mapper.toDto(result.get()), HttpStatus.OK);
        else
            return new ResponseEntity<>(new TextNode("Entity with id " + id + " doesn't exist"), HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@RequestBody EntityType entity, @PathVariable Long id) {
        try {
            return new ResponseEntity<>(mapper.toDto(service.update(entity, id)), HttpStatus.OK);
        } catch (EntityStateException e) {
            return new ResponseEntity<>(new TextNode(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        if (service.findById(id).isPresent()) {
            service.deleteById(id);
            return new ResponseEntity<>(new TextNode("Entity was deleted successfully"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new TextNode("Entity with id " + id + " doesn't exist"), HttpStatus.BAD_REQUEST);
    }
}
