package com.polling_app.polling_app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"username"}, name = "uk_users_username"
                )
        },
        indexes = {
                @Index(columnList = "display_name", name = "idx_users_display_name")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractBaseEntity {
    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    foreignKey = @ForeignKey(
                            name = "fk_users_roles_user_id",
                            foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE"
                    ),
                    nullable = false
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id",
                    foreignKey = @ForeignKey(
                            name = "fk_users_roles_role_id",
                            foreignKeyDefinition = "FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE"
                    ),
                    nullable = false
            ),
            uniqueConstraints = @UniqueConstraint(
                    columnNames = {"user_id", "role_id"},
                    name = "uk_users_roles_user_id_role_id"
            )
    )
    @Builder.Default
    private List<Role> roles = new ArrayList<>();

    @OneToMany( mappedBy = "createdBy", cascade = CascadeType.ALL,  orphanRemoval = true)
    private Set<Poll> polls;

}
