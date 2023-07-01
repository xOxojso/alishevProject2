package ru.xoxo.springcourse.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.xoxo.springcourse.models.Book;
import ru.xoxo.springcourse.models.Person;
import ru.xoxo.springcourse.repositories.BookRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll(boolean sortByYear) {
        if(sortByYear) {
            return bookRepository.findAll(Sort.by("yearOfProduction"));
        } else {
            return bookRepository.findAll();
        }
    }

    public List<Book> findWithPagination(Integer page, Integer perPage, boolean sortByYear) {
        if(sortByYear) {
            return bookRepository.findAll(PageRequest.of(page, perPage, Sort.by("yearOfProduction"))).getContent();
        } else {
            return bookRepository.findAll(PageRequest.of(page, perPage)).getContent();
        }
    }

    public List<Book> findBookByBookNameStartingWith(String bookName) {
        return bookRepository.findBookByBookNameStartingWith(bookName);
    }

    public Book getOneById(int id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.orElse(null);
    }

    @Transactional
    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void updateById(int id, Book updateBook) {
        Book book = bookRepository.findById(id).get();
        updateBook.setId(id);
        updateBook.setOwner(book.getOwner());
        bookRepository.save(updateBook);
    }

    @Transactional
    public void deleteById(int id) {
        bookRepository.deleteById(id);
    }

    public Person getBookOwner(int id) {
        return bookRepository.findById(id).map(Book::getOwner).orElse(null);
    }

    @Transactional
    public void release(int id) {
        bookRepository.findById(id).ifPresent(book -> {
            book.setOwner(null);
            book.setTakeBookTime(null);
        });
    }

    @Transactional
    public void assign(int id, Person person) {
        bookRepository.findById(id).ifPresent(book -> {
            book.setOwner(person);
            book.setTakeBookTime(new Date());
        });
    }
}
