package com.whill.assignment.publisher;

import com.whill.assignment.subscriber.Context;
import com.whill.assignment.subscriber.Subscriber;

public interface Publisher<T extends Context> {

    void addSubscriber(Subscriber<T> subscriber);

    void removeSubscriber(Subscriber<T> subscriber);

}
