package one.digitalinnovation.personapi.service;

import one.digitalinnovation.personapi.dto.mapper.PersonMapper;
import one.digitalinnovation.personapi.dto.request.PersonDTO;
import one.digitalinnovation.personapi.dto.response.MessageResponseDTO;
import one.digitalinnovation.personapi.entities.Person;
import one.digitalinnovation.personapi.exception.PersonNotFoundException;
import one.digitalinnovation.personapi.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private PersonRepository personRepository;

    private final PersonMapper personMapper = PersonMapper.INSTANCE;

    @Autowired
    public PersonService (PersonRepository personRepository){
        this.personRepository = personRepository;
    }

    public MessageResponseDTO createPerson(PersonDTO personDTO){

        Person person = personMapper.toModel(personDTO);
        Person savePerson = personRepository.save(person);

        return MessageResponseDTO.builder()
                .message("Create person with ID "+ savePerson.getId())
                .build();
    }

    public List<PersonDTO> listAll() {
        List<Person> people = personRepository.findAll();
        return people.stream()
                .map(personMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PersonDTO findById(Long id) throws PersonNotFoundException {

         Person person = personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));

         return personMapper.toDTO(person);
    }


    public void delete(Long id) throws PersonNotFoundException {

        verifyIfExists(id);
        personRepository.deleteById(id);

    }

    private void verifyIfExists(Long id) throws PersonNotFoundException {
        personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));
    }
}
