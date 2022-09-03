package sg.edu.nus.iss.team1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.team1.domain.Interest;
import sg.edu.nus.iss.team1.domain.User;
import sg.edu.nus.iss.team1.dto.InterestRequest;
import sg.edu.nus.iss.team1.service.InterestService;
import sg.edu.nus.iss.team1.service.UserService;
import sg.edu.nus.iss.team1.util.MLApi;

@RestController
@RequestMapping(path = "/interest", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class InterestController {
    @Autowired
    private InterestService interestService;

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    List<Interest> getAllInterest() {
        return interestService.getAllInterest();
    }

    @PostMapping("/edit")
    ResponseEntity<String> updateInterest(@RequestBody InterestRequest request, Authentication auth) {
        if (auth != null) {
            User user = userService.findById(request.getUserId());
            if (user.getUsername().equalsIgnoreCase(auth.getName())) {
                List<Interest> interests = interestService.getInterestById(request.getInterests());
                user.setInterest(interests);
                userService.saveUser(user);

                //To not make user wait for book recommendations change upon interest change
                Thread bkThread = new Thread(() -> {
                    try {
                        int maxSleep = 10; // seconds
                        MLApi mlApi = new MLApi();

                        mlApi.recommendBooks(user.getInterest(), user.get_eventLatest());
                        for (int i = 0; i < maxSleep * 10 && mlApi.getBkThread().isAlive(); i++) {
                            try {
                                Thread.sleep(100); // Wait 0.1s at a time for background thread to complete
                            } catch (InterruptedException e) {
                                System.out.println(e);
                            }
                        }
                        String reply = mlApi.getMlReply();
                        user.set_bookOrdering(reply);
                        userService.saveUser(user);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                });
                bkThread.start();
                return ResponseEntity.status(HttpStatus.OK).body("ok");
            } else {
                return new ResponseEntity<>("Fail authorization, Cannot change different user", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Fail authorization", HttpStatus.BAD_REQUEST);
        }
    }
}
