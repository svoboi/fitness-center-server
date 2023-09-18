package cz.cvut.fit.tjv.fitnesscenter.controller;


import cz.cvut.fit.tjv.fitnesscenter.model.SportType;
import cz.cvut.fit.tjv.fitnesscenter.business.SportTypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sportTypes")
public class SportTypeController extends AbstractController<SportType> {

    SportTypeController(SportTypeService sportTypeService) {
        service = sportTypeService;
    }
}
