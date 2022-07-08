package com.whill.assignment.repository;

public class PublishingMatchRepositoryFactory {

    public PublishingMatchRepository create() {
        return PublishingMatchRepositoryImpl.getInstance(
                InMemoryMatchRepository.getInstance()
        );
    }

}
