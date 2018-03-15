package de.virtual7.reactivelabclient.repository;


import de.virtual7.reactivelabclient.event.TrackingEvent;
import org.springframework.data.cassandra.repository.CassandraRepository;

/**
 * Created by mihai.dobrescu
 */
public interface TrackingEventRepository extends CassandraRepository<TrackingEvent, Long> {

}
