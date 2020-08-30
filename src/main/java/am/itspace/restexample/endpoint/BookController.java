package am.itspace.restexample.endpoint;


import am.itspace.restexample.exception.ResourceNotFoundException;
import am.itspace.restexample.model.Book;
import am.itspace.restexample.repository.BookRepository;
import am.itspace.restexample.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//CRUD
@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/books")
    public List<Book> getBooks() {
        return bookService.findAll();
    }

    @GetMapping("/book/{id}")
    public Book getById( @PathVariable("id") int id){
        return bookService.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Book does not exist"));
    }

    @PostMapping("/books")
    public Book create(@RequestBody Book book) {
        if (book.getId() > 0) {
            throw new RuntimeException("Id must be 0");
        }

        return bookService.save(book);
    }

    @PutMapping("/books/{id}")
    public Book update(@RequestBody Book book, @PathVariable("id") int id) {

        Book bookFromDB = bookService.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Book does not exist"));
        bookFromDB.setTitle(book.getTitle());
        bookFromDB.setDescription(book.getDescription());
        bookFromDB.setPrice(book.getPrice());
        bookFromDB.setAuthorName(book.getAuthorName());
        return bookService.save(bookFromDB);
    }

    @DeleteMapping("/books/{id}")
    public void delete(@PathVariable("id") int id){
        bookService.delete(bookService.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Book does not exist")));
    }


}
