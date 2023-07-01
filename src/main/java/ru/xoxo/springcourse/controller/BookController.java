package ru.xoxo.springcourse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.xoxo.springcourse.models.Book;
import ru.xoxo.springcourse.models.Person;
import ru.xoxo.springcourse.services.BookService;
import ru.xoxo.springcourse.services.PersonService;

import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final PersonService personService;

    @Autowired
    public BookController(BookService bookService, PersonService personService) {
        this.bookService = bookService;
        this.personService = personService;
    }

    @GetMapping()
    public String getAllBooks(Model model, @RequestParam(value = "page", required = false) Integer page,
                              @RequestParam(value = "per_page", required = false) Integer perPage,
                              @RequestParam(value = "sort_by_year", required = false) boolean sortByYear) {

        if(page == null || perPage == null) {
            model.addAttribute("books", bookService.findAll(sortByYear));
        } else {
            model.addAttribute("books", bookService.findWithPagination(page, perPage, sortByYear));
        }
        return "books/index";
    }

    @GetMapping("/{id}")
    public String getBookById(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", bookService.getOneById(id));
        Person bookOwner = bookService.getBookOwner(id);
        if(bookOwner != null) {
            model.addAttribute("owner", bookOwner);
        } else {
            model.addAttribute("people", personService.getAllPeople());
        }
        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping
    public String createBook(@ModelAttribute("book") @Valid Book book, BindingResult result) {
        if (result.hasErrors()) {
            return "books/new";
        }
        bookService.saveBook(book);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String searchPage() {
        return "books/search";
    }

    @PostMapping("/search")
    public String searchBook(Model model, @RequestParam("bookName") String bookName) {
        model.addAttribute("books", bookService.findBookByBookNameStartingWith(bookName));
        return "books/search";
    }


    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookService.getOneById(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String updateBookById(@ModelAttribute("book") @Valid Book book, BindingResult result,
                                 @PathVariable("id") int id) {
        if (result.hasErrors()) {
            return "books/edit";
        }
        bookService.updateById(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String deleteBookById(@PathVariable("id") int id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        bookService.release(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        bookService.assign(id, person);
        return "redirect:/books/" + id;
    }
}
