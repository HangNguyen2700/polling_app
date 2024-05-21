package com.polling_app.polling_app.service;

import com.polling_app.polling_app.entity.Poll;
import com.polling_app.polling_app.exception.NotFoundException;
import com.polling_app.polling_app.repository.PollRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.polling_app.polling_app.exception.NotFoundException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PollService {
    private final PollRepository pollRepository;
    private final MessageSourceService messageSourceService;

    /**
     * Get authentication.
     *
     * @return Authentication
     */
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public List<Poll> getAllPolls (int limit, int offset) {
        Authentication authentication = getAuthentication();
        PageRequest pageRequest = PageRequest.of(offset/limit, limit);

        if(authentication.isAuthenticated()) {
                List<Poll> polls = pollRepository.findByOrderByCreatedAtAsc(pageRequest);
                if(polls.isEmpty()) log.info("do not contain any poll");
                return polls;
        } else {
            log.warn("[JWT] User not authenticated!");
            throw new BadCredentialsException(messageSourceService.get("bad_credentials"));
        }
    }

    public List<Poll> getPollsByKeyword (String keyword, int limit, int offset) {
        Authentication authentication = getAuthentication();
        PageRequest pageRequest = PageRequest.of(offset/limit, limit);

        if(authentication.isAuthenticated()){
            List<Poll> polls = pollRepository.findByQuestionContainsOrderByCreatedAtDesc(keyword, pageRequest);
            if(polls.isEmpty()) log.info("do not contain any poll");
            return polls;
        } else {
            log.warn("[JWT] User not authenticated!");
            throw new BadCredentialsException(messageSourceService.get("bad_credentials"));
        }
    }

    public Poll findById(String id) {
        return pollRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NotFoundException(messageSourceService.get("not_found_with_param",
                        new String[]{messageSourceService.get("poll")})));
    }

}
