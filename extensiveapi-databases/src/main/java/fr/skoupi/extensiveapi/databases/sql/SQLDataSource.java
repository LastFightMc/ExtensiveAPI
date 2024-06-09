package fr.skoupi.extensiveapi.databases.sql;

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
import java.sql.SQLException;

public class SQLDataSource implements IDataSource {

	@Getter
	private static SQLDataSource instance;
	private final File configurationFile;
	@Getter
	private HikariDataSource hikariDataSource;

	public SQLDataSource (final File configurationFile)
	{
		this.configurationFile = configurationFile;
		instance = this;
	}

	/**
	 * This function opens a data source using the HikariCP library.
	 */
	@Override
	public void openDataSource ()
	{
		this.hikariDataSource = new HikariDataSource(new HikariConfig(configurationFile.getPath()));
	}


	@Override
	// It checks if the data source is open or not.
	public boolean dataSourceIsOpen ()
	{
		try
		{
			return this.hikariDataSource != null && !this.hikariDataSource.getConnection().isClosed();
		} catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	// It closes the data source.
	public void closeDataSource ()
	{
		if (this.dataSourceIsOpen()) this.hikariDataSource.close();
	}

}
