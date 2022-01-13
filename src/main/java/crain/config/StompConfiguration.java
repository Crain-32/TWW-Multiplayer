package crain.config;


import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.event.inbound.ApplicationEventListeningMessageProducer;
import org.springframework.integration.stomp.ReactorNettyTcpStompSessionManager;
import org.springframework.integration.stomp.StompSessionManager;
import org.springframework.integration.stomp.event.StompIntegrationEvent;
import org.springframework.integration.stomp.inbound.StompInboundChannelAdapter;
import org.springframework.integration.stomp.outbound.StompMessageHandler;
import org.springframework.integration.support.converter.PassThruMessageConverter;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.simp.stomp.ReactorNettyTcpStompClient;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

//https://docs.spring.io/spring-framework/docs/5.3.2/reference/html/web.html#websocket-stomp-handle-broker-relay-configure
//https://www.baeldung.com/spring-websockets-send-message-to-user
@Configuration
@EnableIntegration
public class StompConfiguration {

    @Bean
    public ReactorNettyTcpStompClient stompClient() {
        ReactorNettyTcpStompClient stompClient = new ReactorNettyTcpStompClient("127.0.0.1", 61613);
        stompClient.setMessageConverter(new PassThruMessageConverter());
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.afterPropertiesSet();
        stompClient.setTaskScheduler(taskScheduler);
        stompClient.setReceiptTimeLimit(5000);
        return stompClient;
    }

    @Bean
    public StompSessionManager stompSessionManager() {
        ReactorNettyTcpStompSessionManager stompSessionManager = new ReactorNettyTcpStompSessionManager(stompClient());
        stompSessionManager.setAutoReceipt(true);
        return stompSessionManager;
    }

    @Bean
    public PollableChannel stompInputChannel() {
        return new QueueChannel();
    }

    @Bean
    public StompInboundChannelAdapter stompInboundChannelAdapter() {
        StompInboundChannelAdapter adapter =
                new StompInboundChannelAdapter(stompSessionManager(), "/topic/myTopic");
        adapter.setOutputChannel(stompInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "stompOutputChannel")
    public MessageHandler stompMessageHandler() {
        StompMessageHandler handler = new StompMessageHandler(stompSessionManager());
        handler.setDestination("/topic/myTopic");
        return handler;
    }

    @Bean
    public PollableChannel stompEvents() {
        return new QueueChannel();
    }

    @Bean
    public ApplicationListener<ApplicationEvent> stompEventListener() {
        ApplicationEventListeningMessageProducer producer = new ApplicationEventListeningMessageProducer();
        producer.setEventTypes(StompIntegrationEvent.class);
        producer.setOutputChannel(stompEvents());
        return producer;
    }
}
