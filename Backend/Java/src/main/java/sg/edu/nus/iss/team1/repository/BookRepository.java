package sg.edu.nus.iss.team1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.team1.domain.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    @Query
    public Book findByTitle(String title);

    @Query
    public List<Book> findByAuthor(String author);

    @Query
    public List<Book> findByGenre(String genre);

    @Query("SELECT b from Book b " +
            "WHERE b.author LIKE CONCAT('%',?1,'%') OR " +
            "b.description LIKE CONCAT('%', ?1, '%') OR " +
            "b.genre LIKE CONCAT('%', ?1, '%') OR " +
            "b.title LIKE CONCAT('%', ?1, '%')")
    public List<Book> findBooksBySearchString(String searchTxt);
}
