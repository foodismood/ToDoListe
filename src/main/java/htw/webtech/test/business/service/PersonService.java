package htw.webtech.test.business.service;

import htw.webtech.test.rest.model.PersonDTO;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class PersonService{
    public List<PersonDTO> getAllPersons(){
        PersonDTO personDTO = new PersonDTO("Sean", "Genkel");
        return List.of(new PersonDTO("Sean","Genkel"));
    }
}
