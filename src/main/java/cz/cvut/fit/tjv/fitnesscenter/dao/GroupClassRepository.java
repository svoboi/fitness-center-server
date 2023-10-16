package cz.cvut.fit.tjv.fitnesscenter.dao;


import cz.cvut.fit.tjv.fitnesscenter.model.GroupClass;
import cz.cvut.fit.tjv.fitnesscenter.model.Room;
import cz.cvut.fit.tjv.fitnesscenter.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;

@Repository
public interface GroupClassRepository extends CrudRepository<GroupClass, Long> {
    Collection<GroupClass> findAllByRoom(Room room);

    Collection<GroupClass> findAllByTimeFromBetweenAndTrainersContainingOrTimeToBetweenAndTrainersContaining
            (LocalDateTime timeFromFrom, LocalDateTime timeFromTo,
             User user1,
             LocalDateTime timeToFrom, LocalDateTime timeToTo,
             User user2);
}
