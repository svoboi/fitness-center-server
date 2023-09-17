package cz.cvut.fit.tjv.fitnesscenter.dao;


import cz.cvut.fit.tjv.fitnesscenter.model.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cz.cvut.fit.tjv.fitnesscenter.model.GroupClass;
import java.util.Collection;

@Repository
public interface GroupClassRepository extends CrudRepository<GroupClass, Long> {
    Collection<GroupClass> findAllByRoom(Room room);
}
