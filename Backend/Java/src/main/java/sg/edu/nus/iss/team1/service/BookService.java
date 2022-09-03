package sg.edu.nus.iss.team1.service;

import java.util.List;
import java.util.Optional;

import sg.edu.nus.iss.team1.domain.Book;

public interface BookService {

    public void saveBook(Book book);

    public List<Book> getAllBooks();

    public Book findByTitle(String title);

    public Optional<Book> findById(Integer Id);

    public List<Book> findByAuthor(String author);

    public void deleteById(Integer id);

    public List<Book> findByGenre(String genre);

    public List<Book> findBySearchText(String searchText);
}
