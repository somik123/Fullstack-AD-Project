package sg.edu.nus.iss.team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.iss.team1.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    public Role findByName(String rolename);
}
