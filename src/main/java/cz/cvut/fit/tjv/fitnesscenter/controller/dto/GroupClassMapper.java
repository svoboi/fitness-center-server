package cz.cvut.fit.tjv.fitnesscenter.controller.dto;


import cz.cvut.fit.tjv.fitnesscenter.model.GroupClass;
import cz.cvut.fit.tjv.fitnesscenter.model.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GroupClassMapper extends Mapper<GroupClass> {
    @Override
    public Object toDto(GroupClass groupClass) {
        Set<Long> trainers = groupClass
                .getTrainers()
                .stream()
                .map(User::getId)
                .collect(Collectors.toSet());
        return new GroupClassDto(groupClass.getId(), groupClass.getDay(),
                groupClass.getTimeFrom(), groupClass.getTimeTo(), groupClass.getCapacity(),
                groupClass.getRoom(), groupClass.getSportType(), trainers);
    }
}