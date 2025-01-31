package fr.skoupi.extensiveapi.databases.datasources;

/*  RedisDataSource
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import fr.skoupi.extensiveapi.databases.IDataSource;
import lombok.Getter;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

import java.io.File;
import java.io.IOException;

public abstract class ARedissonDataSource implements IDataSource {

    private final File configurationFile;
    private final boolean setCodec;

    private @Getter RedissonClient redissonClient;

    public ARedissonDataSource(File configurationFile, boolean setCodec) {
        this.configurationFile = configurationFile;
        this.setCodec = setCodec;
    }

    public ARedissonDataSource(File configurationFile) {
        this(configurationFile, false);
    }

    /**
     * > This function opens a connection to the Redis server using the configuration file specified in the
     * `redissonConfigurationFile` variable
     */
    @Override
    public void openDataSource() throws IOException {
        final Config config = Config.fromYAML(configurationFile);
        if (setCodec) config.setCodec(new JsonJacksonCodec());
        redissonClient = Redisson.create(config);
    }

    /**
     * > If the Redisson client is not shutting down, is not shutdown, and is not null, then the data source is open
     *
     * @return A boolean value.
     */
    @Override
    public boolean dataSourceIsOpen() {
        return !redissonClient.isShuttingDown() && !redissonClient.isShutdown() && redissonClient != null;
    }

    /**
     * > Close the Redisson data source
     */
    @Override
    public void closeDataSource() {
        if (dataSourceIsOpen()) redissonClient.shutdown();
    }


}
