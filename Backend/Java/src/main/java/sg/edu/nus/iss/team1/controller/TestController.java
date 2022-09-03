package sg.edu.nus.iss.team1.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.team1.domain.Event;
import sg.edu.nus.iss.team1.domain.Interest;
import sg.edu.nus.iss.team1.domain.StudyGroup;
import sg.edu.nus.iss.team1.domain.User;
import sg.edu.nus.iss.team1.dto.UserDTO;
import sg.edu.nus.iss.team1.service.EmailService;
import sg.edu.nus.iss.team1.service.EventService;
import sg.edu.nus.iss.team1.service.RoleService;
import sg.edu.nus.iss.team1.service.StudyGroupService;
import sg.edu.nus.iss.team1.service.UserService;
import sg.edu.nus.iss.team1.util.MLApi;

@RestController
@RequestMapping(path = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class TestController {

    @Autowired
    private EmailService emailService;

    @Autowired
    protected EventService eventService;
    @Autowired
    protected UserService userService;
    @Autowired
    protected StudyGroupService studyGroupService;
    @Autowired
    protected RoleService roleService;

    static class EmailRequest {

        public EmailRequest(String email, String subject, String text) {
            this.email = email;
            this.subject = subject;
            this.text = text;
        }

        String email;
        String subject;
        String text;
    }

    @PostMapping("/email")
    public Boolean sendEmail(@RequestBody EmailRequest request) {
        return emailService.SendEmail(new String[]{request.email}, request.subject, request.text);
    }

    @GetMapping("/install")
    public String install() {

        LocalDateTime localTime1 = LocalDateTime.now();
        LocalDateTime localTime2 = LocalDateTime.now().plusDays(4);
        LocalDateTime localTime3 = LocalDateTime.now().plusDays(8);
        LocalDateTime localTime4 = LocalDateTime.now().plusDays(16);
        LocalDateTime localTime5 = LocalDateTime.now().plusDays(55);
        LocalDateTime localTime6 = LocalDateTime.now().plusDays(78);

        // UserDTO user = new UserDTO("username","password","name","email","");
        UserDTO user1 = new UserDTO("somik", "pass123", "Khan Sher Mostafa Somik", "e0941661@u.nus.edu", "");
        userService.saveUserDTO(user1);

        UserDTO user2 = new UserDTO("liyuan", "pass123", "Cheong Li Yuan", "cheong.liyuan@u.nus.edu", "");
        userService.saveUserDTO(user2);

        UserDTO user3 = new UserDTO("nyanhtet", "pass123", "Nyan Htet Win Maung", "e0941691@u.nus.edu", "");
        userService.saveUserDTO(user3);

        UserDTO user4 = new UserDTO("aungkhant", "pass123", "Aung Ko Ko Khant", "aungkokokhant@u.nus.edu", "");
        userService.saveUserDTO(user4);

        UserDTO user5 = new UserDTO("ziyou", "pass123", "Zhao Ziyou", "ziyou.zhao@u.nus.edu", "");
        userService.saveUserDTO(user5);

        UserDTO user6 = new UserDTO("amber", "pass123", "Aye Mya Phoo", "ayemyaphoo@u.nus.edu", "");
        userService.saveUserDTO(user6);

        UserDTO user7 = new UserDTO("tingting", "pass123", "Xie Tingting", "xietingting@u.nus.edu", "");
        userService.saveUserDTO(user7);

        User userA = userService.findByUsername(user1.getUsername());
        StudyGroup group1 = new StudyGroup("C#", false, localTime1, userA, null);
        studyGroupService.saveStudyGroup(group1);

        User userB = userService.findByUsername(user2.getUsername());
        StudyGroup group2 = new StudyGroup("Design", false, localTime1, userB, null);
        studyGroupService.saveStudyGroup(group2);

        User userC = userService.findByUsername(user3.getUsername());
        StudyGroup group3 = new StudyGroup("Python", false, localTime1, userC, null);
        studyGroupService.saveStudyGroup(group3);

        User userD = userService.findByUsername(user4.getUsername());
        StudyGroup group4 = new StudyGroup("Java", false, localTime1, userD, null);
        studyGroupService.saveStudyGroup(group4);

        User userE = userService.findByUsername(user5.getUsername());
        StudyGroup group5 = new StudyGroup("Andorid", false, localTime1, userE, null);
        studyGroupService.saveStudyGroup(group5);

        Event event1 = new Event("Foundation", "FOPCS, OOPCS & ASP.NET", userA, localTime1, group1, localTime2);
        eventService.saveEvent(event1);

        Event event2 = new Event("Design", "ADLC", userB, localTime1, group2, localTime3);
        eventService.saveEvent(event2);

        Event event3 = new Event("Machine Learning", "Clustering and Regression", userC, localTime1, group3,
                localTime4);
        eventService.saveEvent(event3);

        Event event4 = new Event("Web Developing", "Spring and React", userD, localTime1, group4, localTime5);
        eventService.saveEvent(event4);

        Event event5 = new Event("Mobile Developing", "Layouts,intent and Fragments", userE, localTime1, group5,
                localTime6);
        eventService.saveEvent(event5);

        return "{ \"Result\": \"OK\" }";
    }

    @GetMapping("/ml-analyse")
    public String testAnalyse() {
        String stringMessage = "Cats make good pets";

        int maxSleep = 10; // seconds

        MLApi mlApi = new MLApi();
        mlApi.analyzeMessage(stringMessage);

        // While thread is still alive
        for (int i = 0; i < maxSleep * 10 && mlApi.getBkThread().isAlive(); i++) {
            try {
                Thread.sleep(100); // Wait 0.1s at a time for background thread to complete
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
        String reply = mlApi.getMlReply();
        System.out.println(reply);
        return reply;
    }

    @GetMapping("/ml-recommend")
    public String testRecommend() {
        User user = userService.findById(1);
        if (user != null) {
            List<Interest> interest = new ArrayList<>(user.getInterest());

            String stringMessage = "Java is not fun";

            int maxSleep = 10; // seconds

            MLApi mlApi = new MLApi();
            mlApi.recommendBooks(interest, stringMessage);

            // While thread is still alive
            for (int i = 0; i < maxSleep * 10 && mlApi.getBkThread().isAlive(); i++) {
                try {
                    Thread.sleep(100); // Wait 0.1s at a time for background thread to complete
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
            String reply = mlApi.getMlReply();
            System.out.println(reply);
            return "[" + reply + "]";
        }
        return "";
    }


}
