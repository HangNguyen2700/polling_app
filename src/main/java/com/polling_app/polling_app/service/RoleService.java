package com.polling_app.polling_app.service;

import com.polling_app.polling_app.entity.Role;
import com.polling_app.polling_app.exception.NotFoundException;
import com.polling_app.polling_app.repository.RoleRepository;
import com.polling_app.polling_app.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    private final MessageSourceService messageSourceService;

    /**
     * Count roles.
     *
     * @return Long
     */
    public long count() {
        return roleRepository.count();
    }

    /**
     * Find by role name.
     *
     * @param name Constants.RoleEnum
     * @return Role
     */
    public Role findByName(final Constants.RoleEnum name) {
        return roleRepository.findByName(name)
            .orElseThrow(() -> new NotFoundException(messageSourceService.get("role_not_found")));
    }

    /**
     * Create role.
     *
     * @param role Role
     * @return Role
     */
    public Role create(final Role role) {
        return roleRepository.save(role);
    }

    /**
     * Save list roles.
     *
     * @param roleList List
     * @return List
     */
    public List<Role> saveList(List<Role> roleList) {
        return roleRepository.saveAll(roleList);
    }
}
