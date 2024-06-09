package fr.skoupi.extensiveapi.databases.mongodb;

/*  MongoDataSource
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import fr.skoupi.extensiveapi.databases.IDataSource;
import lombok.Getter;
import org.bson.UuidRepresentation;


@Getter
public class MongoDataSource implements IDataSource {

    @Getter
    private static MongoDataSource instance;
    private MongoClient mongoClient;
    private final String mongoHostname;


    public MongoDataSource(String mongoHostname) {
        instance = this;
        this.mongoHostname = mongoHostname;
    }

    /**
     * Create a MongoClientSettings object, set the application name, set the connection pool settings, and create a
     * MongoClient object.
     */
    @Override
    public void openDataSource() {
        mongoClient = MongoClients.create(MongoClientSettings.builder().uuidRepresentation(UuidRepresentation.JAVA_LEGACY).applyConnectionString(new ConnectionString(mongoHostname)).build());
    }

    /**
     * > If the mongoClient is not null, then the data source is open
     *
     * @return A boolean value.
     */
    @Override
    public boolean dataSourceIsOpen() {
        return mongoClient != null;
    }

    /**
     * Closes the connection to the MongoDB database.
     */
    @Override
    public void closeDataSource() {
        mongoClient.close();
    }

}
