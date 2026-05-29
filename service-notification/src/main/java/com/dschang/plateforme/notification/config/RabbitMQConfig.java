package com.dschang.plateforme.notification.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Exchange et queues selon les specs
    public static final String EXCHANGE_PUBLICATIONS = "publications.exchange";
    public static final String EXCHANGE_MESSAGES = "messages.exchange";

    public static final String QUEUE_NOTIFICATION_MESSAGE = "notification.message.queue";
    public static final String QUEUE_NOTIFICATION_PUBLICATION = "notification.publication.queue";
    public static final String QUEUE_NOTIFICATION_COMPTE = "notification.compte.queue";

    public static final String ROUTING_KEY_MESSAGE = "message.nouveau";
    public static final String ROUTING_KEY_PUBLICATION_CREE = "publication.cree";
    public static final String ROUTING_KEY_COMPTE_VALIDE = "compte.valide";

    // Queues
    @Bean
    public Queue notificationMessageQueue() {
        return new Queue(QUEUE_NOTIFICATION_MESSAGE, true);
    }

    @Bean
    public Queue notificationPublicationQueue() {
        return new Queue(QUEUE_NOTIFICATION_PUBLICATION, true);
    }

    @Bean
    public Queue notificationCompteQueue() {
        return new Queue(QUEUE_NOTIFICATION_COMPTE, true);
    }

    // Exchanges
    @Bean
    public DirectExchange publicationsExchange() {
        return new DirectExchange(EXCHANGE_PUBLICATIONS);
    }

    @Bean
    public DirectExchange messagesExchange() {
        return new DirectExchange(EXCHANGE_MESSAGES);
    }

    // Bindings
    @Bean
    public Binding bindingMessage(Queue notificationMessageQueue, DirectExchange messagesExchange) {
        return BindingBuilder.bind(notificationMessageQueue).to(messagesExchange).with(ROUTING_KEY_MESSAGE);
    }

    @Bean
    public Binding bindingPublication(Queue notificationPublicationQueue, DirectExchange publicationsExchange) {
        return BindingBuilder.bind(notificationPublicationQueue).to(publicationsExchange).with(ROUTING_KEY_PUBLICATION_CREE);
    }

    // Pour le compte validé, on suppose un exchange "comptes.exchange" qui sera créé par le service Compte
    // Ici on peut lier à un exchange qui n'existe pas encore (RabbitMQ le créera s'il est déclaré).
    @Bean
    public DirectExchange comptesExchange() {
        return new DirectExchange("comptes.exchange");
    }

    @Bean
    public Binding bindingCompte(Queue notificationCompteQueue, DirectExchange comptesExchange) {
        return BindingBuilder.bind(notificationCompteQueue).to(comptesExchange).with(ROUTING_KEY_COMPTE_VALIDE);
    }

    // Convertisseur JSON
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // Fabrique de conteneur avec convertisseur JSON
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        return factory;
    }
}