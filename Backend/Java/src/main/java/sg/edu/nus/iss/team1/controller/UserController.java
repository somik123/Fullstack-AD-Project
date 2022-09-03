package sg.edu.nus.iss.team1.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.team1.domain.Interest;
import sg.edu.nus.iss.team1.domain.User;
import sg.edu.nus.iss.team1.dto.PasswordDTO;
import sg.edu.nus.iss.team1.dto.UserDTO;
import sg.edu.nus.iss.team1.exception.UserNotFoundException;
import sg.edu.nus.iss.team1.service.EmailService;
import sg.edu.nus.iss.team1.service.UserService;
import sg.edu.nus.iss.team1.util.MLApi;

@RestController
@RequestMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        User existingUser = userService.findById(id);
        UserDTO existingUserDTO = new UserDTO();
        if (existingUser != null) {
            existingUserDTO.setId(existingUser.getId());
            existingUserDTO.setUsername(existingUser.getUsername());
            existingUserDTO.setName(existingUser.getName());
            existingUserDTO.setEmail(existingUser.getEmail());
            existingUserDTO.setPhoto(existingUser.getPhoto());
            List<String> interestList = new ArrayList<>();
            for (Interest interest : existingUser.getInterest()) {
                interestList.add(interest.getName());

            }
            existingUserDTO.setInterest(interestList);

        } else {
            throw new UserNotFoundException("No user with this id" + id + "exist.");
        }
        return new ResponseEntity<>(existingUserDTO, HttpStatus.OK);
    }

    @GetMapping("/search/{nameString}")
    public ResponseEntity<?> searchUser(@PathVariable String nameString) {
        List<User> existingUsers = userService.searchUser(nameString);
        List<UserDTO> existingUserDTOList = new ArrayList<UserDTO>();
        for (User user : existingUsers
        ) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setName(user.getName());
            userDTO.setEmail(user.getEmail());
            userDTO.setPhoto(user.getPhoto());
            List<String> interestList = new ArrayList<>();
            for (Interest interest : user.getInterest()) {
                interestList.add(interest.getName());
            }
            userDTO.setInterest(interestList);
            existingUserDTOList.add(userDTO);
        }
        return new ResponseEntity<>(existingUserDTOList, HttpStatus.OK);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> findByName(@PathVariable String username) {
        User existingUser = userService.findByUsername(username);
        UserDTO existingUserDTO = new UserDTO();
        if (existingUser != null) {
            existingUserDTO.setId(existingUser.getId());
            existingUserDTO.setUsername(existingUser.getUsername());
            existingUserDTO.setName(existingUser.getName());
            existingUserDTO.setEmail(existingUser.getEmail());
            existingUserDTO.setPhoto(existingUser.getPhoto());
            List<String> interestList = new ArrayList<>();
            for (Interest interest : existingUser.getInterest()) {
                interestList.add(interest.getName());

            }
            existingUserDTO.setInterest(interestList);

        } else {
            return new ResponseEntity<>("ERROR", HttpStatus.OK);
        }
        return new ResponseEntity<>(existingUserDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@Valid @RequestBody UserDTO user) throws Exception {
        boolean isExist = userService.checkuserusernameOremailExist(user.getUsername(), user.getEmail());
        if (isExist) {
            return new ResponseEntity<>("The same username or email already Existed!", HttpStatus.BAD_REQUEST);
        }

        User newUser = userService.saveUserDTO(user);
        newUserBookRecommendations(newUser, "");

        UserDTO newUserDTO = new UserDTO();
        if (newUser != null) {
            newUserDTO.setId(newUser.getId());
            newUserDTO.setUsername(newUser.getUsername());
            newUserDTO.setName(newUser.getName());
            newUserDTO.setEmail(newUser.getEmail());
            newUserDTO.setPhoto(newUser.getPhoto());
            newUserDTO.setInterest(null);
            sendWelcomeEmail(newUserDTO);
            return ResponseEntity.ok(newUserDTO);
        }
        return new ResponseEntity<>("ERROR", HttpStatus.OK);
    }

    /*
     * @PostMapping("/update") User user(@RequestBody User newUser) {
     * uService.saveUser(newUser); return newUser; }
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        userService.deleteById(id);
        return new ResponseEntity<>("User deleted", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO newUser, @PathVariable Integer id, Authentication auth) {
        if (auth != null) {
            User user = userService.findById(id);
            if (user != null) {
                if (user.getUsername().equalsIgnoreCase(auth.getName())) {
                    user.setName(newUser.getName());
                    user.setEmail(newUser.getEmail());
                    user.setPhoto(newUser.getPhoto());
                    userService.saveUser(user);
                    return new ResponseEntity<>("User updated", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Fail authorization, Cannot change different user",
                            HttpStatus.BAD_REQUEST);
                }
            } else {
                throw new UserNotFoundException("No user with this id " + id + " exist.");
            }

        } else {
            return new ResponseEntity<>("Fail authorization", HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordDTO passwordDTO, Authentication auth) {
        if (auth != null) {
            User existingUser = userService.findById(passwordDTO.getUserId());
            if (existingUser.getUsername().equalsIgnoreCase(auth.getName())) {
                boolean isUpdate = userService.updatePassword(passwordDTO);
                if (isUpdate) {
                    sendChangePasswordEmail(existingUser);
                    return new ResponseEntity<>("User password updated", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("User password cannot update", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>("Fail authorization, Cannot change different user", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Fail authorization", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/photo/{id}")
    public ResponseEntity<?> findPhotoById(@PathVariable Integer id) {
        User existingUser = userService.findById(id);
        if (existingUser != null) {
            return new ResponseEntity<>(existingUser.getPhoto(), HttpStatus.OK);
        } else {
            throw new UserNotFoundException("No user with this id" + id + "exist.");
        }
    }

    @PutMapping("/updatePhoto")
    public ResponseEntity<?> updatePhoto(@RequestBody UserDTO userDTO, Authentication auth) {
        if (auth != null) {
            User existingUser = userService.findById(userDTO.getId());
            if (existingUser.getUsername().equalsIgnoreCase(auth.getName())) {
                userService.updatePhoto(userDTO);
                return new ResponseEntity<>("User password updated", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Fail authorization, Cannot change different user", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Fail authorization", HttpStatus.BAD_REQUEST);
        }
    }

    private void sendWelcomeEmail(UserDTO userDTO) {
        emailService.SendEmail(new String[]{userDTO.getEmail()}, "Welcome to StudyBuddy", "Hi " + userDTO.getName() +
                ", welcome to StudyBuddy!");
    }

    private void sendChangePasswordEmail(User user) {
        emailService.SendEmail(new String[]{user.getEmail()}, "You Changed Password", "Hi " + user.getName() +
                ", you have changed password successfully!");
    }

    private void newUserBookRecommendations(User user, String eventName) {
        try {
            int maxSleep = 10; // seconds
            MLApi mlApi = new MLApi();

            System.out.println(user.getInterest());
            System.out.println(eventName);

            mlApi.recommendBooks(new ArrayList<Interest>(), null);
            for (int i = 0; i < maxSleep * 10 && mlApi.getBkThread().isAlive(); i++) {
                try {
                    Thread.sleep(100); // Wait 0.1s at a time for background thread to complete
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
            String reply = mlApi.getMlReply();
            user.set_bookOrdering(reply);
            user.set_eventLatest(eventName);
            userService.saveUser(user);
        } catch (Exception e) {
            System.out.println(e);
        }
    }


}
