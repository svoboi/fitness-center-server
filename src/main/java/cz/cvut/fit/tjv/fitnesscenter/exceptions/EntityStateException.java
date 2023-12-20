package cz.cvut.fit.tjv.fitnesscenter.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EntityStateException extends RuntimeException {

    public EntityStateException(String errorMessage) {
        super(errorMessage);
    }
}
