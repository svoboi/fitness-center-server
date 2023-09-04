package cz.cvut.fit.tjv.fitnesscenter.dao;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cz.cvut.fit.tjv.fitnesscenter.model.GroupClass;

@Repository
public interface GroupClassRepository extends CrudRepository<GroupClass, Long> {
}
