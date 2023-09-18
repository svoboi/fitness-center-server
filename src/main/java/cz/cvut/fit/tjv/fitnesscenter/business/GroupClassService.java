package cz.cvut.fit.tjv.fitnesscenter.business;

import cz.cvut.fit.tjv.fitnesscenter.dao.GroupClassRepository;
import cz.cvut.fit.tjv.fitnesscenter.model.GroupClass;
import cz.cvut.fit.tjv.fitnesscenter.model.Room;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class GroupClassService implements ServiceInterface<GroupClass> {

    GroupClassRepository repository;
    RoomService roomService;

    public GroupClass create(GroupClass groupClass) throws EntityStateException {
        if (exists(groupClass))
            throw new EntityStateException("class with id " + groupClass.getId() + " already exists");
        if (groupClass.getRoom() != null && !roomExists(groupClass))
            throw new EntityStateException("room with id " + groupClass.getRoom().getId() + " doesn't exist");
        //todo: check room availability
        if (!enoughCapacity(groupClass))
            throw new EntityStateException("not enough capacity in room");
        return repository.save(groupClass);
    }

    public Optional<GroupClass> findById(Long id) {
        return repository.findById(id);
    }

    public Collection<GroupClass> findAll() {
        List<GroupClass> list = new ArrayList<>();
        repository.findAll().forEach(list::add);
        return list;
    }

    public GroupClass update(GroupClass groupClass, Long pathId) throws EntityStateException {
        if (!groupClass.getId().equals(pathId)) {
            throw new EntityStateException("conficting id in path and in body");
        }
        if (!exists(groupClass))
            throw new EntityStateException("class id missing or class with this id doesnt exist");
        if (groupClass.getRoom() != null && !roomExists(groupClass))
            throw new EntityStateException("room id missing or room with this id doesnt exist");
        //todo: check room availability
        if (!enoughCapacity(groupClass))
            throw new EntityStateException("not enough capacity in room");
        return repository.save(groupClass);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Collection<GroupClass> findAllByRoom(Room room) {
        return repository.findAllByRoom(room);
    }

    public Boolean exists(GroupClass groupClass) {
        Long id = groupClass.getId();
        return id != null && repository.existsById(id);
    }

    public Boolean roomExists(GroupClass groupClass) {
        Long roomId = groupClass.getRoom().getId();
        return roomId != null && roomService.findById(roomId).isPresent();
    }

    public Boolean enoughCapacity (GroupClass groupClass) {
        var room = roomService.findById(groupClass.getRoom().getId());
        if (room.isPresent()) {
            return groupClass.getCapacity() <= room.get().getCapacity();
        }
        return false;
    }
}
