package cz.cvut.fit.tjv.fitnesscenter.business;

import cz.cvut.fit.tjv.fitnesscenter.dao.SportTypeRepository;
import cz.cvut.fit.tjv.fitnesscenter.exceptions.ConflictingEntityExistsException;
import cz.cvut.fit.tjv.fitnesscenter.exceptions.EntityIdentificationException;
import cz.cvut.fit.tjv.fitnesscenter.exceptions.EntityNotFoundException;
import cz.cvut.fit.tjv.fitnesscenter.model.SportType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.Optional;

@SpringBootTest
public class SportTypeServiceUnitTest {
    @Autowired
    SportTypeService sportTypeService;

    @MockBean
    SportTypeRepository sportTypeRepository;

    @Test
    void shouldCreate() {
        SportType sportType = new SportType(1L, "one");

        Mockito.when(sportTypeRepository.save(sportType)).thenReturn(sportType);
        sportTypeService.create(sportType);
        Mockito.verify(sportTypeRepository, Mockito.times(1)).save(sportType);
    }

    @Test
    void shouldThrowConflictCreate() {
        SportType conflictingSportType = new SportType(1L, "one");
        Mockito.when(sportTypeRepository.existsById(1L)).thenReturn(Boolean.TRUE);

        Assertions.assertThrows(
                ConflictingEntityExistsException.class,
                () -> sportTypeService.create(conflictingSportType)
        );
    }

    @Test
    void shouldFind1InFindAll() {
        SportType sportType = new SportType(1L, "one");
        Mockito.when(sportTypeRepository.findAll()).thenReturn(Collections.singleton(sportType));

        var sportTypes = sportTypeService.findAll();
        assert (sportTypes.size() == 1);
        assert (sportTypes.iterator().next().getTypeName().equals("one"));
    }

    @Test
    void shouldFindByID() {
        SportType sportType = new SportType(1L, "one");
        Mockito.when(sportTypeRepository.findById(1L)).thenReturn(Optional.of(sportType));

        var foundSportType = sportTypeService.findById(1L);
        assert (foundSportType.isPresent());
        assert (foundSportType.get().getId().equals(1L));
    }

    @Test
    void shouldNotFindByID() {
        Mockito.when(sportTypeRepository.findById(1L)).thenReturn(Optional.empty());

        var foundSportType = sportTypeService.findById(1L);
        assert (foundSportType.isEmpty());
    }

    @Test
    void shouldUpdate() {
        SportType sportType = new SportType(1L, "changed");
        Mockito.when(sportTypeRepository.existsById(1L)).thenReturn(Boolean.TRUE);
        Mockito.when(sportTypeRepository.save(sportType)).thenReturn(sportType);

        assert (sportTypeService.update(sportType, sportType.getId()).getTypeName().equals("changed"));
        Mockito.verify(sportTypeRepository, Mockito.times(1)).save(sportType);
    }

    @Test
    void shouldThrowIdentificationExceptionUpdate() {
        SportType sportType = new SportType(1L, "one");

        Assertions.assertThrows(
                EntityIdentificationException.class,
                () -> sportTypeService.update(sportType, 2L)
        );
    }

    @Test
    void shouldThrowEntityNotFoundUpdate() {
        SportType sportType = new SportType(1L, "one");
        Mockito.when(sportTypeRepository.existsById(1L)).thenReturn(Boolean.FALSE);


        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> sportTypeService.update(sportType, sportType.getId())
        );
    }

    @Test
    void shouldDelete() {
        sportTypeService.deleteById(1L);
        Mockito.verify(sportTypeRepository, Mockito.times(1)).deleteById(1L);
    }
}
