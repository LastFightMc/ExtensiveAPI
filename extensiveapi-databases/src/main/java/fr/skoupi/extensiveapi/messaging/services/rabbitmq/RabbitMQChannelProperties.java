package fr.skoupi.extensiveapi.messaging.services.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import lombok.*;

import java.util.HashMap;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder(setterPrefix = "set")
public class RabbitMQChannelProperties {

    private String queueName;
    private String exchangeName;

    private BuiltinExchangeType exchangeType;

    private String routingKey;
    private boolean durable;
    private boolean exclusive;
    private boolean autoDelete;
    private HashMap<String, Object> arguments;

}
