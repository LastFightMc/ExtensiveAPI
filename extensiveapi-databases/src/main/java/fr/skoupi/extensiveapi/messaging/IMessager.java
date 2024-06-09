package fr.skoupi.extensiveapi.messaging;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface IMessager extends AutoCloseable {

    void initialize(String host, String virtualHost, String username, String password);

    void openConnection() throws IOException, TimeoutException;

    @Override
    void close() throws Exception;
}
