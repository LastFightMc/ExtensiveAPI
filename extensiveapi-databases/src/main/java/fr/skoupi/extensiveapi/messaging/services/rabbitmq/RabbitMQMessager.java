package fr.skoupi.extensiveapi.messaging.services.rabbitmq;

import com.rabbitmq.client.*;
import fr.skoupi.extensiveapi.messaging.IMessager;
import lombok.Getter;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Getter
public class RabbitMQMessager implements IMessager {

    private static final int DEFAULT_PORT = 5672;
    private final RabbitMQChannelProperties channelProperties;
    private ConnectionFactory connectionFactory;

    private Connection connection;
    private Channel channel;

    public RabbitMQMessager(RabbitMQChannelProperties channelProperties) {
        this.channelProperties = channelProperties;
    }


    @Override
    public void initialize(String host, String virtualHost, String username, String password) {
        this.connectionFactory = new ConnectionFactory();
        this.connectionFactory.setHost(host);
        this.connectionFactory.setPort(host.contains(":") ? Integer.parseInt(host.split(":")[1]) : DEFAULT_PORT);
        this.connectionFactory.setVirtualHost(virtualHost);
        this.connectionFactory.setUsername(username);
        this.connectionFactory.setPassword(password);
        this.connectionFactory.setAutomaticRecoveryEnabled(true);
    }

    @Override
    public void openConnection() throws IOException, TimeoutException {
        connection = connectionFactory.newConnection();
        channel = connection.createChannel();


        /*
        RabbitMQChannelProperties properties = channelProperties;

        this.channel.queueDeclare(properties.getQueueName(), properties.isDurable(), properties.isExclusive(), properties.isAutoDelete(), properties.getArguments());
        this.channel.exchangeDeclare(properties.getExchangeName(), properties.getExchangeType(), properties.isDurable(), properties.isExclusive(), properties.isAutoDelete(), properties.getArguments());

        this.channel.queueBind(properties.getQueueName(), properties.getExchangeName(), properties.getRoutingKey());


        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Consumed: " + message);
            }
        };

        channel.basicConsume(channelProperties.getQueueName(), false, consumer);
         */
    }

   /*
    @Override
    public void sendMessage(IOutgoingMessage message) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF(message.asJson());
        try {
            this.channel.basicPublish(channelProperties.getExchangeName(), channelProperties.getRoutingKey(), null, output.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    */


    protected boolean checkAndReopenConnection(RabbitMQMessager messager) {
        boolean connectionAlive = this.connection != null && this.connection.isOpen();
        boolean channelAlive = this.channel != null && this.channel.isOpen();

        if (connectionAlive && channelAlive) return true;

        // cleanup existing
        if (this.channel != null && this.channel.isOpen())
            try { this.channel.close(); } catch (Exception ignored) { }
        if (this.connection != null && this.connection.isOpen())
            try { this.connection.close(); } catch (Exception ignored) { }

        // reopen
        try {
            messager.openConnection();
            return true;
        } catch (IOException | TimeoutException ignored) {
            return false;
        }

    }

    @Override
    public void close() throws Exception {
        this.channel.close();
        this.connection.close();
    }


   /* public class MqSubscriber  extends DefaultConsumer {

        public MqSubscriber(Channel channel) {
            super(channel);
        }


        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
            ByteArrayDataInput input = ByteStreams.newDataInput(body);
            String json = input.readUTF();
            System.out.println("Received: " + json);
        }
    }
    */
}
