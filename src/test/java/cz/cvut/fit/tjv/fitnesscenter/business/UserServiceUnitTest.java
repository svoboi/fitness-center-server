package cz.cvut.fit.tjv.fitnesscenter.business;

import cz.cvut.fit.tjv.fitnesscenter.dao.GroupClassRepository;
import cz.cvut.fit.tjv.fitnesscenter.dao.UserRepository;
import cz.cvut.fit.tjv.fitnesscenter.exceptions.ConflictingEntityExistsException;
import cz.cvut.fit.tjv.fitnesscenter.exceptions.EntityIdentificationException;
import cz.cvut.fit.tjv.fitnesscenter.exceptions.EntityNotFoundException;
import cz.cvut.fit.tjv.fitnesscenter.exceptions.UsernameTakenException;
import cz.cvut.fit.tjv.fitnesscenter.model.GroupClass;
import cz.cvut.fit.tjv.fitnesscenter.model.Room;
import cz.cvut.fit.tjv.fitnesscenter.model.SportType;
import cz.cvut.fit.tjv.fitnesscenter.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceUnitTest {
    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    GroupClassRepository groupClassRepository;

    @Test
    void shouldCreate() {
        User user = new User(1L,
                "Troy",
                "Bolton",
                "troybolton",
                "password123",
                "troy.bolton@easthigh.com",
                789456123L,
                Boolean.TRUE,
                Boolean.TRUE);

        Mockito.when(userRepository.save(user)).thenReturn(user);
        userService.create(user);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    void shouldThrowConflictCreate() {
        User conflictingUser = new User(1L,
                "Troy",
                "Bolton",
                "troybolton",
                "password123",
                "troy.bolton@easthigh.com",
                789456123L,
                Boolean.TRUE,
                Boolean.TRUE);
        Mockito.when(userRepository.existsById(1L)).thenReturn(Boolean.TRUE);

        Assertions.assertThrows(
                ConflictingEntityExistsException.class,
                () -> userService.create(conflictingUser)
        );
    }

    @Test
    void shouldThrowUsernameTaken() {
        User user = new User(1L,
                "Troy",
                "Bolton",
                "troybolton",
                "password123",
                "troy.bolton@easthigh.com",
                789456123L,
                Boolean.TRUE,
                Boolean.TRUE);
        Mockito.when(userRepository.existsById(1L)).thenReturn(Boolean.FALSE);
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        Assertions.assertThrows(
                UsernameTakenException.class,
                () -> userService.create(user)
        );
    }

    @Test
    void shouldFind1InFindAll() {
        User user = new User(1L,
                "Troy",
                "Bolton",
                "troybolton",
                "password123",
                "troy.bolton@easthigh.com",
                789456123L,
                Boolean.TRUE,
                Boolean.TRUE);
        Mockito.when(userRepository.findAll()).thenReturn(Collections.singleton(user));

        var users = userService.findAll();
        assert (users.size() == 1);
        assert (users.iterator().next().getUsername().equals("troybolton"));
    }

    @Test
    void shouldFindByID() {
        User user = new User(1L,
                "Troy",
                "Bolton",
                "troybolton",
                "password123",
                "troy.bolton@easthigh.com",
                789456123L,
                Boolean.TRUE,
                Boolean.TRUE);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        var foundUser = userService.findById(1L);
        assert (foundUser.isPresent());
        assert (foundUser.get().getId().equals(1L));
    }

    @Test
    void shouldNotFindByID() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        var foundUser = userService.findById(1L);
        assert (foundUser.isEmpty());
    }

    @Test
    void shouldUpdate() {
        User user = new User(1L,
                "Troy",
                "Bolton",
                "troybolton",
                "password123",
                "troy.bolton@easthigh.com",
                789456123L,
                Boolean.TRUE,
                Boolean.TRUE);
        Mockito.when(userRepository.existsById(1L)).thenReturn(Boolean.TRUE);
        Mockito.when(userRepository.save(user)).thenReturn(user);

        assert (userService.update(user, user.getId()).getUsername().equals("troybolton"));
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    void shouldThrowIdentificationExceptionUpdate() {
        User user = new User(1L,
                "Troy",
                "Bolton",
                "troybolton",
                "password123",
                "troy.bolton@easthigh.com",
                789456123L,
                Boolean.TRUE,
                Boolean.TRUE);

        Assertions.assertThrows(
                EntityIdentificationException.class,
                () -> userService.update(user, 2L)
        );
    }

    @Test
    void shouldThrowEntityNotFoundUpdate() {
        User user = new User(1L,
                "Troy",
                "Bolton",
                "troybolton",
                "password123",
                "troy.bolton@easthigh.com",
                789456123L,
                Boolean.TRUE,
                Boolean.TRUE);
        Mockito.when(userRepository.existsById(1L)).thenReturn(Boolean.FALSE);


        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> userService.update(user, user.getId())
        );
    }

    @Test
    void shouldDelete() {
        User user = new User(1L,
                "Troy",
                "Bolton",
                "troybolton",
                "password123",
                "troy.bolton@easthigh.com",
                789456123L,
                Boolean.TRUE,
                Boolean.TRUE);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(groupClassRepository.findAllByTrainersContaining(user)).thenReturn(Collections.emptyList());
        Mockito.doNothing().when(groupClassRepository).deleteById(1L);

        userService.deleteById(1L);
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void shouldNotBeAvailable() {
        User user = new User(1L,
                "Troy",
                "Bolton",
                "troybolton",
                "password123",
                "troy.bolton@easthigh.com",
                789456123L,
                Boolean.TRUE,
                Boolean.TRUE);
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        assert (userService.isUsernameAvailable(user.getUsername()) == Boolean.FALSE);
    }

    @Test
    void shouldCount() {
        Room room = new Room(1L, 1000, "one");
        SportType sportType = new SportType(1L, "one");
        User user = new User(1L,
                "Troy",
                "Bolton",
                "troybolton",
                "password123",
                "troy.bolton@easthigh.com",
                789456123L,
                Boolean.TRUE,
                Boolean.TRUE);
        GroupClass groupClass1 = new GroupClass(1L,
                LocalDateTime.of(2023, 3, 20, 8, 30),
                LocalDateTime.of(2023, 3, 20, 10, 0),
                100,
                room,
                sportType,
                Collections.singleton(user));
        GroupClass groupClass2 = new GroupClass(1L,
                LocalDateTime.of(2023, 3, 20, 10, 30),
                LocalDateTime.of(2023, 3, 20, 11, 30),
                100,
                room,
                sportType,
                Collections.singleton(user));
        GroupClass groupClass3 = new GroupClass(1L,
                LocalDateTime.of(2023, 3, 20, 14, 0),
                LocalDateTime.of(2023, 3, 20, 15, 30),
                100,
                room,
                sportType,
                Collections.singleton(user));

        LocalDateTime from = LocalDateTime.of(2023, 3, 20, 9, 0);
        LocalDateTime to = LocalDateTime.of(2023, 3, 20, 15, 0);

        var groupClasses = new HashSet<GroupClass>();
        groupClasses.add(groupClass1);
        groupClasses.add(groupClass2);
        groupClasses.add(groupClass3);
        Mockito.when(groupClassRepository
                        .findAllByTrainerAndTime(
                                user,
                                from,
                                to
                        ))
                .thenReturn(groupClasses);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assert (userService.countHoursByUserAndTimeFrame(user.getId(), from, to) == 3);
    }
}
