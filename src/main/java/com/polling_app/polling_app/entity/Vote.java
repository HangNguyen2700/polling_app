package com.polling_app.polling_app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "votes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vote extends AbstractBaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voted_in_id", nullable = false)
    private Option votedIn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voted_by_id", nullable = false)
    private User votedBy;
}
