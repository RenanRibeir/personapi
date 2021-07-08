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

        return createMessageResponse(savePerson.getId(),"Person successfully updated with ID "+savePerson.getId());
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

    public MessageResponseDTO updateById(Long id, PersonDTO personDTO) throws PersonNotFoundException {

        verifyIfExists(id);

        Person person = personMapper.toModel(personDTO);
        Person updatePerson = personRepository.save(person);

        return createMessageResponse(updatePerson.getId(),"Person successfully updated with ID "+updatePerson.getId());
    }

    private void verifyIfExists(Long id) throws PersonNotFoundException {
        personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));
    }

    private MessageResponseDTO createMessageResponse(Long id,String message) {
        return MessageResponseDTO.builder()
                .message(message + id)
                .build();
    }
}
