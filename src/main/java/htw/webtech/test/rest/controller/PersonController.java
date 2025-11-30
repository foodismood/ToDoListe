package htw.webtech.test.rest.controller;


import htw.webtech.test.business.service.PersonService;
import htw.webtech.test.rest.model.PersonDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService){
        this.personService = personService;
    }
    @CrossOrigin
    @GetMapping("/personas")
    public ResponseEntity<List<PersonDTO>>getPersonas(){
        return ResponseEntity.ok(personService.getAllPersons());
    }





}
