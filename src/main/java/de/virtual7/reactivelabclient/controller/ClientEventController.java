package de.virtual7.reactivelabclient.controller;

import de.virtual7.reactivelabclient.event.TrackingEvent;
import de.virtual7.reactivelabclient.service.TrackingEventClientService;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mihai.dobrescu
 */
@RestController
@RequestMapping("/client")
public class ClientEventController {

    Logger logger = LoggerFactory.getLogger(ClientEventController.class);

    @Autowired
    private TrackingEventClientService trackingEventClientService;

    @GetMapping(value = "/events")
    public String getTrackingEvents() {
        Flux<TrackingEvent> trackingEventFlux = WebClient.create("http://localhost:8080")
                .get()
                .uri("events/latest")
                .retrieve()
                .bodyToFlux(TrackingEvent.class);

        trackingEventFlux.log().subscribe(new BaseSubscriber<TrackingEvent>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                request(1);
            }

            @Override
            protected void hookOnNext(TrackingEvent value) {
                System.out.println(value);
                trackingEventClientService.saveEvent(value);
                request(1);
            }
        });
        return "OK";
    }

    @GetMapping("/groupedEvents")
    public String getTrackingEventsGrouped() {

        Flux<TrackingEvent> trackingEventFlux = WebClient.create("http://localhost:8080")
                .get()
                .uri("events/latest")
                .retrieve()
                .bodyToFlux(TrackingEvent.class);

        trackingEventFlux.log().subscribe(new BaseSubscriber<TrackingEvent>() {

            List<TrackingEvent> buffer = new ArrayList<>();

            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                request(10);
            }

            @Override
            protected void hookOnNext(TrackingEvent value) {
                System.out.println(value);
                buffer.add(value);
                if(buffer.size() >= 10){
                    trackingEventClientService.saveBulkEvents(buffer);
                    buffer.clear();
                }
                request(10);
            }
        });

        return "OK"; //TODO: return something meaningful :)
    }


}
