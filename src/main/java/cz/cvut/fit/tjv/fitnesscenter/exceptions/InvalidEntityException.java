package cz.cvut.fit.tjv.fitnesscenter.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidEntityException extends RuntimeException {
    public InvalidEntityException(String errorMessage) {
        super(errorMessage);
    }
}
