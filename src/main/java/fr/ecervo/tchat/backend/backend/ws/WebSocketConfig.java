package fr.ecervo.tchat.backend.backend.ws;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import fr.ecervo.tchat.backend.backend.exceptions.PortOutOfRangeException;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{

    @Value("${websocket.source.allowed.port}")
    private String allowedPort;

    @Value("${websocket.source.allowed.url}")
    private String allowedURL;

    @Value("${websocket.source.protocol}")
    private String protocol;

    private String origin;




    @PostConstruct
    @Order(1)
    void validPort() throws PortOutOfRangeException{
        int port = Integer.parseInt(this.allowedPort);
        if(port < 0 || port > 65535) throw new PortOutOfRangeException(this.allowedPort);
    }


    @PostConstruct
    void init(){
        this.origin = new String()
        .concat(this.protocol).concat("://")
        .concat(this.allowedURL).concat(":[")
        .concat(this.allowedPort).concat("]");       
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handler(), "/")
        .addInterceptors(new HttpSessionHandshakeInterceptor())
        .setAllowedOriginPatterns(this.origin);        
    }

    @Bean WebSocketHandler handler() {
        return new WebSocketHandler();
    }
    
}
