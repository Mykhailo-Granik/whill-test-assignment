package com.whill.assignment.subscriber;

import com.whill.assignment.publisher.Publisher;

public interface Subscriber<T extends Context> {

    void onUpdate(Publisher<T> caller, T context);

}
