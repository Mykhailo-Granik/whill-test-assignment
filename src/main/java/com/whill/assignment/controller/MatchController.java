package com.whill.assignment.controller;

import com.whill.assignment.entity.Match;
import com.whill.assignment.repository.PublishingMatchRepository;
import com.whill.assignment.repository.PublishingMatchRepositoryFactory;
import com.whill.assignment.subscriber.MatchUpdateSubscriber;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MatchController {

    private final SubscribeToUpdatesExecutor subscribeToUpdatesExecutor;

    @PutMapping("/upsertMatch")
    public void upsertMatch(@RequestBody Match match) {
        log.info(
                "Upserting match info for match with id {} and timestamp {}",
                match.getId(), match.getUpdateTimeStamp()
        );
        match.upsert(matchRepository());
    }

    private PublishingMatchRepository matchRepository() {
        return new PublishingMatchRepositoryFactory().create();
    }

    @GetMapping("/match/{id}")
    public Match getMatch(@PathVariable String id) {
        log.info("Getting match info for match with id {}", id);
        Match match = new Match(id).find(matchRepository());
        if (match == null) {
            log.warn("Match with id {} not found", id);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, String.format("Match with id %s not found", id)
            );
        }
        return match;
    }

    @GetMapping("/match/{id}/updates")
    public DeferredResult<Match> subscribeToUpdates(@PathVariable String id,
                                                    @RequestParam("timeout") long timeout) {
        log.info("Subscribing for updates for match with id {}", id);
        DeferredResult<Match> result = new DeferredResult<>(timeout);
        result.onTimeout(() -> result.setErrorResult(
                        new ResponseStatusException(HttpStatus.NOT_MODIFIED)
                )
        );
        subscribeToUpdatesExecutor.subscribeAsync(() -> matchRepository().addSubscriber(new MatchUpdateSubscriber(result, id)));
        return result;
    }

    @DeleteMapping("/matches")
    public void clearMatchesInfo() {
        log.info("Clearing all matches information from storage");
        matchRepository().clear();
    }

}
