# Service that handles concurrent updates about football matches

## How to run the service

Simply execute

```shell
mvnw spring-boot:run
```

## Notes about implementation

### Endpoints

Four endpoints were implemented for this service.
They allow to perform the following actions:

- post an update about the match
- get the most relevant information about the match
- remove all the stored data from the storage
- subscribe to updates for a specific match

Swagger integration was implemented to view the relevant documentation and test the application.
Visit the following link to see it.

```
http://localhost:8080/swagger-ui/index.html#/
```

### Conflict resolution

The timestamp is sent together with every match update event to resolve the possible conflicts
between concurrent events related to the same match.
Information about the match will only be updated in the persistence storage if the timestamp
indicates that the newly received event is fresher than the persisted one.

### Subscribing to updates

For pushing the score updates to the client, the following flow is suggested:

- Client gets initial match information using regular "Get match info" endpoint
- Client subscribes to updates for this specific match. When an update to match happens or a
  pre-defined timeout is reached, response is sent to the client, and the client is supposed to
  re-subscribe for the updates.