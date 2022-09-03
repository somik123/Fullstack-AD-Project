package sg.edu.nus.iss.team1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.team1.domain.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    @Query
    public List<Event> findByName(String name);

    @Query("SELECT e FROM Event e JOIN e.group.participants p WHERE p.Id=?1")
    public List<Event> findAllByUserId(Integer id);

    @Query("SELECT e FROM Event e ORDER BY e.eventTime DESC")
    public List<Event> findAllEventOrderByDateTimeDesc();
}
