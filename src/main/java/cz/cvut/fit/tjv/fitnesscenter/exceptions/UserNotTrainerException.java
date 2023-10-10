package cz.cvut.fit.tjv.fitnesscenter.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserNotTrainerException extends RuntimeException {
    public UserNotTrainerException(String errorMessage) {
        super(errorMessage);
    }
}
