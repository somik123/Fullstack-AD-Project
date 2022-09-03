package sg.edu.nus.iss.team1.controller;

import java.util.*;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.thymeleaf.util.StringUtils;
import sg.edu.nus.iss.team1.domain.Book;
import sg.edu.nus.iss.team1.domain.User;
import sg.edu.nus.iss.team1.dto.BookSearchDTO;
import sg.edu.nus.iss.team1.service.BookService;
import sg.edu.nus.iss.team1.service.UserService;
import sg.edu.nus.iss.team1.util.MLApi;

@RestController
@RequestMapping(path = "/book", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    UserService uService;

    @GetMapping("/all")
    public ResponseEntity<?> findAllBooks() {
        return new ResponseEntity<>(bookService.getAllBooks(), HttpStatus.OK);
    }

    /**
     * Gets all textbooks, ordered according to User's _bookOrdering values. Aka, textbook recommendations
     *
     * @param username User's username
     * @return List of all books
     */
    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUserOrderedBooks(@PathVariable String username) {
        User user = uService.findByUsername(username);

        if (user != null) {
            List<Book> userOrderedBooks = new ArrayList<>();
            List<Book> allBooks = bookService.getAllBooks();

            String order = user.get_bookOrdering();
            String[] ordering = order.split(",");

            //top 20 books
            for (int i = 0; i < 50; i++) {
                userOrderedBooks.add(allBooks.get(Integer.parseInt(ordering[i]) - 1));
            }

            return new ResponseEntity<>(userOrderedBooks, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User does not exist. Book list is not returned.", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Gets all textbooks according to searchText. Gets top 5 results from ML server(cosine similarity), then gets sql results
     *
     * @param bookSearchDTO searchText (json)
     * @return List of found books
     */
    @PostMapping("/search")
    public ResponseEntity<?> getSearchResultsBooks(@RequestBody BookSearchDTO bookSearchDTO) {
        LinkedHashSet<Book> bookList = new LinkedHashSet<>();
        //a)Get cosine similarity results
        //get top 5 book order from ML server
        int maxSleep = 10; // seconds
        MLApi mlApi = new MLApi();

        if (StringUtils.isEmpty(bookSearchDTO.getSearchText())) {
            System.out.println("nothing in search string ");
            return new ResponseEntity<>(bookList, HttpStatus.OK);
        }

        mlApi.searchBooks(bookSearchDTO.getSearchText());
        // While thread is still alive
        for (int i = 0; i < maxSleep * 10 && mlApi.getBkThread().isAlive(); i++) {
            try {
                Thread.sleep(100); // Wait 0.1s at a time for background thread to complete
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
        String reply = mlApi.getMlReply();

        if (!reply.isEmpty()) {
            String[] top5 = reply.split(",");
            for (String s : top5) {
                Optional<Book> b = bookService.findById(Integer.parseInt(s));
                if (b.isPresent()) {
                    Book temp = b.get();
                    bookList.add(temp);
                }
            }
        }
        //b)Get SQL LIKE results
        bookList.addAll(bookService.findBySearchText(bookSearchDTO.getSearchText()));
        //empty results are possible and allowed
        return new ResponseEntity<>(bookList, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> findBookById(@PathVariable Integer id) {
        Optional<Book> existingBook = bookService.findById(id);
        if (existingBook.isPresent()) {
            return new ResponseEntity<>(existingBook, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No book with this Id " + id + " exist.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<?> findBooksByGenre(@PathVariable String genre) {
        List<Book> bookList = bookService.findByGenre(genre);
        if (!bookList.isEmpty()) {
            return new ResponseEntity<>(bookList, HttpStatus.OK);
        }
        return new ResponseEntity<>("No book with this genre exist", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<?> findBookByTitle(@PathVariable String title) {
        Book book = bookService.findByTitle(title);
        if (book != null) {
            return new ResponseEntity<>(book, HttpStatus.OK);
        }
        return new ResponseEntity<>("No book with this title exist", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/author/{author}")
    public ResponseEntity<?> findBookByAuthor(@PathVariable String author) {
        List<Book> bookList = bookService.findByAuthor(author);
        if (!bookList.isEmpty()) {
            return new ResponseEntity<>(bookList, HttpStatus.OK);
        }
        return new ResponseEntity<>("No book with this author exist", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteBookById(@PathVariable Integer id) {
        bookService.deleteById(id);
        return new ResponseEntity<>("Book deleted", HttpStatus.OK);
    }

    @PostMapping("/")
    ResponseEntity<?> createBookById(@Valid @RequestBody Book newBook) {
        bookService.saveBook(newBook);
        return new ResponseEntity<>("Book saved", HttpStatus.OK);
    }
}
