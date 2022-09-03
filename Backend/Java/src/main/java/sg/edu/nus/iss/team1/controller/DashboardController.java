package sg.edu.nus.iss.team1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.team1.domain.*;
import sg.edu.nus.iss.team1.dto.DashboardDTO;
import sg.edu.nus.iss.team1.dto.DashboardDTO.GroupParticipantRateDTO;
import sg.edu.nus.iss.team1.service.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    UserService userService;

    @Autowired
    StudyGroupService studyGroupService;

    @Autowired
    BookService bookService;

    @Autowired
    RoleService roleService;

    @Autowired
    MessageService messageService;

    @Autowired
    EventService eventService;

    @GetMapping(path = "/home")
    public String home(Model model) {

        List<User> users = userService.findAllUser();
        model.addAttribute("users", users);

        List<Event> events = eventService.getAllEvents();
        model.addAttribute("events", events);

        List<StudyGroup> studyGroups = studyGroupService.findAllStudyGroup();
        for (StudyGroup g : studyGroups) {
            System.out.println(g.getName());
            System.out.println(g.getParticipants().size());
            List<Message> messageList = messageService.findMessageByGroupId(g.getId());
            System.out.println(messageList.size());
        }
        model.addAttribute("studyGroups", studyGroups);

        List<Role> roles = roleService.findAll();
        model.addAttribute("roles", roles);

        List<Book> books = bookService.getAllBooks();
        books.stream()
                .collect(Collectors.groupingBy(b -> b.getGenre()))
                .forEach((genre, bookList) -> {
                    System.out.print(genre + "  ->  ");
                    System.out.println(bookList.size());
                });
        model.addAttribute("books", books);


        return "admin/dashboard";
    }


    @GetMapping("/{userName}")
    public ResponseEntity<?> getDashboardData(@PathVariable String userName) {

        User user = userService.findByUsername(userName);
        if (user != null) {
            int totalMessageSend = messageService.getallmessagesByUserIdperweek(user.getId(), 1);
            List<Event> eventList = eventService.getAllEvents();
            int totalEvents = eventList.stream().filter(x -> x.getCreator().getUsername().equalsIgnoreCase(userName)).collect(Collectors.toList()).size();
            List<StudyGroup> studyGroupList = studyGroupService.findAllStudyGroup();
            int totalGroups = studyGroupService.getallindividualgroupasParticipant(user).size();

            List<Book> userOrderedBooks = new ArrayList<>();
            Set<String> genreList = new HashSet<String>();
            List<Book> allBooks = bookService.getAllBooks();

            String order = user.get_bookOrdering();
            String[] ordering = order.split(",");

            //top 20 books
            for (int i = 0; i < 50; i++) {
                Book book = allBooks.get(Integer.parseInt(ordering[i]) - 1);
                userOrderedBooks.add(book);
                genreList.add(book.getGenre());
            }

            DashboardDTO dto = new DashboardDTO(totalMessageSend, totalGroups, totalEvents, genreList.size());

            List<GroupParticipantRateDTO> participantRateList = new ArrayList<GroupParticipantRateDTO>();
            for (int i = 0; i < studyGroupList.size(); i++) {
                if (!studyGroupList.get(i).getIsPrivate()) {
                    String groupName = studyGroupList.get(i).getName();
                    int participantCount = studyGroupList.get(i).getParticipants().size();
                    GroupParticipantRateDTO participantRate = dto.new GroupParticipantRateDTO(groupName, participantCount);
                    participantRateList.add(participantRate);
                }
            }


            dto.setParticipantRate(participantRateList);

            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No user exist", HttpStatus.NOT_FOUND);
        }
    }

}
