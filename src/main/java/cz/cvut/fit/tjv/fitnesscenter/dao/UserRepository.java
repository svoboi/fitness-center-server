package cz.cvut.fit.tjv.fitnesscenter.dao;

import cz.cvut.fit.tjv.fitnesscenter.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
