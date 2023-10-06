package cz.cvut.fit.tjv.fitnesscenter.business;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EntityStateException extends RuntimeException {

    public EntityStateException(String errorMessage) {
        super(errorMessage);
    }
}
