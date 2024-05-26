package com.polling_app.polling_app.service;

import com.polling_app.polling_app.entity.Option;
import com.polling_app.polling_app.entity.Poll;
import com.polling_app.polling_app.exception.NotFoundException;
import com.polling_app.polling_app.repository.OptionRepository;
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
public class OptionService {
    private final VoteRepository voteRepository;
    private final OptionRepository optionRepository;
    private final MessageSourceService messageSourceService;
    private final VoteService voteService;

    public Option findById(UUID id) {
        return optionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageSourceService.get("not_found_with_param",
                        new String[]{messageSourceService.get("option")})));
    }

    public void delete(UUID id){
        Option option = findById(id);
        voteService.deleteAll(option.getVotes());
        optionRepository.delete(option);
    }
}
