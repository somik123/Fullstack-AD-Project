package sg.edu.nus.iss.team1.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.iss.team1.domain.StudyGroup;
import sg.edu.nus.iss.team1.domain.User;

public interface StudyGroupRepository extends JpaRepository<StudyGroup, Integer> {
    public StudyGroup findByName(String name);

    public List<StudyGroup> findByCreateDate(LocalDateTime createDate);

    public Optional<StudyGroup> findById(Integer Id);

    public List<StudyGroup> findByParticipantsOrderByLastactiveTimeDesc(User existingUser);

    public List<StudyGroup> findByParticipantsAndIsPrivateOrderByLastactiveTimeDesc(User existingUser, boolean check);
}
