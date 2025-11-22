package htw.webtech.test.business.service;

import htw.webtech.test.rest.model.PersonDTO;


import java.util.List;

public class PersonService{
    public List<PersonDTO> getAllPersons(){
        PersonDTO personDTO = new PersonDTO("Sean", "Genkel");
        return List.of(new PersonDTO("Sean","Genkel"));
    }
}
