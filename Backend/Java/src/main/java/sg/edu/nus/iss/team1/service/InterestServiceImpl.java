package sg.edu.nus.iss.team1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.edu.nus.iss.team1.domain.Interest;
import sg.edu.nus.iss.team1.repository.InterestRepository;

import java.util.List;

@Service
public class InterestServiceImpl implements InterestService {

    @Autowired
    private InterestRepository interestRepository;

    @Override
    public List<Interest> getAllInterest() {
        return interestRepository.findAll();
    }

    @Override
    public List<Interest> getInterestById(List<Integer> ids) {
        return interestRepository.findAllById(ids);
    }
}
