package cz.cvut.fit.tjv.fitnesscenter.business;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class GroupClassOverlapHelper implements Comparable<GroupClassOverlapHelper> {
    public LocalDateTime index;
    public Boolean end;
    public Integer capacity;

    @Override
    public int compareTo(GroupClassOverlapHelper other) {
        if (index != other.index)
            return index.compareTo(other.index);
        else return end.compareTo(other.end);
    }
}
