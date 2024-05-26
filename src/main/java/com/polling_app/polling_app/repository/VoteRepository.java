package com.polling_app.polling_app.repository;

import com.polling_app.polling_app.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface VoteRepository extends JpaRepository<Vote, UUID>, JpaSpecificationExecutor<Vote> {
}
