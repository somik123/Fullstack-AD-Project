package sg.edu.nus.iss.team1.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
public class Book {
    @Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Integer Id;
    @NotBlank(message = "Title is mandatory")
    private String title;
    @NotBlank(message = "Description is mandatory")
    private String description;
    private String photo;
    @NotBlank(message = "Author is mandatory")
    private String author;
    @NotBlank(message = "Link is mandatory")
    private String link;
    @NotBlank(message = "Genre is mandatory")
    private String genre;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime publishDate;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public LocalDateTime getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDateTime publishDate) {
        this.publishDate = publishDate;
    }

    public Book() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        return "Book [Id=" + Id + ", title=" + title + ", description=" + description + ", photo=" + photo + ", author="
                + author + ", link=" + link + ", genre=" + genre + ", publishDate=" + publishDate + "]";
    }

}
