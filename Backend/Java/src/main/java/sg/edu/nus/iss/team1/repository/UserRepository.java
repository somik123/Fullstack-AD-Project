package sg.edu.nus.iss.team1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.team1.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    public User findUserByUsernameAndPassword(String username, String password);

    public User findUserByUsernameAndEmail(String username, String email);

    public User findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.username like CONCAT('%', ?1, '%')")
    public List<User> searchUser(String nameString);

    public User findByEmail(String email);

    // List all Students/Admins

    @Query("SELECT u FROM User u WHERE u.userRole.name='ADMIN' ")
    public List<User> findAllAdmins();

    @Query("SELECT u FROM User u WHERE u.userRole.name='STUDENT' ")
    public List<User> findAllStudents();

}
