package com.whill.assignment.repository;

import com.whill.assignment.entity.Match;
import com.whill.assignment.publisher.Publisher;

public interface PublishingMatchRepository extends MatchRepository, Publisher<Match> {
}
