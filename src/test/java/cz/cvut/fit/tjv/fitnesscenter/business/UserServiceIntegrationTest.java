package cz.cvut.fit.tjv.fitnesscenter.business;

import cz.cvut.fit.tjv.fitnesscenter.dao.GroupClassRepository;
import cz.cvut.fit.tjv.fitnesscenter.dao.RoomRepository;
import cz.cvut.fit.tjv.fitnesscenter.dao.SportTypeRepository;
import cz.cvut.fit.tjv.fitnesscenter.exceptions.ConflictingEntityExistsException;
import cz.cvut.fit.tjv.fitnesscenter.exceptions.EntityIdentificationException;
import cz.cvut.fit.tjv.fitnesscenter.exceptions.EntityNotFoundException;
import cz.cvut.fit.tjv.fitnesscenter.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceIntegrationTest {

    @Autowired
    UserService userService;

    @Autowired
    GroupClassRepository groupClassRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    SportTypeRepository sportTypeRepository;

    @BeforeEach
    void setUp() {
        User user = new User(Long.MAX_VALUE,
                "Troy",
                "Bolton",
                "troybolton",
                "password123",
                "troy.bolton@easthigh.com",
                789456123L,
                Boolean.TRUE,
                Boolean.TRUE);
        userService.create(user);
    }

    @AfterEach
    void tearDown() {
        var users = userService.findAll();
        for (User user : users) {
            userService.deleteById(user.getId());
        }
    }

    @Test
    void shouldFind1InFindAll() {
        var users = userService.findAll();
        assert (users.size() == 1);
        assert (users.iterator().next().getUsername().equals("troybolton"));
    }

    @Test
    void shouldFindByID() {
        var users = userService.findAll();
        User lookedForUser = users.iterator().next();
        var foundUser = userService.findById(lookedForUser.getId());
        assert (foundUser.isPresent());
        assert (lookedForUser.getId().equals(foundUser.get().getId()));
    }

    @Test
    void shouldNotFindByID() {
        var users = userService.findAll();
        User user = users.iterator().next();
        Long wrongID = user.getId().equals(1L) ? 2L : 1L;
        var foundUser = userService.findById(wrongID);
        assert (foundUser.isEmpty());
    }

    @Test
    void shouldThrowConflict() {
        var users = userService.findAll();
        User foundUser = users.iterator().next();
        User conflictingUser = new User(foundUser.getId(),
                "Troy",
                "Bolton",
                "troybolton",
                "password123",
                "troy.bolton@easthigh.com",
                789456123L,
                Boolean.TRUE,
                Boolean.TRUE);
        Assertions.assertThrows(
                ConflictingEntityExistsException.class,
                () -> userService.create(conflictingUser)
        );
    }

    @Test
    void shouldUpdate() {
        var users = userService.findAll();
        User foundUser = users.iterator().next();
        foundUser.setUsername("newUsername");
        userService.update(foundUser, foundUser.getId());

        User updatedUser = users.iterator().next();
        assert (updatedUser.getUsername().equals("newUsername"));
    }

    @Test
    void shouldThrowIdentificationExceptionUpdate() {
        var users = userService.findAll();
        User foundUser = users.iterator().next();
        Long wrongID = foundUser.getId().equals(1L) ? 2L : 1L;
        Assertions.assertThrows(
                EntityIdentificationException.class,
                () -> userService.update(foundUser, wrongID)
        );
    }

    @Test
    void shouldThrowEntityNotFoundUpdate() {
        var users = userService.findAll();
        User foundUser = users.iterator().next();
        Long wrongID = foundUser.getId().equals(1L) ? 2L : 1L;
        foundUser.setId(wrongID);
        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> userService.update(foundUser, wrongID)
        );
    }

    @Test
    void shouldDelete() {
        var users = userService.findAll();
        User foundUser = users.iterator().next();
        userService.deleteById(foundUser.getId());
        assert (userService.findAll().isEmpty());
    }

    @Test
    void usernameUnavailable() {
        assert (!userService.isUsernameAvailable("troybolton"));
    }
}
