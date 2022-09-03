package sg.edu.nus.iss.team1.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Interest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interest_id")
    private Integer Id;
    private String name;


    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Interest() {
        super();
        // TODO Auto-generated constructor stub
    }


    @Override
    public String toString() {
        return "Interest{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                '}';
    }
}
