package ru.xoxo.springcourse.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.xoxo.springcourse.models.Book;
import ru.xoxo.springcourse.models.Person;
import ru.xoxo.springcourse.repositories.BookRepository;
import ru.xoxo.springcourse.repositories.PersonRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {
    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getAllPeople() {
        return personRepository.findAll();
    }

    public Person getOneById(int id) {
        return personRepository.findById(id).orElse(null);
    }

    @Transactional
    public void savePerson(Person person) {
        personRepository.save(person);
    }

    @Transactional
    public void updateById(int id, Person updatePerson) {
        updatePerson.setId(id);
        personRepository.save(updatePerson);
    }

    @Transactional
    public void deleteById(int id) {
        personRepository.deleteById(id);
    }

    public List<Book> getBooksByPersonId(int id) {
        Optional<Person> person = Optional.ofNullable(getOneById(id));
        if (person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());
            person.get().getBooks().forEach(book -> {
                long diff = Math.abs(book.getTakeBookTime().getTime() - new Date().getTime());
                if(diff > 864_000_000) {
                    book.setExpired(true);
                }
            });
            return person.get().getBooks();
        } else {
            return Collections.emptyList();
        }
    }

    public Optional<Person> findPersonByName(String name) {
        return personRepository.findPersonByName(name);
    }
}
