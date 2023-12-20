package cz.cvut.fit.tjv.fitnesscenter.business;

import cz.cvut.fit.tjv.fitnesscenter.dao.GroupClassRepository;
import cz.cvut.fit.tjv.fitnesscenter.dao.SportTypeRepository;
import cz.cvut.fit.tjv.fitnesscenter.dao.UserRepository;
import cz.cvut.fit.tjv.fitnesscenter.exceptions.*;
import cz.cvut.fit.tjv.fitnesscenter.model.GroupClass;
import cz.cvut.fit.tjv.fitnesscenter.model.Room;
import cz.cvut.fit.tjv.fitnesscenter.model.SportType;
import cz.cvut.fit.tjv.fitnesscenter.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

@SpringBootTest
public class GroupClassServiceUnitTest {
    @Autowired
    GroupClassService groupClassService;

    @MockBean
    GroupClassRepository groupClassRepository;

    @MockBean
    RoomService roomService;

    @MockBean
    SportTypeRepository sportTypeRepository;

    @MockBean
    UserRepository userRepository;

    @Test
    void shouldCreate() {
        Room room = new Room(1L, 1000, "one");
        SportType sportType = new SportType(1L, "one");

        GroupClass groupClass = new GroupClass(1L,
                LocalDateTime.of(2023, 3, 20, 9, 30),
                LocalDateTime.of(2023, 3, 20, 11, 30),
                100,
                room,
                sportType,
                new HashSet<>());

        Mockito.when(roomService.findById(room.getId())).thenReturn(Optional.of(room));
        Mockito.when(groupClassRepository.save(groupClass)).thenReturn(groupClass);
        Mockito.when(sportTypeRepository.findById(1L)).thenReturn(Optional.of(sportType));

        groupClassService.create(groupClass);
        Mockito.verify(groupClassRepository, Mockito.times(1)).save(groupClass);
    }

    @Test
    void shouldThrowTrainerNotAvailableExceptionCreate() {
        Room room = new Room(1L, 1000, "one");
        SportType sportType = new SportType(1L, "one");
        User user = new User(Long.MAX_VALUE,
                "Troy",
                "Bolton",
                "troybolton",
                "password123",
                "troy.bolton@easthigh.com",
                789456123L,
                Boolean.TRUE,
                Boolean.TRUE);
        GroupClass conflictingGroupClass = new GroupClass(1L,
                LocalDateTime.of(2023, 3, 20, 9, 30),
                LocalDateTime.of(2023, 3, 20, 11, 30),
                100,
                room,
                sportType,
                Collections.singleton(user));
        GroupClass newGroupClass = new GroupClass(2L,
                LocalDateTime.of(2023, 3, 20, 9, 30),
                LocalDateTime.of(2023, 3, 20, 11, 30),
                100,
                room,
                sportType,
                Collections.singleton(user));
        Mockito.when(roomService.findById(room.getId())).thenReturn(Optional.of(room));
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(groupClassRepository.existsById(1L)).thenReturn(Boolean.FALSE);
        Mockito.when(sportTypeRepository.findById(1L)).thenReturn(Optional.of(sportType));
        Mockito.when(groupClassRepository
                        .findAllByTrainerAndTime(user,
                                LocalDateTime.of(2023, 3, 20, 9, 30),
                                LocalDateTime.of(2023, 3, 20, 11, 30)))
                .thenReturn(Collections.singleton(conflictingGroupClass));


        Assertions.assertThrows(
                TrainerNotAvailableException.class,
                () -> groupClassService.create(newGroupClass)
        );
    }

    @Test
    void shouldThrowConflictCreate() {
        Room room = new Room(1L, 1000, "one");
        SportType sportType = new SportType(1L, "one");
        GroupClass conflictingGroupClass = new GroupClass(1L,
                LocalDateTime.of(2023, 3, 20, 9, 30),
                LocalDateTime.of(2023, 3, 20, 11, 30),
                100,
                room,
                sportType,
                new HashSet<>());
        Mockito.when(groupClassRepository.existsById(1L)).thenReturn(Boolean.TRUE);

        Assertions.assertThrows(
                ConflictingEntityExistsException.class,
                () -> groupClassService.create(conflictingGroupClass)
        );
    }

    @Test
    void shouldFind1InFindAll() {
        Room room = new Room(1L, 1000, "one");
        SportType sportType = new SportType(1L, "one");
        GroupClass groupClass = new GroupClass(1L,
                LocalDateTime.of(2023, 3, 20, 9, 30),
                LocalDateTime.of(2023, 3, 20, 11, 30),
                100,
                room,
                sportType,
                new HashSet<>());
        Mockito.when(groupClassRepository.findAll()).thenReturn(Collections.singleton(groupClass));

        var groupClasss = groupClassService.findAll();
        assert (groupClasss.size() == 1);
        assert (groupClasss.iterator().next().getTimeFrom().equals(LocalDateTime.of(2023, 3, 20, 9, 30)));
    }

    @Test
    void shouldFindByID() {
        Room room = new Room(1L, 1000, "one");
        SportType sportType = new SportType(1L, "one");
        GroupClass groupClass = new GroupClass(1L,
                LocalDateTime.of(2023, 3, 20, 9, 30),
                LocalDateTime.of(2023, 3, 20, 11, 30),
                100,
                room,
                sportType,
                new HashSet<>());
        Mockito.when(groupClassRepository.findById(1L)).thenReturn(Optional.of(groupClass));

        var foundGroupClass = groupClassService.findById(1L);
        assert (foundGroupClass.isPresent());
        assert (foundGroupClass.get().getId().equals(1L));
    }

    @Test
    void shouldNotFindByID() {
        Mockito.when(groupClassRepository.findById(1L)).thenReturn(Optional.empty());

        var foundGroupClass = groupClassService.findById(1L);
        assert (foundGroupClass.isEmpty());
    }

    @Test
    void shouldUpdate() {
        Room room = new Room(1L, 1000, "one");
        SportType sportType = new SportType(1L, "one");
        GroupClass groupClass = new GroupClass(1L,
                LocalDateTime.of(2023, 3, 20, 9, 30),
                LocalDateTime.of(2023, 3, 20, 11, 30),
                100,
                room,
                sportType,
                new HashSet<>());
        Mockito.when(groupClassRepository.existsById(1L)).thenReturn(Boolean.TRUE);
        Mockito.when(groupClassRepository.save(groupClass)).thenReturn(groupClass);
        Mockito.when(roomService.findById(room.getId())).thenReturn(Optional.of(room));
        Mockito.when(sportTypeRepository.findById(1L)).thenReturn(Optional.of(sportType));

        assert (groupClassService.update(groupClass, groupClass.getId()).getTimeFrom().equals(LocalDateTime.of(2023, 3, 20, 9, 30)));
        Mockito.verify(groupClassRepository, Mockito.times(1)).save(groupClass);
    }

    @Test
    void shouldThrowIdentificationExceptionUpdate() {
        Room room = new Room(1L, 1000, "one");
        SportType sportType = new SportType(1L, "one");
        GroupClass groupClass = new GroupClass(1L,
                LocalDateTime.of(2023, 3, 20, 9, 30),
                LocalDateTime.of(2023, 3, 20, 11, 30),
                100,
                room,
                sportType,
                new HashSet<>());

        Assertions.assertThrows(
                EntityIdentificationException.class,
                () -> groupClassService.update(groupClass, 2L)
        );
    }

    @Test
    void shouldThrowEntityNotFoundUpdate() {
        Room room = new Room(1L, 1000, "one");
        SportType sportType = new SportType(1L, "one");
        GroupClass groupClass = new GroupClass(1L,
                LocalDateTime.of(2023, 3, 20, 9, 30),
                LocalDateTime.of(2023, 3, 20, 11, 30),
                100,
                room,
                sportType,
                new HashSet<>());
        Mockito.when(groupClassRepository.existsById(1L)).thenReturn(Boolean.FALSE);


        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> groupClassService.update(groupClass, groupClass.getId())
        );
    }

    @Test
    void shouldDelete() {
        groupClassService.deleteById(1L);
        Mockito.verify(groupClassRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void userNotTrainerAddTrainer() {
        Room room = new Room(1L, 1000, "one");
        SportType sportType = new SportType(1L, "one");
        User user = new User(1L,
                "Troy",
                "Bolton",
                "jareknohavica",
                "password123",
                "troy.bolton@easthigh.com",
                789456123L,
                Boolean.FALSE,
                Boolean.FALSE);
        GroupClass groupClass = new GroupClass(1L,
                LocalDateTime.of(2023, 3, 20, 9, 30),
                LocalDateTime.of(2023, 3, 20, 11, 30),
                100,
                room,
                sportType,
                Collections.singleton(user));
        Mockito.when(groupClassRepository.findById(1L)).thenReturn(Optional.of(groupClass));
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(roomService.findById(room.getId())).thenReturn(Optional.of(room));

        Assertions.assertThrows(
                UserNotTrainerException.class,
                () -> groupClassService.addTrainer(groupClass.getId(), user.getUsername())
        );
    }
}
