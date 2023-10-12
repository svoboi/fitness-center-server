package cz.cvut.fit.tjv.fitnesscenter.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UsernameTakenException extends RuntimeException {
    public UsernameTakenException(String errorMessage) {
        super(errorMessage);
    }
}
