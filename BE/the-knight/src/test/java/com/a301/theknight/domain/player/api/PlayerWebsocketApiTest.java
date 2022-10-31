package com.a301.theknight.domain.player.api;

import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.member.repository.MemberRepository;
import com.a301.theknight.domain.player.dto.PlayerEntryResponse;
import com.a301.theknight.domain.player.repository.PlayerRepository;
import com.a301.theknight.global.websocket.handler.StompFrameHandlerImpl;
import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlayerWebsocketApiTest {

    @LocalServerPort
    private int port;
    private BlockingQueue<PlayerEntryResponse> entryPlayers;
//    private BlockingQueue<> messages;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PlayerRepository playerRepository;

    PlayerWebsocketApiTest() {
    }


    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        entryPlayers = new LinkedBlockingDeque<>();
        멤버_생성();
        게임방_5vs5_생성();
    }

    private void 멤버_생성(){
        for(int i=1; i<=10; i++){
            Member member = Member.builder()
                    .email("player" + Integer.toString(i) + "@email.com")
                    .password("player" + Integer.toString(i))
                    .nickname("player" + Integer.toString(i))
                    .image("image" + Integer.toString(i))
                    .build();
            memberRepository.save(member);
        }
    }

    private void 게임방_5vs5_생성(){
        Game game = Game.builder()
                .title("game1")
                .capacity(10)
                .sword(4)
                .twin(2)
                .shield(3)
                .hand(1)
                .build();

        gameRepository.save(game);
    }

    private WebSocketStompClient 웹_소켓_STOMP_CLIENT() {
        StandardWebSocketClient standardWebSocketClient = new StandardWebSocketClient();
        WebSocketTransport webSocketTransport = new WebSocketTransport(standardWebSocketClient);
        List<Transport> transports = Collections.singletonList(webSocketTransport);
        SockJsClient sockJsClient = new SockJsClient(transports);

        return new WebSocketStompClient(sockJsClient);
    }

    @Disabled
    @Test
    void entry() throws InterruptedException, ExecutionException, TimeoutException {
        Game game = gameRepository.findAll().get(0);
        Member member = memberRepository.findAll().get(0);

        // Settings
        WebSocketStompClient webSocketStompClient = 웹_소켓_STOMP_CLIENT();
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        // Connection
        ListenableFuture<StompSession> connect = webSocketStompClient
                .connect("ws://localhost:" + port + "/websocket", new StompSessionHandlerAdapter() {
                });
        StompSession stompSession = connect.get(60, TimeUnit.SECONDS);
        //  클라이언트가 구독
        stompSession.subscribe(String.format("/sub/games/%s/entry", game.getId()), new StompFrameHandlerImpl((new PlayerEntryResponse()), entryPlayers));
        //  클라이언트가 요청 발행
        stompSession.send(String.format("/pub/games/%s/entry", game.getId()), member.getId());

        PlayerEntryResponse playerEntryResponse = entryPlayers.poll(10, TimeUnit.SECONDS);

        //  Then
        Assertions.assertThat(playerEntryResponse.getPlayerId()).isEqualTo(1L);
        Assertions.assertThat(playerEntryResponse.getNickname()).isEqualTo(member.getNickname());
    }

//    @Test
//    void exit() {
//    }
//
//    @Test
//    void team() {
//    }
//
//    @Test
//    void ready() {
//    }
}