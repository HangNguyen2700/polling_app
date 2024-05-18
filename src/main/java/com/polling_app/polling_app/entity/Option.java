package com.polling_app.polling_app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "options")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Option extends AbstractBaseEntity {

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "exist_in_id", nullable = false)
    private Poll existIn;

    @OneToMany(mappedBy = "votedIn", cascade = CascadeType.ALL,  orphanRemoval = true)
    private Set<Vote> votes;
}
