package com.polling_app.polling_app.entity;

import com.polling_app.polling_app.util.Constants;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"name"}, name = "uk_roles_name")},
        indexes = {@Index(columnList = "name", name = "idx_roles_name")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role extends AbstractBaseEntity {
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinTable(
            name="users_roles",
            joinColumns = @JoinColumn(
                    name = "role_id",
                    foreignKey = @ForeignKey(
                            name = "fk_users_roles_role_id",
                            foreignKeyDefinition = "FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE"
                    ),
                    nullable = false
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id",
                    foreignKey = @ForeignKey(
                            name = "fk_users_roles_user_id",
                            foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE"
                    ),
                    nullable = false
            ),
            uniqueConstraints = {
                    @UniqueConstraint(
                            columnNames = {"user_id", "role_id"},
                            name = "uk_users_roles_user_id_role_id"
                    )
            }
    )
    @Builder.Default
    private Set<User> users = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "name", length = 16)
    @NaturalId
    private Constants.RoleEnum name;

    public Role (final Constants.RoleEnum name) {
        this.name = name;
    }
}
