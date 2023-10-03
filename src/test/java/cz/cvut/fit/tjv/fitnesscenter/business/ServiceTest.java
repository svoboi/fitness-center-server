package cz.cvut.fit.tjv.fitnesscenter.business;

import cz.cvut.fit.tjv.fitnesscenter.model.GroupClass;
import cz.cvut.fit.tjv.fitnesscenter.model.Room;
import cz.cvut.fit.tjv.fitnesscenter.model.SportType;
import cz.cvut.fit.tjv.fitnesscenter.model.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.time.LocalDateTime;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ServiceTest {
    @Autowired
    GroupClassService groupClassService;
    @Autowired
    RoomService roomService;
    @Autowired
    SportTypeService sportTypeService;
    @Autowired
    UserService userService;

    @AfterEach
    void tearDown() {
        groupClassService.repository.deleteAll();
        roomService.repository.deleteAll();
        sportTypeService.repository.deleteAll();
        userService.repository.deleteAll();
    }

    @Test
    public void contextLoads() {
        assertThat(groupClassService).isNotNull();
        assertThat(roomService).isNotNull();
        assertThat(sportTypeService).isNotNull();
        assertThat(userService).isNotNull();
    }

    @Test
    void shouldKeepRoomInfo () {
        addTestRoom();
        var rooms = roomService.findAll();
        assert (rooms.iterator().hasNext());
        assert (rooms.iterator().next().getCapacity() == 100);
    }

    @Test
    void shouldKeepSportTypeInfo () {
        addTestSportType();
        var sports = sportTypeService.findAll();
        assert (sports.iterator().hasNext());
        assert (sports.iterator().next().getTypeName().equals("joga"));
    }

    @Test
    void shouldKeepUserInfo () {
        addTestUser();
        var userOp = userService.findById(1L);
        assert (userOp.isPresent());
    }

    @Test
    void shouldKeepGroupClassInfo () {
        addGroupClass();
        var groupClasses = groupClassService.findAll();
        assert (groupClasses.iterator().hasNext());
        assert (groupClasses.iterator().next().getCapacity() == 100);
    }

    @Test
    void shouldHaveConnectionOnBothSidesGroupClassUpdate () {
        addTestUser();
        addGroupClass();
        var user1 = userService.findAll();
        GroupClass groupClass1 = new GroupClass(
                groupClassService.findAll().iterator().next().getId(),
                LocalDateTime.of(2023, 3, 20, 9, 30),
                LocalDateTime.of(2023, 3, 20, 10, 30),
                100,
                roomService.findAll().iterator().next(),
                sportTypeService.findAll().iterator().next(),
                new HashSet<>(user1));

        groupClassService.update(groupClass1, groupClass1.getId());
        assert (!groupClassService.findAll().iterator().next().getTrainers().isEmpty());
        assert (!userService.findAll().iterator().next().getLeadClasses().isEmpty());
    }

    @Test
    void shouldHaveConnectionOnBothSidesGroupClassCreate () {
        addTestUser();
        addTestRoom();
        addTestSportType();
        var user1 = userService.findAll();
        GroupClass groupClass1 = new GroupClass(
                1L,
                LocalDateTime.of(2023, 3, 20, 9, 30),
                LocalDateTime.of(2023, 3, 20, 10, 30),
                100,
                roomService.findAll().iterator().next(),
                sportTypeService.findAll().iterator().next(),
                new HashSet<>(user1));

        groupClassService.create(groupClass1);
        assert (!groupClassService.findAll().iterator().next().getTrainers().isEmpty());
        assert (!userService.findAll().iterator().next().getLeadClasses().isEmpty());
    }

    @Test
    void shouldHaveConnectionOnBothSidesUserUpdate () {
        addTestUser();
        addGroupClass();

        User user = userService.findAll().iterator().next();
        GroupClass groupClass = groupClassService.findAll().iterator().next();
        user.addLeadClass(groupClass);
        userService.update(user, user.getId());
        assert (!userService.findAll().iterator().next().getLeadClasses().isEmpty());
        assert (!groupClassService.findAll().iterator().next().getTrainers().isEmpty());
    }
    
    void addTestRoom () {
        Room room1 = new Room (1L,100);
        roomService.create(room1);
    }
    void addTestSportType () {
        SportType sportType1 = new SportType (1L, "joga");
        sportTypeService.create(sportType1);
    }
    void addTestUser () {
        User user1 = new User (1L,
                "Troy",
                "Bolton",
                "troybolton",
                "password123",
                "troy.bolton@easthigh.com",
                "10",
                Boolean.TRUE,
                Boolean.TRUE,
                new HashSet<>());
        userService.create(user1);
    }
    void addGroupClass () {
        addTestRoom();
        addTestSportType();
        GroupClass groupClass1 = new GroupClass(1L,
                LocalDateTime.of(2023, 3, 20, 9, 30),
                LocalDateTime.of(2023, 3, 20, 10, 30),
                100,
                roomService.findAll().iterator().next(),
                sportTypeService.findAll().iterator().next(),
                new HashSet<>());
        groupClassService.create(groupClass1);
    }


}
