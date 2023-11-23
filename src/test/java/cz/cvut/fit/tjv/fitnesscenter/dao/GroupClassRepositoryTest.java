package cz.cvut.fit.tjv.fitnesscenter.dao;

import cz.cvut.fit.tjv.fitnesscenter.model.GroupClass;
import cz.cvut.fit.tjv.fitnesscenter.model.Room;
import cz.cvut.fit.tjv.fitnesscenter.model.SportType;
import cz.cvut.fit.tjv.fitnesscenter.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

@DataJpaTest
public class GroupClassRepositoryTest {
    @Autowired
    GroupClassRepository groupClassRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    SportTypeRepository sportTypeRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        Room room1 = new Room(Long.MAX_VALUE, 100, "one");
        roomRepository.save(room1);

        SportType sportType1 = new SportType(Long.MAX_VALUE, "joga");
        sportTypeRepository.save(sportType1);

        User user1 = new User(Long.MAX_VALUE,
                "Troy",
                "Bolton",
                "troybolton",
                "password123",
                "troy.bolton@easthigh.com",
                "10",
                Boolean.FALSE,
                Boolean.TRUE);
        userRepository.save(user1);

        GroupClass groupClass1 = new GroupClass(Long.MAX_VALUE,
                LocalDateTime.of(2023, 3, 20, 9, 30),
                LocalDateTime.of(2023, 3, 20, 11, 30),
                100,
                roomRepository.findAll().iterator().next(),
                sportTypeRepository.findAll().iterator().next(),
                new HashSet<>());
        groupClassRepository.save(groupClass1);
    }

    @AfterEach
    void tearDown() {
        groupClassRepository.deleteAll();
        userRepository.deleteAll();
        roomRepository.deleteAll();
        sportTypeRepository.deleteAll();
    }

    @Test
    void shouldBe1ClassesWithRoom() {
        Room room = roomRepository.findAll().iterator().next();
        Collection<GroupClass> groupClasses = groupClassRepository.findAllByRoom(room);
        assert (groupClasses.size() == 1);
        assert (groupClasses.iterator().next().getRoom() == room);
    }

    @Test
    void shouldBe0ClassesWithRoom() {
        Room room = new Room(Long.MAX_VALUE, 100, "two");
        roomRepository.save(room);
        Collection<GroupClass> groupClasses = groupClassRepository.findAllByRoom(room);
        assert (groupClasses.isEmpty());
    }

    @Test
    void shouldBe1ClassesWithSportType() {
        SportType sportType = sportTypeRepository.findAll().iterator().next();
        Collection<GroupClass> groupClasses = groupClassRepository.findAllBySportType(sportType);
        assert (groupClasses.size() == 1);
        assert (groupClasses.iterator().next().getSportType() == sportType);
    }

    @Test
    void shouldBe0ClassesWithSportType() {
        SportType sportType = new SportType(Long.MAX_VALUE, "joga");
        sportTypeRepository.save(sportType);
        Collection<GroupClass> groupClasses = groupClassRepository.findAllBySportType(sportType);
        assert (groupClasses.isEmpty());
    }

    @Test
    void shouldFind1ClassWithMultipleTrainers() {
        User user1 = userRepository.findAll().iterator().next();
        User user2PreSave = new User(Long.MAX_VALUE,
                "Troy",
                "Bolton",
                "zeke",
                "password123",
                "troy.bolton@easthigh.com",
                "10",
                Boolean.FALSE,
                Boolean.TRUE);
        User user2 = userRepository.save(user2PreSave);
        var users = new HashSet<User>();
        users.add(user1);
        users.add(user2);

        GroupClass groupClass1 = new GroupClass(Long.MAX_VALUE,
                LocalDateTime.of(2023, 3, 20, 9, 30),
                LocalDateTime.of(2023, 3, 20, 11, 30),
                100,
                roomRepository.findAll().iterator().next(),
                sportTypeRepository.findAll().iterator().next(),
                users);
        groupClassRepository.save(groupClass1);
        var groupClasses = groupClassRepository.findAllByTrainersContaining(user1);
        assert (groupClasses.size() == 1);
        assert (groupClasses.iterator().next().getTrainers().contains(user1));
    }

    @Test
    void shouldFindClassWith1Trainer() {
        User user = userRepository.findAll().iterator().next();
        GroupClass groupClass1 = new GroupClass(Long.MAX_VALUE,
                LocalDateTime.of(2023, 3, 20, 9, 30),
                LocalDateTime.of(2023, 3, 20, 11, 30),
                100,
                roomRepository.findAll().iterator().next(),
                sportTypeRepository.findAll().iterator().next(),
                Collections.singleton(user));
        groupClassRepository.save(groupClass1);
        var groupClasses = groupClassRepository.findAllByTrainersContaining(user);
        assert (groupClasses.size() == 1);
        assert (groupClasses.iterator().next().getTrainers().iterator().next() == user);
    }

    @Test
    void shouldNotFindClassWithTrainer() {
        User user = userRepository.findAll().iterator().next();
        var groupClasses = groupClassRepository.findAllByTrainersContaining(user);
        assert (groupClasses.size() == 0);
    }

    @Test
    void shouldBe0ClassesWithRoomAndTimeframe() {
        Room room = new Room(Long.MAX_VALUE, 100, "two");
        roomRepository.save(room);
        LocalDateTime from = LocalDateTime.of(2022, 2, 20, 9, 30);
        LocalDateTime to = LocalDateTime.of(2024, 4, 20, 11, 30);
        Collection<GroupClass> workedGroupClasses = groupClassRepository.findAllByRoomAndTime(room, from, to);
        assert (workedGroupClasses.size() == 0);
    }

    @Test
    void shouldBe1ClassesWithRoomAndTimeframe() {
        Room room = roomRepository.findAll().iterator().next();
        LocalDateTime from = LocalDateTime.of(2022, 2, 20, 9, 30);
        LocalDateTime to = LocalDateTime.of(2024, 4, 20, 11, 30);
        Collection<GroupClass> workedGroupClasses = groupClassRepository.findAllByRoomAndTime(room, from, to);
        assert (workedGroupClasses.size() == 1);
        assert (workedGroupClasses.iterator().next().getRoom() == room);
    }

    @Test
    void shouldBe0ClassesWithTrainerAndTimeframe() {
        User user = userRepository.findAll().iterator().next();
        LocalDateTime from = LocalDateTime.of(2022, 2, 20, 9, 30);
        LocalDateTime to = LocalDateTime.of(2024, 4, 20, 11, 30);
        Collection<GroupClass> workedGroupClasses = groupClassRepository.findAllByTrainerAndTime(user, from, to);
        assert (workedGroupClasses.size() == 0);
    }

    @Test
    void shouldBe2ClassesWithTrainerAndTimeframe() {
        Room room = roomRepository.findAll().iterator().next();
        SportType sportType = sportTypeRepository.findAll().iterator().next();
        User user = userRepository.findAll().iterator().next();
        GroupClass groupClass1 = new GroupClass(Long.MAX_VALUE,
                LocalDateTime.of(2023, 3, 20, 9, 30),
                LocalDateTime.of(2023, 3, 20, 11, 30),
                100,
                room,
                sportType,
                Collections.singleton(user));
        GroupClass groupClass2 = new GroupClass(Long.MAX_VALUE,
                LocalDateTime.of(2023, 3, 20, 9, 30),
                LocalDateTime.of(2023, 3, 20, 11, 30),
                100,
                room,
                sportType,
                Collections.singleton(user));
        GroupClass groupClass3 = new GroupClass(Long.MAX_VALUE,
                LocalDateTime.of(2021, 3, 20, 9, 30),
                LocalDateTime.of(2021, 3, 20, 16, 30),
                100,
                room,
                sportType,
                Collections.singleton(user));
        groupClassRepository.save(groupClass1);
        groupClassRepository.save(groupClass2);
        groupClassRepository.save(groupClass3); //this groupClass shouldnt be counting into the sum
        LocalDateTime from = LocalDateTime.of(2022, 2, 20, 9, 30);
        LocalDateTime to = LocalDateTime.of(2024, 4, 20, 11, 30);
        Collection<GroupClass> workedGroupClasses = groupClassRepository.findAllByTrainerAndTime(user, from, to);
        assert (workedGroupClasses.size() == 2);
    }
}
