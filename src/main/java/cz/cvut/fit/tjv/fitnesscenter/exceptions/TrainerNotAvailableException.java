package cz.cvut.fit.tjv.fitnesscenter.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TrainerNotAvailableException extends RuntimeException {
    public TrainerNotAvailableException(String errorMessage) {
        super(errorMessage);
    }
}
