package sg.edu.nus.iss.team1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.team1.domain.Role;
import sg.edu.nus.iss.team1.domain.User;
import sg.edu.nus.iss.team1.dto.PasswordDTO;
import sg.edu.nus.iss.team1.dto.UserDTO;
import sg.edu.nus.iss.team1.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findById(Integer Id) {
        return userRepository.findById(Id).get();
    }

    @Override
    public List<User> searchUser(String nameString) {
        return userRepository.searchUser(nameString);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    // user role is hardcoded
    @Override
    public User saveUserDTO(UserDTO user) {
        Role userRole = new Role();
        userRole.setId(1);
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        if (user.getPhoto() != null && user.getPhoto().length() > 5)
            newUser.setPhoto(user.getPhoto());
        else
            newUser.setPhoto("http://team1ad.site:8080/file/2022-08-15_16-59-35_931446.png");
        newUser.setUserRole(userRole);
        return userRepository.save(newUser);
    }

    @Override
    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    @Override
    public boolean checkuserusernameOremailExist(String username, String email) {
        User existingUser;
        existingUser = userRepository.findByUsername(username);
        if (existingUser != null) {
            return true;
        } else {
            existingUser = userRepository.findByEmail(email);
            if (existingUser != null) {
                return true;
            } else {
                return false;
            }
        }

    }

    @Override
    public boolean updatePassword(PasswordDTO passwordDTO) {
        if (userRepository.findById(passwordDTO.getUserId()).isPresent()) {
            User existingUser = userRepository.findById(passwordDTO.getUserId()).get();
            if (bcryptEncoder.matches(passwordDTO.getCurrentPassword(), existingUser.getPassword())) {
                existingUser.setPassword(bcryptEncoder.encode(passwordDTO.getUpdatePassword()));
                userRepository.saveAndFlush(existingUser);
                return true;
            } else
                return false;
        } else {
            return false;
        }
    }

    @Override
    public void update(User u) {
        userRepository.save(u);
    }

    @Override
    public void updatePhoto(UserDTO userDTO) {
        // TODO Auto-generated method stub
        User existingUser = userRepository.findById(userDTO.getId()).get();
        existingUser.setPhoto(userDTO.getPhoto());
        userRepository.saveAndFlush(existingUser);
    }

}
