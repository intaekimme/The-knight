package com.a301.theknight.domain.player.api;


import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.member.repository.MemberRepository;
import com.a301.theknight.domain.player.dto.response.PlayerEntryResponse;
import com.a301.theknight.domain.player.repository.PlayerRepository;
import com.a301.theknight.global.websocket.handler.MessageFrameHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PlayerApiTest2 {

    protected StompSession stompSession;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PlayerRepository playerRepository;

    @LocalServerPort
    private int port;

    private final String url;

    private final WebSocketStompClient websocketClient;

    public PlayerApiTest2() {
        this.websocketClient = new WebSocketStompClient(new SockJsClient(createTransport()));
        this.websocketClient.setMessageConverter(new MappingJackson2MessageConverter());
        this.url = "ws://localhost:";
    }

    @BeforeEach
    public void connect() throws ExecutionException, InterruptedException, TimeoutException {
        this.stompSession = this.websocketClient
                .connect(url + port + "/websocket", new StompSessionHandlerAdapter() {})
                .get(3, TimeUnit.SECONDS);
    }

    @AfterEach
    public void disconnect() {
        if (this.stompSession.isConnected()) {
            this.stompSession.disconnect();
        }
    }

    private List<Transport> createTransport() {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }


    @Test
    void entry() throws ExecutionException, InterruptedException, TimeoutException {
        Game game = gameRepository.findAll().get(9);
        Member member = memberRepository.findAll().get(94);


        //GIVEN
        MessageFrameHandler<PlayerEntryResponse> handler = new MessageFrameHandler<>(PlayerEntryResponse.class);

        this.stompSession.subscribe(String.format("/sub/games/%s/entry", game.getId()), handler);

//        this.stompSession.send(String.format("/pub/games/%s/entry", game.getId()), )

    }

}
