package cz.cvut.fit.tjv.fitnesscenter;

import cz.cvut.fit.tjv.fitnesscenter.dao.GroupClassRepository;
import cz.cvut.fit.tjv.fitnesscenter.model.GroupClass;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootApplication
public class FitnessCenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitnessCenterApplication.class, args);
	}

}
