package sg.edu.nus.iss.team1.service;

import java.util.List;

import sg.edu.nus.iss.team1.domain.User;
import sg.edu.nus.iss.team1.dto.PasswordDTO;
import sg.edu.nus.iss.team1.dto.UserDTO;

public interface UserService {

    public User findByUsername(String username);

    boolean checkuserusernameOremailExist(String username, String email);

    public User findById(Integer Id);

    public List<User> searchUser(String nameString);

    public void saveUser(User user);

    public void deleteById(Integer id);

    public User saveUserDTO(UserDTO user);

    public List<User> findAllUser();

    boolean updatePassword(PasswordDTO passwordDTO);

    public void update(User u);

    public void updatePhoto(UserDTO userDTO);
}
