package cz.cvut.fit.tjv.fitnesscenter.business;

import cz.cvut.fit.tjv.fitnesscenter.dao.GroupClassRepository;
import cz.cvut.fit.tjv.fitnesscenter.dao.UserRepository;
import cz.cvut.fit.tjv.fitnesscenter.exceptions.*;
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
            throw new ConflictingEntityExistsException();
        //todo: check room availability
        if (!enoughCapacity(groupClass))
            throw new NotEnoughCapacityException();
        if (!trainersSetOnlyEmployees(groupClass)) {
            throw new UserNotTrainerException();
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
        if (groupClass.getId() == null || !groupClass.getId().equals(pathId)) {
            throw new EntityIdentificationException();
        }
        if (!exists(groupClass))
            throw new EntityNotFoundException("Class");
        //todo: check room availability
        if (!enoughCapacity(groupClass))
            throw new NotEnoughCapacityException();
        if (!trainersSetOnlyEmployees(groupClass)) {
            throw new UserNotTrainerException();
        }
        return repository.save(groupClass);
    }

    public Boolean trainersSetOnlyEmployees(GroupClass groupClass) {
        for (var user : groupClass.getTrainers()) {
            var userOp = userRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException("User"));
            if (!userOp.getEmployee()) {
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

    public Boolean enoughCapacity(GroupClass groupClass) {
        var room = roomService.findById(groupClass.getRoom().getId())
                .orElseThrow(() -> new EntityNotFoundException("Room"));
        return groupClass.getCapacity() <= room.getCapacity();
    }
}
