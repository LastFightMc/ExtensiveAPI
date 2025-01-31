package fr.skoupi.extensiveapi.databases.datasources;

/*  SQLDataSource
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import fr.skoupi.extensiveapi.databases.IDataSource;
import lombok.Getter;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class AHikariDataSource implements IDataSource {

    private final File configurationFile;
    private @Getter HikariDataSource hikariDataSource;

    public AHikariDataSource(final File configurationFile) {
        this.configurationFile = configurationFile;
    }

    /**
     * This function opens a data source using the HikariCP library.
     */
    @Override
    public void openDataSource() throws Exception {
        HikariConfig config = new HikariConfig(this.configurationFile.getPath());
        this.hikariDataSource = new HikariDataSource(config);
    }


    /**
     * Check if the data sources is open or not
     *
     * @return If it opened return true.
     */
    @Override
    public boolean dataSourceIsOpen() {
        boolean isConnected = hikariDataSource != null && hikariDataSource.isRunning();

        if (!isConnected) return false;

        try (Connection connection = hikariDataSource.getConnection()) {
            isConnected = connection != null;
        } catch (SQLException e) {
            return false;
        }
        return isConnected;
    }

    /**
     * Closing the data source
     */
    @Override
    public void closeDataSource() throws Exception {
        if (hikariDataSource != null && !hikariDataSource.isClosed())
            hikariDataSource.close();
    }

}
