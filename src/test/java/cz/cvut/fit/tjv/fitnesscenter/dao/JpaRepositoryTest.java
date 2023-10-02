package cz.cvut.fit.tjv.fitnesscenter.dao;

import cz.cvut.fit.tjv.fitnesscenter.model.GroupClass;
import cz.cvut.fit.tjv.fitnesscenter.model.Room;
import cz.cvut.fit.tjv.fitnesscenter.model.SportType;
import cz.cvut.fit.tjv.fitnesscenter.model.User;
import org.junit.jupiter.api.*;
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
        roomRepository.deleteAll();
        sportTypeRepository.deleteAll();
        userRepository.deleteAll();
        groupClassRepository.deleteAll();
    }

    @Test
    void shouldKeepRoomInfo () {
        addTestRoom();
        var rooms = roomRepository.findAll();
        assert (rooms.iterator().hasNext());
        assert (rooms.iterator().next().getCapacity() == 100);
    }

    @Test
    void shouldKeepSportTypeInfo () {
        addTestSportType();
        var sports = sportTypeRepository.findAll();
        assert (sports.iterator().hasNext());
        assert (sports.iterator().next().getTypeName().equals("joga"));
    }

    @Test
    void shouldKeepUserInfo () {
        addTestUser();
        var userOp = userRepository.findById(1L);
        assert (userOp.isPresent());
    }

    @Test
    void shouldKeepGroupClassInfo () {
        addGroupClass();
        var groupClasses = groupClassRepository.findAll();
        assert (groupClasses.iterator().hasNext());
        assert (groupClasses.iterator().next().getCapacity() == 100);
    }

    void addTestRoom () {
        Room room1 = new Room (1L,100);
        roomRepository.save(room1);
    }
    void addTestSportType () {
        SportType sportType1 = new SportType (1L, "joga");
        sportTypeRepository.save(sportType1);
    }
    void addTestUser () {
        User user1 = new User (1L,
                "Troy",
                "Bolton",
                "troybolton",
                "password123",
                "troy.bolton@easthigh.com",
                "10",
                Boolean.FALSE,
                Boolean.TRUE,
                new HashSet<>());
        userRepository.save(user1);
    }
    void addGroupClass () {
        addTestRoom();
        addTestSportType();
        GroupClass groupClass1 = new GroupClass(1L,
                LocalDateTime.of(2023, 3, 20, 9, 30),
                LocalDateTime.of(2023, 3, 20, 10, 30),
                100,
                roomRepository.findAll().iterator().next(),
                sportTypeRepository.findAll().iterator().next(),
                new HashSet<>());
        groupClassRepository.save(groupClass1);
    }
}
