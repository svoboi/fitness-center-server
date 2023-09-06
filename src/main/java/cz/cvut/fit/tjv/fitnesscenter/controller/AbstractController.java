package cz.cvut.fit.tjv.fitnesscenter.controller;

import cz.cvut.fit.tjv.fitnesscenter.business.EntityStateException;
import cz.cvut.fit.tjv.fitnesscenter.business.ServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

public abstract class AbstractController<EntityType> {
    public ServiceInterface<EntityType> service;

    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody EntityType entity) {
        try {
            return new ResponseEntity<>(service.create(entity), HttpStatus.CREATED);
        }
        catch (EntityStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<Collection<EntityType>> readAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<Object> readByID(@PathVariable Long id) {
        var result = service.findById(id);
        if (result.isPresent())
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
        else
            return new ResponseEntity<>("Entity with id " + id + " doesn't exist", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@RequestBody EntityType entity, @PathVariable Long id) {
        try {
            return new ResponseEntity<>(service.update(entity, id), HttpStatus.OK);
        }
        catch (EntityStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        if (service.findById(id).isPresent()) {
            service.deleteById(id);
            return new ResponseEntity<>("Room was deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Room with id " + id + " doesn't exist", HttpStatus.BAD_REQUEST);
    }
}
