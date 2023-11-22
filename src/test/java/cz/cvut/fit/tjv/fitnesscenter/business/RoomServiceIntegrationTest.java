package cz.cvut.fit.tjv.fitnesscenter.business;

import cz.cvut.fit.tjv.fitnesscenter.exceptions.ConflictingEntityExistsException;
import cz.cvut.fit.tjv.fitnesscenter.exceptions.EntityIdentificationException;
import cz.cvut.fit.tjv.fitnesscenter.exceptions.EntityNotFoundException;
import cz.cvut.fit.tjv.fitnesscenter.model.Room;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RoomServiceIntegrationTest {
    @Autowired
    RoomService roomService;


    @BeforeEach
    void setUp() {
        Room room1 = new Room(Long.MAX_VALUE, 100, "one");
        roomService.create(room1);
    }

    @AfterEach
    void tearDown() {
        var rooms = roomService.findAll();
        for (Room room : rooms) {
            roomService.deleteById(room.getId());
        }
    }

    @Test
    void shouldFind1InFindAll() {
        var rooms = roomService.findAll();
        assert (rooms.size() == 1);
        assert (rooms.iterator().next().getName().equals("one"));
    }

    @Test
    void shouldFindByID() {
        var rooms = roomService.findAll();
        Room lookedForRoom = rooms.iterator().next();
        var foundRoom = roomService.findById(lookedForRoom.getId());
        assert (foundRoom.isPresent());
        assert (lookedForRoom.getId().equals(foundRoom.get().getId()));
    }

    @Test
    void shouldNotFindByID() {
        var rooms = roomService.findAll();
        Room room = rooms.iterator().next();
        Long wrongID = room.getId().equals(1L) ? 2L : 1L;

        var foundRoom = roomService.findById(wrongID);
        assert (foundRoom.isEmpty());
    }

    @Test
    void shouldThrowConflictCreate() {
        var rooms = roomService.findAll();
        Room foundRoom = rooms.iterator().next();
        Room conflictingRoom = new Room(foundRoom.getId(), 100, "one");
        Assertions.assertThrows(
                ConflictingEntityExistsException.class,
                () -> roomService.create(conflictingRoom)
        );
    }

    @Test
    void shouldUpdate() {
        var rooms = roomService.findAll();
        Room foundRoom = rooms.iterator().next();
        foundRoom.setName("changed");
        roomService.update(foundRoom, foundRoom.getId());

        Room updatedRoom = rooms.iterator().next();
        assert (updatedRoom.getName().equals("changed"));
    }

    @Test
    void shouldThrowIdentificationExceptionUpdate() {
        var rooms = roomService.findAll();
        Room foundRoom = rooms.iterator().next();
        Long wrongID = foundRoom.getId().equals(1L) ? 2L : 1L;
        Assertions.assertThrows(
                EntityIdentificationException.class,
                () -> roomService.update(foundRoom, wrongID)
        );
    }

    @Test
    void shouldThrowEntityNotFoundUpdate() {
        var rooms = roomService.findAll();
        Room foundRoom = rooms.iterator().next();
        Long wrongID = foundRoom.getId().equals(1L) ? 2L : 1L;
        foundRoom.setId(wrongID);
        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> roomService.update(foundRoom, wrongID)
        );
    }

    @Test
    void shouldDelete() {
        var rooms = roomService.findAll();
        Room foundRoom = rooms.iterator().next();
        roomService.deleteById(foundRoom.getId());
        assert (roomService.findAll().isEmpty());
    }
}
