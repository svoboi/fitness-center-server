package cz.cvut.fit.tjv.fitnesscenter.dao;


import cz.cvut.fit.tjv.fitnesscenter.model.GroupClass;
import cz.cvut.fit.tjv.fitnesscenter.model.Room;
import cz.cvut.fit.tjv.fitnesscenter.model.SportType;
import cz.cvut.fit.tjv.fitnesscenter.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;

@Repository
public interface GroupClassRepository extends CrudRepository<GroupClass, Long> {
    Collection<GroupClass> findAllByRoom(Room room);

    Collection<GroupClass> findAllBySportType(SportType sportType);

    Collection<GroupClass> findAllByTrainersContaining(User user);

    @Query("SELECT g FROM GroupClass g WHERE g.room = ?1 AND (g.timeFrom BETWEEN ?2 and ?3 OR g.timeTo BETWEEN ?2 and ?3)")
    Collection<GroupClass> findAllByRoomAndTime(Room room, LocalDateTime timeFrom, LocalDateTime timeTo);

    @Query("SELECT g FROM GroupClass g WHERE ?1 member of g.trainers AND (g.timeFrom BETWEEN ?2 and ?3 OR g.timeTo BETWEEN ?2 and ?3)")
    Collection<GroupClass> findAllByTrainerAndTime(User user, LocalDateTime timeFrom, LocalDateTime timeTo);
}
