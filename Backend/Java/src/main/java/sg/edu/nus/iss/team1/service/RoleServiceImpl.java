package sg.edu.nus.iss.team1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.team1.domain.Role;
import sg.edu.nus.iss.team1.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepo;

    @Override
    public Optional<Role> findById(Integer Id) {

        return roleRepo.findById(Id);
    }

    @Override
    public List<Role> findAll() {
        return roleRepo.findAll();
    }

}
