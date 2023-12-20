package cz.cvut.fit.tjv.fitnesscenter.business;

import cz.cvut.fit.tjv.fitnesscenter.exceptions.ConflictingEntityExistsException;
import cz.cvut.fit.tjv.fitnesscenter.exceptions.EntityIdentificationException;
import cz.cvut.fit.tjv.fitnesscenter.exceptions.EntityNotFoundException;
import cz.cvut.fit.tjv.fitnesscenter.model.SportType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SportTypeServiceIntegrationTest {

    @Autowired
    SportTypeService sportTypeServiceService;

    @BeforeEach
    void setUp() {
        SportType sportType1 = new SportType(Long.MAX_VALUE, "one");
        sportTypeServiceService.create(sportType1);
    }

    @AfterEach
    void tearDown() {
        var sportTypes = sportTypeServiceService.findAll();
        for (SportType sportType : sportTypes) {
            sportTypeServiceService.deleteById(sportType.getId());
        }
    }

    @Test
    void shouldFind1InFindAll() {
        var sportTypes = sportTypeServiceService.findAll();
        assert (sportTypes.size() == 1);
        assert (sportTypes.iterator().next().getTypeName().equals("one"));
    }

    @Test
    void shouldFindByID() {
        var sportTypes = sportTypeServiceService.findAll();
        SportType lookedForSportType = sportTypes.iterator().next();
        var foundSportType = sportTypeServiceService.findById(lookedForSportType.getId());
        assert (foundSportType.isPresent());
        assert (lookedForSportType.getId().equals(foundSportType.get().getId()));
    }

    @Test
    void shouldNotFindByID() {
        var sportTypes = sportTypeServiceService.findAll();
        SportType sportType = sportTypes.iterator().next();
        Long wrongID = sportType.getId().equals(1L) ? 2L : 1L;
        var foundSportType = sportTypeServiceService.findById(wrongID);
        assert (foundSportType.isEmpty());
    }

    @Test
    void shouldThrowConflict() {
        var sportTypes = sportTypeServiceService.findAll();
        SportType foundSportType = sportTypes.iterator().next();
        SportType conflictingSportType = new SportType(foundSportType.getId(), "one");
        Assertions.assertThrows(
                ConflictingEntityExistsException.class,
                () -> sportTypeServiceService.create(conflictingSportType)
        );
    }

    @Test
    void shouldUpdate() {
        var sportTypes = sportTypeServiceService.findAll();
        SportType foundSportType = sportTypes.iterator().next();
        foundSportType.setTypeName("changed");
        sportTypeServiceService.update(foundSportType, foundSportType.getId());

        SportType updatedSportType = sportTypes.iterator().next();
        assert (updatedSportType.getTypeName().equals("changed"));
    }

    @Test
    void shouldThrowIdentificationExceptionUpdate() {
        var sportTypes = sportTypeServiceService.findAll();
        SportType foundSportType = sportTypes.iterator().next();
        Long wrongID = foundSportType.getId().equals(1L) ? 2L : 1L;
        Assertions.assertThrows(
                EntityIdentificationException.class,
                () -> sportTypeServiceService.update(foundSportType, wrongID)
        );
    }

    @Test
    void shouldThrowEntityNotFoundUpdate() {
        var sportTypes = sportTypeServiceService.findAll();
        SportType foundSportType = sportTypes.iterator().next();
        Long wrongID = foundSportType.getId().equals(1L) ? 2L : 1L;
        foundSportType.setId(wrongID);
        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> sportTypeServiceService.update(foundSportType, wrongID)
        );
    }

    @Test
    void shouldDelete() {
        var sportTypes = sportTypeServiceService.findAll();
        SportType foundSportType = sportTypes.iterator().next();
        sportTypeServiceService.deleteById(foundSportType.getId());
        assert (sportTypeServiceService.findAll().isEmpty());
    }

}
