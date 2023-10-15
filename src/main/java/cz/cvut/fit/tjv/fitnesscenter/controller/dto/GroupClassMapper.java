package cz.cvut.fit.tjv.fitnesscenter.controller.dto;


import cz.cvut.fit.tjv.fitnesscenter.model.GroupClass;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Vector;

@Component
public class GroupClassMapper extends Mapper<GroupClass> {
    @Override
    public Object toDto(GroupClass groupClass) {
        Vector<Map<String, Long>> trainers = new Vector<>();
        if (groupClass.getTrainers() != null) {
            for (var el : groupClass.getTrainers()) {
                trainers.add(Map.of("id", el.getId()));
            }
//            trainers.put("id", groupClass
//                    .getTrainers()
//                    .stream()
//                    .map(User::getId)
//                    .collect(Collectors.toList()));
        }
        return new GroupClassDto(groupClass.getId(),
                groupClass.getTimeFrom(), groupClass.getTimeTo(), groupClass.getCapacity(),
                groupClass.getRoom(), groupClass.getSportType(), trainers);
    }
}