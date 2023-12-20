package cz.cvut.fit.tjv.fitnesscenter.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EntityIdentificationException extends RuntimeException {
    public EntityIdentificationException(String errorMessage) {
        super(errorMessage);
    }
}
