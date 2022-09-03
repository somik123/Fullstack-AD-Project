package sg.edu.nus.iss.team1.service;

import java.util.List;
import java.util.Optional;

import sg.edu.nus.iss.team1.domain.Role;

public interface RoleService {
    public Optional<Role> findById(Integer Id);

    public List<Role> findAll();
}
