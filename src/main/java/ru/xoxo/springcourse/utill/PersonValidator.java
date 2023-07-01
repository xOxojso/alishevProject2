package ru.xoxo.springcourse.utill;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.xoxo.springcourse.models.Person;
import ru.xoxo.springcourse.services.PersonService;

@Component
public class PersonValidator implements Validator {
    private final PersonService personService;

    @Autowired
    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;
        if(personService.findPersonByName(person.getName()).isPresent()){
            errors.rejectValue("fullName", "", "человек с таким ФИО уже существует");
        }
    }
}
