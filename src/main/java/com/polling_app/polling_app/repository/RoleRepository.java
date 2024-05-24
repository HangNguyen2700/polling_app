package com.polling_app.polling_app.repository;

import com.polling_app.polling_app.entity.Role;
import com.polling_app.polling_app.util.Constants;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(Constants.RoleEnum name);
}
