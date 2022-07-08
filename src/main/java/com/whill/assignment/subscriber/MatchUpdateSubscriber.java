package com.whill.assignment.subscriber;

import com.whill.assignment.entity.Match;
import com.whill.assignment.publisher.Publisher;

import org.springframework.web.context.request.async.DeferredResult;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MatchUpdateSubscriber implements Subscriber<Match> {

    private final DeferredResult<Match> deferredResult;
    private final String id;

    @Override
    public void onUpdate(Publisher<Match> caller, Match context) {
        if (!updateApplicable(context)) {
            return;
        }
        doUpdate(caller, context);
    }

    private boolean updateApplicable(Match match) {
        return id.equals(match.getId());
    }

    private void doUpdate(Publisher<Match> caller, Match context) {
        deferredResult.setResult(context);
        caller.removeSubscriber(this);
    }
}
