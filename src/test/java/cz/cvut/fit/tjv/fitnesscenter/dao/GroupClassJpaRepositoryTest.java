package cz.cvut.fit.tjv.fitnesscenter.dao;

import cz.cvut.fit.tjv.fitnesscenter.model.GroupClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//@RunWith(SpringRunner.class)
//@SpringBootTest
@DataJpaTest
class GroupClassJpaRepositoryTest {

    @Autowired
    GroupClassRepository groupClassRepository;

    @AfterEach
    void tearDown() {
        groupClassRepository.deleteAll();
    }

    @Test
    void testOfRepository() {
        var groupClassTest1 = new GroupClass(LocalDate.of(2021, 1, 27),
                LocalTime.of(8, 30),
                LocalTime.of(10, 30),
                100
                );

        groupClassRepository.save(groupClassTest1);

        var result = groupClassRepository.findAll();
//        assert (result.iterator().next().getCapacity() == 100);

    }



}
