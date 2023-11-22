package cz.cvut.fit.tjv.fitnesscenter.business;

import cz.cvut.fit.tjv.fitnesscenter.dao.RoomRepository;
import cz.cvut.fit.tjv.fitnesscenter.exceptions.ConflictingEntityExistsException;
import cz.cvut.fit.tjv.fitnesscenter.exceptions.EntityIdentificationException;
import cz.cvut.fit.tjv.fitnesscenter.exceptions.EntityNotFoundException;
import cz.cvut.fit.tjv.fitnesscenter.model.Room;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.Optional;

@SpringBootTest
public class RoomServiceUnitTest {
    @Autowired
    RoomService roomService;

    @MockBean
    RoomRepository roomRepository;

    @Test
    void shouldCreate() {
        Room room = new Room(1L, 100, "one");

        Mockito.when(roomRepository.save(room)).thenReturn(room);
        roomService.create(room);
        Mockito.verify(roomRepository, Mockito.times(1)).save(room);
    }

    @Test
    void shouldThrowConflictCreate() {
        Room conflictingRoom = new Room(1L, 100, "one");
        Mockito.when(roomRepository.existsById(1L)).thenReturn(Boolean.TRUE);

        Assertions.assertThrows(
                ConflictingEntityExistsException.class,
                () -> roomService.create(conflictingRoom)
        );
    }

    @Test
    void shouldFind1InFindAll() {
        Room room = new Room(1L, 100, "one");
        Mockito.when(roomRepository.findAll()).thenReturn(Collections.singleton(room));

        var rooms = roomService.findAll();
        assert (rooms.size() == 1);
        assert (rooms.iterator().next().getName().equals("one"));
    }

    @Test
    void shouldFindByID() {
        Room room = new Room(1L, 100, "one");
        Mockito.when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        var foundRoom = roomService.findById(1L);
        assert (foundRoom.isPresent());
        assert (foundRoom.get().getId().equals(1L));
    }

    @Test
    void shouldNotFindByID() {
        Mockito.when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        var foundRoom = roomService.findById(1L);
        assert (foundRoom.isEmpty());
    }

    @Test
    void shouldUpdate() {
        Room room = new Room(1L, 100, "changed");
        Mockito.when(roomRepository.existsById(1L)).thenReturn(Boolean.TRUE);
        Mockito.when(roomRepository.save(room)).thenReturn(room);

        assert (roomService.update(room, room.getId()).getName().equals("changed"));
        Mockito.verify(roomRepository, Mockito.times(1)).save(room);
    }

    @Test
    void shouldThrowIdentificationExceptionUpdate() {
        Room room = new Room(1L, 100, "one");

        Assertions.assertThrows(
                EntityIdentificationException.class,
                () -> roomService.update(room, 2L)
        );
    }

    @Test
    void shouldThrowEntityNotFoundUpdate() {
        Room room = new Room(1L, 100, "one");
        Mockito.when(roomRepository.existsById(1L)).thenReturn(Boolean.FALSE);


        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> roomService.update(room, room.getId())
        );
    }

    @Test
    void shouldDelete() {
        roomService.deleteById(1L);
        Mockito.verify(roomRepository, Mockito.times(1)).deleteById(1L);
    }
}
