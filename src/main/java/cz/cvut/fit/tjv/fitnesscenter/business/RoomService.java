package cz.cvut.fit.tjv.fitnesscenter.business;

import cz.cvut.fit.tjv.fitnesscenter.dao.RoomRepository;
import cz.cvut.fit.tjv.fitnesscenter.model.Room;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoomService implements ServiceInterface<Room>{
    RoomRepository repository;

    public Room create(Room entity) throws EntityStateException {
        Long id = entity.getId();
        if (id != null && repository.existsById(id))
            throw new EntityStateException("room with id " + entity.getId() + " already exists");
        return repository.save(entity);
    }

    public Optional<Room> findById(Long id) {
        return repository.findById(id);
    }

    public Collection<Room> findAll() {
        List<Room> list = new ArrayList<>();
        repository.findAll().forEach(list::add);
        return list;
    }

    public Room update(Room room, Long pathId) throws EntityStateException {
        if (!room.getId().equals(pathId)) {
            throw new EntityStateException("conficting id in path and in body");
        }
        Long id = room.getId();
        if (id == null)
            throw new EntityStateException("room id missing");
        //todo: check room capacity
        if (repository.existsById(id))
            return repository.save(room);
        else
            throw new EntityStateException("room with id " + id + " does not exist exists");
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
