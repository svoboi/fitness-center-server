package cz.cvut.fit.tjv.fitnesscenter.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ConflictingEntityExistsException extends RuntimeException {
    public ConflictingEntityExistsException(String errorMessage) {
        super(errorMessage);
    }
}
