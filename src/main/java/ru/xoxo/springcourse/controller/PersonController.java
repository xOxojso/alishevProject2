package ru.xoxo.springcourse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.xoxo.springcourse.models.Person;
import ru.xoxo.springcourse.services.PersonService;
import ru.xoxo.springcourse.utill.PersonValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PersonController {
    private final PersonService personService;
    private final PersonValidator personValidator;

    @Autowired
    public PersonController(PersonService personService, PersonValidator personValidator) {
        this.personService = personService;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String getAllPeople(Model model) {
        model.addAttribute("people", personService.getAllPeople());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String getPersonById(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personService.getOneById(id));
        model.addAttribute("books", personService.getBooksByPersonId(id));
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping()
    public String createPerson(@ModelAttribute("person") @Valid Person person, BindingResult result) {
        personValidator.validate(person, result);
        if (result.hasErrors()) {
            return "people/new";
        }
        personService.savePerson(person);
        return "redirect:/people";
    }


    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        Person person = personService.getOneById(id);
        model.addAttribute("person", person);
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String updatePerson(@ModelAttribute("person") @Valid Person person, BindingResult result, @PathVariable("id") int id) {
        if (result.hasErrors()) {
            return "people/edit";
        }
        personService.updateById(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String deletePersonById(@PathVariable("id") int id) {
        personService.deleteById(id);
        return "redirect:/people";
    }
}
