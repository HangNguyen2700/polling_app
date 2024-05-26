package com.polling_app.polling_app.service;

import com.polling_app.polling_app.entity.Option;
import com.polling_app.polling_app.entity.Vote;
import com.polling_app.polling_app.exception.NotFoundException;
import com.polling_app.polling_app.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VoteService {
    private final VoteRepository voteRepository;
    private final MessageSourceService messageSourceService;

    public Vote findById(UUID id) {
            return voteRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(messageSourceService.get("not_found_with_param",
                            new String[]{messageSourceService.get("vote")})));
    }
    public void delete(UUID id) {
        voteRepository.delete(findById(id));
    }

    public void deleteAll(Set<Vote> votes) {
        voteRepository.deleteAll(votes);
    }
}
