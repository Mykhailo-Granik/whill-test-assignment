package com.whill.assignment.controller;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SubscribeToUpdatesExecutor {

    @Async
    public void subscribeAsync(Runnable subscribeCommand) {
        subscribeCommand.run();
    }
}
