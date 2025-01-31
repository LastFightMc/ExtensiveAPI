package fr.skoupi.extensiveapi.databases;

/*
 *  * @Created on 2021 - 13:15
 *  * @Project UtilsAPI
 *  * @Author Jimmy
 */


public interface IDataSource {
    /**
     * This function opens a data source and throws an Exception if it fails.
     */
    void openDataSource() throws Exception;

    /**
     * > Returns true if the data source is open, false otherwise
     *
     * @return A boolean value.
     */
    boolean dataSourceIsOpen() throws Exception;

    /**
     * Closes the data source
     */
    void closeDataSource() throws Exception;
}
