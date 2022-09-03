package edu.iss.nus.group1.studybuddy.dto;

public class BookDTO {
    private Integer Id;
    private String title;
    private String description;
    private String photo;
    private String author;
    private String link;
    private String genre;
    private String publishDate;

    public BookDTO(Integer id, String title, String description, String photo, String author, String link, String publishDate) {
        Id = id;
        this.title = title;
        this.description = description;
        this.photo = photo;
        this.author = author;
        this.link = link;
        this.publishDate = publishDate;
    }

    public BookDTO() {
        super();
    }

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

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }
}
