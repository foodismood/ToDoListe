package htw.webtech.test.business.service;

import java.util.List;

public class PersonService {

    public List<PersonDTO> getAllPersons(){
        return List.of(
                new PersonDTO("Suad","Numanovic"),
                new PersonDTO("Sean", "Genkel")
        );
    }
}
