package cz.cvut.fit.tjv.fitnesscenter.business;

import cz.cvut.fit.tjv.fitnesscenter.exceptions.EntityStateException;

import java.util.Collection;
import java.util.Optional;

public interface ServiceInterface<EntityType> {
    public EntityType create(EntityType entity) throws EntityStateException;

    public Optional<EntityType> findById(Long id);

    public Collection<EntityType> findAll();

    public EntityType update(EntityType entity, Long id) throws EntityStateException;

    public void deleteById(Long id);
}
