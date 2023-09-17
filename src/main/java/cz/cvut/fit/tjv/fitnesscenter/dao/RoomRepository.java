package cz.cvut.fit.tjv.fitnesscenter.dao;

import cz.cvut.fit.tjv.fitnesscenter.model.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {
}
