package com.polling_app.polling_app.repository;

import com.polling_app.polling_app.entity.Poll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PollRepository extends JpaRepository<Poll, UUID>, JpaSpecificationExecutor<Poll> {

    @Query("select p from Poll p order by p.createdAt")
    List<Poll> findByOrderByCreatedAtAsc(Pageable pageable);

    @Query("select p from Poll p where p.question like concat('%', ?1, '%') order by p.createdAt DESC")
    List<Poll> findByQuestionContainsOrderByCreatedAtDesc(String question, Pageable pageable);



}
