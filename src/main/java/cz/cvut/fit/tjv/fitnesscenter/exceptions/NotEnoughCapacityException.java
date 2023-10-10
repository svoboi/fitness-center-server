package cz.cvut.fit.tjv.fitnesscenter.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotEnoughCapacityException extends RuntimeException {
    public NotEnoughCapacityException(String errorMessage) {
        super(errorMessage);
    }
}
