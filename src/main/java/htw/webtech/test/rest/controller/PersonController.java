package htw.webtech.test.rest.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public class PersonController {
   private final PersonService personService;

   public PersonController(PersonService personService){
       this.personService = personService;
   }
   @GetMapping("/personas")
    public ResponseEntity<List>PersonDTO>>getPersonas(){
       return ResponseEntity.ok()personService.getAllPersons());
    }
}
