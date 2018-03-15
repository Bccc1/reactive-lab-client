package de.virtual7.reactivelabclient.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;

import java.util.Collections;
import java.util.List;

/**
 * Created by mihai.dobrescu
 */
@Configuration
public class CassandraConfiguration extends AbstractCassandraConfiguration {

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        final CreateKeyspaceSpecification specification =
                CreateKeyspaceSpecification
                        .createKeyspace("reactive_lab")
                        .ifNotExists()
                        .withSimpleReplication(1)
                        .with(KeyspaceOption.DURABLE_WRITES);

        return Collections.singletonList(specification);
    }

    @Override
    protected String getKeyspaceName() {
        return "reactive_lab";
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[] {"de.virtual7.reactivelabclient.event"};
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }
}
