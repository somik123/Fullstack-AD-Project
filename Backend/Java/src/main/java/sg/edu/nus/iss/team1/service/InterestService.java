package sg.edu.nus.iss.team1.service;

import sg.edu.nus.iss.team1.domain.Interest;

import java.util.List;

public interface InterestService {
    List<Interest> getAllInterest();

    List<Interest> getInterestById(List<Integer> ids);
}
