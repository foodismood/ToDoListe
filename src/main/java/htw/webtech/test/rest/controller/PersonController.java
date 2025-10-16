package htw.webtech.test.rest.controller;

import htw.webtech.test.business.service.PersonService;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class PersonController {
    private final PersonService personService;

    public PersonController (PersonService personService){
        this.personService = personService;
    }
    @getMapping
    public ResponseEntity<List<PersonDTO>> getPersonas(){
        return ResponseEntity.ok(personService.getAllPersons());
    }
}
