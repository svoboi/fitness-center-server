package cz.cvut.fit.tjv.fitnesscenter.business;

import cz.cvut.fit.tjv.fitnesscenter.exceptions.*;
import cz.cvut.fit.tjv.fitnesscenter.model.GroupClass;
import cz.cvut.fit.tjv.fitnesscenter.model.Room;
import cz.cvut.fit.tjv.fitnesscenter.model.SportType;
import cz.cvut.fit.tjv.fitnesscenter.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;

@SpringBootTest
public class GroupClassServiceIntegrationTest {

    @Autowired
    GroupClassService groupClassService;

    @Autowired
    RoomService roomService;

    @Autowired
    SportTypeService sportTypeService;

    @Autowired
    UserService userService;

    @BeforeEach
    void setUp() {
        Room room = new Room(Long.MAX_VALUE, 1000, "one");
        roomService.create(room);

        SportType sportType = new SportType(Long.MAX_VALUE, "one");
        sportTypeService.create(sportType);

        User user = new User(Long.MAX_VALUE,
                "Troy",
                "Bolton",
                "troybolton",
                "password123",
                "troy.bolton@easthigh.com",
                "10",
                Boolean.TRUE,
                Boolean.TRUE);
        userService.create(user);
        GroupClass groupClass = new GroupClass(Long.MAX_VALUE,
                LocalDateTime.of(2023, 3, 20, 9, 30),
                LocalDateTime.of(2023, 3, 20, 11, 30),
                100,
                roomService.findAll().iterator().next(),
                sportTypeService.findAll().iterator().next(),
                new HashSet<>());
        groupClassService.create(groupClass);
    }

    @AfterEach
    void tearDown() {
        for (var groupClass : groupClassService.findAll()) {
            groupClassService.deleteById(groupClass.getId());
        }
        for (var room : roomService.findAll()) {
            roomService.deleteById(room.getId());
        }
        for (var sportType : sportTypeService.findAll()) {
            sportTypeService.deleteById(sportType.getId());
        }
        for (var user : userService.findAll()) {
            userService.deleteById(user.getId());
        }
    }

    @Test
    void userNotTrainerCreate() {
        var groupClasses = groupClassService.findAll();
        GroupClass foundGroupClass = groupClasses.iterator().next();
        User user = new User(Long.MAX_VALUE,
                "Troy",
                "Bolton",
                "jareknohavica",
                "password123",
                "troy.bolton@easthigh.com",
                "10",
                Boolean.FALSE,
                Boolean.FALSE);
        User userNotTrainer = userService.create(user);
        foundGroupClass.setTrainers(Collections.singleton(userNotTrainer));
        Assertions.assertThrows(
                UserNotTrainerException.class,
                () -> groupClassService.update(foundGroupClass, foundGroupClass.getId())
        );
    }

    @Test
    void notEnoughCapacityCreate() {
        var groupClasses = groupClassService.findAll();
        GroupClass foundGroupClass = groupClasses.iterator().next();
        foundGroupClass.setCapacity(50000);

        Assertions.assertThrows(
                NotEnoughCapacityException.class,
                () -> groupClassService.update(foundGroupClass, foundGroupClass.getId())
        );
    }

    @Test
    void shouldFind1InFindAll() {
        var groupClasses = groupClassService.findAll();
        assert (groupClasses.size() == 1);
        assert (groupClasses.iterator().next().getTimeFrom().equals(LocalDateTime.of(2023, 3, 20, 9, 30)));
    }

    @Test
    void shouldFindByID() {
        var groupClasses = groupClassService.findAll();
        GroupClass lookedForGroupClass = groupClasses.iterator().next();
        var foundGroupClass = groupClassService.findById(lookedForGroupClass.getId());
        assert (foundGroupClass.isPresent());
        assert (lookedForGroupClass.getId().equals(foundGroupClass.get().getId()));
    }

    @Test
    void shouldNotFindByID() {
        var groupClasses = groupClassService.findAll();
        GroupClass groupClass = groupClasses.iterator().next();
        Long wrongID = groupClass.getId().equals(1L) ? 2L : 1L;
        var foundGroupClass = groupClassService.findById(wrongID);
        assert (foundGroupClass.isEmpty());
    }

    @Test
    void shouldThrowConflict() {
        var groupClasses = groupClassService.findAll();
        GroupClass foundGroupClass = groupClasses.iterator().next();
        Assertions.assertThrows(
                ConflictingEntityExistsException.class,
                () -> groupClassService.create(foundGroupClass)
        );
    }

    @Test
    void shouldUpdate() {
        var groupClasses = groupClassService.findAll();
        GroupClass foundGroupClass = groupClasses.iterator().next();
        User foundUser = userService.findAll().iterator().next();
        foundGroupClass.setTimeFrom(LocalDateTime.of(2000, 1, 1, 1, 1));
        foundGroupClass.setTrainers(Collections.singleton(foundUser));
        groupClassService.update(foundGroupClass, foundGroupClass.getId());

        GroupClass updatedGroupClass = groupClasses.iterator().next();
        assert (updatedGroupClass.getTimeFrom().equals(LocalDateTime.of(2000, 1, 1, 1, 1)));
    }

    @Test
    void shouldThrowIdentificationExceptionUpdate() {
        var groupClasses = groupClassService.findAll();
        GroupClass foundGroupClass = groupClasses.iterator().next();
        Long wrongID = foundGroupClass.getId().equals(1L) ? 2L : 1L;
        Assertions.assertThrows(
                EntityIdentificationException.class,
                () -> groupClassService.update(foundGroupClass, wrongID)
        );
    }

    @Test
    void shouldThrowEntityNotFoundUpdate() {
        var groupClasses = groupClassService.findAll();
        GroupClass foundGroupClass = groupClasses.iterator().next();
        Long wrongID = foundGroupClass.getId().equals(1L) ? 2L : 1L;
        foundGroupClass.setId(wrongID);
        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> groupClassService.update(foundGroupClass, wrongID)
        );
    }

    @Test
    void shouldDelete() {
        var groupClasses = groupClassService.findAll();
        GroupClass foundGroupClass = groupClasses.iterator().next();
        groupClassService.deleteById(foundGroupClass.getId());
        assert (groupClassService.findAll().isEmpty());
    }

    @Test
    void shouldThrowTrainerNotAvailable() {
        User user = userService.findAll().iterator().next();
        GroupClass groupClass1 = new GroupClass(Long.MAX_VALUE,
                LocalDateTime.of(2023, 3, 20, 9, 30),
                LocalDateTime.of(2023, 3, 20, 11, 30),
                100,
                roomService.findAll().iterator().next(),
                sportTypeService.findAll().iterator().next(),
                Collections.singleton(user));
        groupClassService.create(groupClass1);
        Assertions.assertThrows(
                TrainerNotAvailableException.class,
                () -> groupClassService.create(groupClass1)
        );
    }

    @Test
    void addTrainerSuccess() {
        User user = userService.findAll().iterator().next();
        GroupClass groupClass = groupClassService.findAll().iterator().next();
        assert (groupClassService.findAll().iterator().next().getTrainers().size() == 0);
        groupClassService.addTrainer(groupClass.getId(), user.getId());
        assert (groupClassService.findAll().iterator().next().getTrainers().size() == 1);
    }

    @Test
    void userNotTrainerAddTrainer() {
        GroupClass groupClass = groupClassService.findAll().iterator().next();
        User user = new User(Long.MAX_VALUE,
                "Troy",
                "Bolton",
                "jareknohavica",
                "password123",
                "troy.bolton@easthigh.com",
                "10",
                Boolean.FALSE,
                Boolean.FALSE);
        User createdUser = userService.create(user);
        Assertions.assertThrows(
                UserNotTrainerException.class,
                () -> groupClassService.addTrainer(groupClass.getId(), createdUser.getId())
        );
    }

    @Test
    void removeTrainerSuccess() {
        User user = userService.findAll().iterator().next();
        GroupClass groupClass = groupClassService.findAll().iterator().next();
        groupClassService.removeTrainer(groupClass.getId(), user.getId());

        var groupClassAfterRemoving = groupClassService.findAll().iterator().next();
        assert (groupClassAfterRemoving.getTrainers().size() == 0);
    }

    @Test
    void findsAllByRoom() {
        Room room = roomService.findAll().iterator().next();
        assert (groupClassService.findAllByRoom(room.getId()).size() == 1);
    }

}
