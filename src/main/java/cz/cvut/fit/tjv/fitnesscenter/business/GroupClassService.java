package cz.cvut.fit.tjv.fitnesscenter.business;

import cz.cvut.fit.tjv.fitnesscenter.dao.GroupClassRepository;
import cz.cvut.fit.tjv.fitnesscenter.dao.UserRepository;
import cz.cvut.fit.tjv.fitnesscenter.model.GroupClass;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@AllArgsConstructor
public class GroupClassService implements ServiceInterface<GroupClass> {

    GroupClassRepository repository;
    RoomService roomService;

    UserRepository userRepository;

    public GroupClass create(GroupClass groupClass) throws EntityStateException {
        if (exists(groupClass))
            throw new EntityStateException("class with id " + groupClass.getId() + " already exists");
        if (groupClass.getRoom() != null && !roomExists(groupClass))
            throw new EntityStateException("room with id " + groupClass.getRoom().getId() + " doesn't exist");
        //todo: check room availability
        if (!enoughCapacity(groupClass))
            throw new EntityStateException("not enough capacity in room");
        if (!trainersSetOnlyEmployees(groupClass)) {
            throw new EntityStateException("at least one of the users IDs is invalid or user isn't employee");
        }
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
        if (!exists(groupClass))
            throw new EntityStateException("class id missing or class with this id doesnt exist");
        if (!groupClass.getId().equals(pathId)) {
            throw new EntityStateException("conficting id in path and in body");
        }
        if (groupClass.getRoom() != null && !roomExists(groupClass))
            throw new EntityStateException("room id missing or room with this id doesnt exist");
        //todo: check room availability
        if (!enoughCapacity(groupClass))
            throw new EntityStateException("not enough capacity in room");
        if (!trainersSetOnlyEmployees(groupClass)) {
            throw new EntityStateException("at least one of the users IDs is invalid or user isn't employee");
        }
        return repository.save(groupClass);
    }

    public Boolean trainersSetOnlyEmployees(GroupClass groupClass) {
        for (var user : groupClass.getTrainers()) {
            Long userId = user.getId();
            var userOp = userRepository.findById(userId);
            if (userOp.isEmpty() || !userOp.get().getEmployee()) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Collection<GroupClass> findAllByRoom(Long id) {
        var room = roomService.findById(id);
        if (room.isPresent()) {
            return repository.findAllByRoom(room.get());
        }
        return Collections.emptyList();
    }

    public Boolean exists(GroupClass groupClass) {
        Long id = groupClass.getId();
        return id != null && repository.existsById(id);
    }

    public Boolean roomExists(GroupClass groupClass) {
        Long roomId = groupClass.getRoom().getId();
        return roomId != null && roomService.findById(roomId).isPresent();
    }

    public Boolean enoughCapacity(GroupClass groupClass) {
        var room = roomService.findById(groupClass.getRoom().getId());
        if (room.isPresent()) {
            return groupClass.getCapacity() <= room.get().getCapacity();
        }
        return false;
    }
}
