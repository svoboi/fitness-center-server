package cz.cvut.fit.tjv.fitnesscenter.dao;

import cz.cvut.fit.tjv.fitnesscenter.model.GroupClass;
import cz.cvut.fit.tjv.fitnesscenter.model.Room;
import cz.cvut.fit.tjv.fitnesscenter.model.SportType;
import cz.cvut.fit.tjv.fitnesscenter.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.HashSet;

@DataJpaTest
class JpaRepositoryTest {

    @Autowired
    GroupClassRepository groupClassRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    SportTypeRepository sportTypeRepository;
    @Autowired
    UserRepository userRepository;

    @AfterEach
    void tearDown() {
        groupClassRepository.deleteAll();
        userRepository.deleteAll();
        roomRepository.deleteAll();
        sportTypeRepository.deleteAll();
    }

    @Test
    void shouldKeepRoomInfo() {
        Room room = addTestRoom();
        var roomOp = roomRepository.findById(room.getId());
        assert (roomOp.isPresent());
        assert (roomOp.get().getCapacity() == 100);
    }

    @Test
    void shouldKeepSportTypeInfo() {
        SportType sportType = addTestSportType();
        var sportOp = sportTypeRepository.findById(sportType.getId());
        assert (sportOp.isPresent());
        assert (sportOp.get().getTypeName().equals("joga"));
    }

    @Test
    void shouldKeepUserInfo() {
        User user = addTestUser();
        var userOp = userRepository.findById(user.getId());
        assert (userOp.isPresent());
        assert (userOp.get().getUsername().equals("troybolton"));
    }

    @Test
    void shouldKeepGroupClassInfo() {
        GroupClass groupClass = addGroupClass();
        var groupClassOp = groupClassRepository.findById(groupClass.getId());
        assert (groupClassOp.isPresent());
        assert (groupClassOp.get().getCapacity() == 100);
    }

    Room addTestRoom() {
        Room room1 = new Room(1L, 100, "one");
        return roomRepository.save(room1);
    }

    SportType addTestSportType() {
        SportType sportType1 = new SportType(1L, "joga");
        return sportTypeRepository.save(sportType1);
    }

    User addTestUser() {
        User user1 = new User(1L,
                "Troy",
                "Bolton",
                "troybolton",
                "password123",
                "troy.bolton@easthigh.com",
                "10",
                Boolean.FALSE,
                Boolean.TRUE,
                new HashSet<>());
        return userRepository.save(user1);
    }

    GroupClass addGroupClass() {
        addTestRoom();
        addTestSportType();
        GroupClass groupClass1 = new GroupClass(1L,
                LocalDateTime.of(2023, 3, 20, 9, 30),
                LocalDateTime.of(2023, 3, 20, 10, 30),
                100,
                roomRepository.findAll().iterator().next(),
                sportTypeRepository.findAll().iterator().next(),
                new HashSet<>());
        return groupClassRepository.save(groupClass1);
    }
}
