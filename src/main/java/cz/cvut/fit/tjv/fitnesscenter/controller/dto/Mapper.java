package cz.cvut.fit.tjv.fitnesscenter.controller.dto;

import org.springframework.stereotype.Component;

@Component
public class Mapper<EntityType> {
    public Object toDto(EntityType entity) {
        return entity;
    }
}
