package cz.cvut.fit.tjv.fitnesscenter.dao;

import cz.cvut.fit.tjv.fitnesscenter.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user1 = new User(Long.MAX_VALUE,
                "Troy",
                "Bolton",
                "troybolton",
                "password123",
                "troy.bolton@easthigh.com",
                789456123L,
                Boolean.FALSE,
                Boolean.TRUE);
        userRepository.save(user1);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void nobodyWithUsername() {
        assert (userRepository.findByUsername("jareknohavica").isEmpty());
    }

    @Test
    void userWithUsername() {
        var user = userRepository.findByUsername("troybolton");
        assert (user.isPresent());
        assert (user.get().getUsername().equals("troybolton"));
    }
}
