package cz.cvut.fit.tjv.fitnesscenter.business;

import cz.cvut.fit.tjv.fitnesscenter.model.GroupClass;
import cz.cvut.fit.tjv.fitnesscenter.model.Room;
import cz.cvut.fit.tjv.fitnesscenter.model.SportType;
import cz.cvut.fit.tjv.fitnesscenter.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Collections;
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
    void shouldKeepRoomInfo() {
        addTestRoom();
        var rooms = roomService.findAll();
        assert (rooms.iterator().hasNext());
        assert (rooms.iterator().next().getCapacity() == 100);
    }

    @Test
    void shouldKeepSportTypeInfo() {
        addTestSportType();
        var sports = sportTypeService.findAll();
        assert (sports.iterator().hasNext());
        assert (sports.iterator().next().getTypeName().equals("joga"));
    }

    @Test
    void shouldKeepUserInfo() {
        addTestUser();
        var userOp = userService.findById(1L);
        assert (userOp.isPresent());
    }

    @Test
    void shouldKeepGroupClassInfo() {
        addGroupClass();
        var groupClasses = groupClassService.findAll();
        assert (groupClasses.iterator().hasNext());
        assert (groupClasses.iterator().next().getCapacity() == 100);
    }

    @Test
    void shouldHaveConnectionOnBothSidesGroupClassUpdate() {
        User user = addTestUser();
        GroupClass groupClassUpdated = new GroupClass(
                addGroupClass().getId(),
                LocalDateTime.of(2023, 3, 20, 9, 30),
                LocalDateTime.of(2023, 3, 20, 10, 30),
                100,
                roomService.findAll().iterator().next(),
                sportTypeService.findAll().iterator().next(),
                Collections.singleton(user));
        GroupClass groupClass = groupClassService.update(groupClassUpdated, groupClassUpdated.getId());

        assert (!groupClassService.findById(groupClass.getId()).get().getTrainers().isEmpty());
        assert (!userService.findById(user.getId()).get().getLeadClasses().isEmpty());
    }

    @Test
    void shouldHaveConnectionOnBothSidesGroupClassCreate() {
        User user = addTestUser();
        GroupClass groupClass = new GroupClass(
                1L,
                LocalDateTime.of(2023, 3, 20, 9, 30),
                LocalDateTime.of(2023, 3, 20, 10, 30),
                100,
                addTestRoom(),
                addTestSportType(),
                Collections.singleton(user));
        long groupClassId = groupClassService.create(groupClass).getId();

        assert (!groupClassService.findById(groupClassId).get().getTrainers().isEmpty());
        assert (!userService.findById(user.getId()).get().getLeadClasses().isEmpty());
    }

    @Test
    void shouldHaveConnectionOnBothSidesUserUpdate() {
        User user = addTestUser();
        GroupClass groupClass = addGroupClass();
        user.addLeadClass(groupClass);
        userService.update(user, user.getId());

        assert (!userService.findById(user.getId()).get().getLeadClasses().isEmpty());
        assert (!groupClassService.findById(groupClass.getId()).get().getTrainers().isEmpty());
    }

    @Test
    void shouldHaveConnectionOnBothSidesUserCreate() {
        GroupClass groupClass = addGroupClass();
        User user = new User(1L,
                "Troy",
                "Bolton",
                "troybolton",
                "password123",
                "troy.bolton@easthigh.com",
                "10",
                Boolean.TRUE,
                Boolean.TRUE,
                Collections.singleton(groupClass));
        long userId = userService.create(user).getId();

        assert (!userService.findById(userId).get().getLeadClasses().isEmpty());
        assert (!groupClassService.findById(groupClass.getId()).get().getTrainers().isEmpty());
    }

    @Test
    void shouldLeaveUserAloneDeleteGroupClass() {
        GroupClass groupClass = addGroupClass();
        User user = new User(1L,
                "Troy",
                "Bolton",
                "troybolton",
                "password123",
                "troy.bolton@easthigh.com",
                "10",
                Boolean.TRUE,
                Boolean.TRUE,
                Collections.singleton(groupClass));
        long userId = userService.create(user).getId();

        groupClassService.deleteById(groupClass.getId());

        assert (groupClassService.findById(groupClass.getId()).isEmpty());
        assert (userService.findById(userId).isPresent());
        assert (!userService.findById(userId).get().getLeadClasses().contains(groupClass));
    }

    @Test
    void shouldLeaveGroupClassAloneDeleteUser() {
        User user = addTestUser();
        GroupClass groupClass = new GroupClass(
                1L,
                LocalDateTime.of(2023, 3, 20, 9, 30),
                LocalDateTime.of(2023, 3, 20, 10, 30),
                100,
                addTestRoom(),
                addTestSportType(),
                Collections.singleton(user));
        long groupClassId = groupClassService.create(groupClass).getId();

        userService.deleteById(user.getId());

        assert (userService.findById(user.getId()).isEmpty());
        assert (groupClassService.findById(groupClassId).isPresent());
        assert (!groupClassService.findById(groupClassId).get().getTrainers().contains(user));
    }

    Room addTestRoom() {
        Room room1 = new Room(1L, 100, "one");
        return roomService.create(room1);
    }

    SportType addTestSportType() {
        SportType sportType1 = new SportType(1L, "joga");
        return sportTypeService.create(sportType1);
    }

    User addTestUser() {
        User user1 = new User(1L,
                "Troy",
                "Bolton",
                "troybolton",
                "password123",
                "troy.bolton@easthigh.com",
                "10",
                Boolean.TRUE,
                Boolean.TRUE,
                new HashSet<>());
        return userService.create(user1);
    }

    GroupClass addGroupClass() {
        addTestRoom();
        addTestSportType();
        GroupClass groupClass1 = new GroupClass(1L,
                LocalDateTime.of(2023, 3, 20, 9, 30),
                LocalDateTime.of(2023, 3, 20, 10, 30),
                100,
                roomService.findAll().iterator().next(),
                sportTypeService.findAll().iterator().next(),
                new HashSet<>());
        return groupClassService.create(groupClass1);
    }


}
