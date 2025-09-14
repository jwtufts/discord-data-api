package discorddataapi.configuration;

import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
public class MongoConfig {

    // ----- Cakebot Data (default) -----
    @Primary
    @Bean(name = "cakebotDataMongoProperties")
    @ConfigurationProperties(prefix = "spring.data.mongodb")
    public MongoProperties cakebotDataMongoProperties() {
        return new MongoProperties();
    }

    @Primary
    @Bean(name = "cakebotDataMongoDatabaseFactory")
    public MongoDatabaseFactory cakebotDataMongoDatabaseFactory(
            @Qualifier("cakebotDataMongoProperties") MongoProperties props) {
        return new SimpleMongoClientDatabaseFactory(
                MongoClients.create(props.getUri()),
                props.getDatabase()
        );
    }

    @Primary
    @Bean(name = "cakebotDataMongoTemplate")
    public MongoTemplate cakebotDataMongoTemplate(
            @Qualifier("cakebotDataMongoDatabaseFactory") MongoDatabaseFactory factory) {
        return new MongoTemplate(factory);
    }

    @EnableMongoRepositories(
            basePackages = "discorddataapi.repositories",
            excludeFilters = @ComponentScan.Filter(
                    type = FilterType.REGEX,
                    pattern = "discorddataapi.repositories.leveling.*"
            ),
            mongoTemplateRef = "cakebotDataMongoTemplate"
    )
    static class CakebotDataMongoRepositories {
    }


    // ----- Leveling -----
    @Bean(name = "levelingMongoProperties")
    @ConfigurationProperties(prefix = "leveling.mongodb")
    public MongoProperties levelingMongoProperties() {
        return new MongoProperties();
    }

    @Bean(name = "levelingMongoDatabaseFactory")
    public MongoDatabaseFactory levelingMongoDatabaseFactory(
            @Qualifier("levelingMongoProperties") MongoProperties props) {
        return new SimpleMongoClientDatabaseFactory(
                MongoClients.create(props.getUri()),
                props.getDatabase()
        );
    }

    @Bean(name = "levelingMongoTemplate")
    public MongoTemplate levelingMongoTemplate(
            @Qualifier("levelingMongoDatabaseFactory") MongoDatabaseFactory factory) {
        return new MongoTemplate(factory);
    }

    @EnableMongoRepositories(
            basePackages = "discorddataapi.repositories.leveling",
            mongoTemplateRef = "levelingMongoTemplate"
    )
    static class LevelingMongoRepositories {
    }
}

