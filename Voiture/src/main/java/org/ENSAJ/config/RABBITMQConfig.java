package org.ENSAJ.config;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RABBITMQConfig {
    @Bean
    public Queue queue(){
        return  new Queue("microservice_queue");
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange("microservice_exchange");
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(exchange()).with("microservice_key");
    }

}
