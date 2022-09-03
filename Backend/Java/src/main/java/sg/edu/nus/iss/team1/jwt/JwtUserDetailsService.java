package sg.edu.nus.iss.team1.jwt;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.team1.domain.User;
import sg.edu.nus.iss.team1.repository.UserRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    /*
     * @Autowired private User userDao;
     */

    @Autowired
    UserRepository userRepo;

    /*
     * @Autowired private PasswordEncoder bcryptEncoder;
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                new ArrayList<>());
    }


}
