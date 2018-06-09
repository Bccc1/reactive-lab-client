package de.virtual7.reactivelabclient.service;

import de.virtual7.reactivelabclient.event.TrackingEvent;
import de.virtual7.reactivelabclient.repository.TrackingEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.InsertOptions;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

/**
 * Created by mihai.dobrescu
 */
@Service
public class TrackingEventClientService {

    @Autowired
    private TrackingEventRepository repository;

    @Autowired
    private CassandraOperations cassandraOperations;

    public void saveEvent(TrackingEvent event){
        this.repository.save(event);
    }

    public void saveEventTemp(TrackingEvent event){
        this.cassandraOperations.insert(event, InsertOptions.builder().ttl(Duration.ofMillis(1000)).build());
    }

    public void saveBulkEvents(List<TrackingEvent> events){
        this.repository.saveAll(events);
    }
}
