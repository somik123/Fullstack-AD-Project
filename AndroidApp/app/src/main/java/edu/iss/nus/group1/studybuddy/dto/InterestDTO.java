package edu.iss.nus.group1.studybuddy.dto;

public class InterestDTO {
    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "InterestDTO{" +
                "Id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
