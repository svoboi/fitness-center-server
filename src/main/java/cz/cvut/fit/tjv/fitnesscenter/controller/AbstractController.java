package cz.cvut.fit.tjv.fitnesscenter.controller;

import cz.cvut.fit.tjv.fitnesscenter.business.ServiceInterface;
import cz.cvut.fit.tjv.fitnesscenter.controller.dto.Mapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractController<EntityType> {
    public ServiceInterface<EntityType> service;
    public Mapper<EntityType> mapper;

    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody @Validated EntityType entity) {
        return new ResponseEntity<>(mapper.toDto(service.create(entity)), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Object>> readAll() {
        return new ResponseEntity<>(service.findAll().stream().map(mapper::toDto).collect(toList()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> readByID(@PathVariable Long id) {
        return new ResponseEntity<>(mapper.toDto(service.findById(id).get()), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@RequestBody @Validated EntityType entity, @PathVariable Long id) {
        return new ResponseEntity<>(mapper.toDto(service.update(entity, id)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        if (service.findById(id).isEmpty()) {
            throw new NoSuchElementException();
        }
        service.deleteById(id);
        return new ResponseEntity<>(new HashMap<>(Map.of("message", "Entity deleted successfully")), HttpStatus.OK);
    }
}
