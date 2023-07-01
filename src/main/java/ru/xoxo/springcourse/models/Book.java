package ru.xoxo.springcourse.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.DAYS;

@Entity
@Table(name = "Book")
public class Book {
    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "Title should not be empty")
    @Size(min = 2, max = 100,message = "Title should be between 2 and 100 characters")
    @Column(name = "book_name")
    private String bookName;
    @NotEmpty(message = "Author should not be empty")
    @Size(min = 2, max = 100,message = "Author name should be between 2 and 100 characters")
    @Column(name = "author")
    private String author;
    @Min(value = 1900, message = "Year should be greater than 1900")
    @Column(name = "year_of_production")
    private int yearOfProduction;

    @Column(name = "take_book_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date takeBookTime;

    @Transient
    private boolean expired;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;

    public Book(String bookName, String author, int yearOfProduction) {
        this.bookName = bookName;
        this.author = author;
        this.yearOfProduction = yearOfProduction;
    }

    public Book() {
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public Date getTakeBookTime() {
        return takeBookTime;
    }

    public void setTakeBookTime(Date takeBookTime) {
        this.takeBookTime = takeBookTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYearOfProduction() {
        return yearOfProduction;
    }

    public void setYearOfProduction(int yearOfProduction) {
        this.yearOfProduction = yearOfProduction;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", bookName='" + bookName + '\'' +
                ", author='" + author + '\'' +
                ", yearOfProduction=" + yearOfProduction +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id && yearOfProduction == book.yearOfProduction && Objects.equals(bookName, book.bookName) && Objects.equals(author, book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bookName, author, yearOfProduction);
    }
}
