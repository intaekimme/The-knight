package com.a301.theknight.domain.player.api;

import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.member.repository.MemberRepository;
import com.a301.theknight.domain.player.dto.response.PlayerEntryResponse;
import com.a301.theknight.domain.player.dto.request.PlayerTeamRequest;
import com.a301.theknight.domain.player.dto.response.PlayerTeamResponse;
import com.a301.theknight.domain.player.entity.Player;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.domain.player.repository.PlayerRepository;
import com.a301.theknight.global.websocket.handler.StompFrameHandlerImpl;
import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlayerApiTest {

    @LocalServerPort
    private int port;
    private BlockingQueue<PlayerEntryResponse> entryPlayers;
    private BlockingQueue<Long> exitPlayers;
    private BlockingQueue<PlayerTeamResponse> teamPlayers;

//    private BlockingQueue<> messages;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PlayerRepository playerRepository;

    PlayerApiTest() {
    }


    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        entryPlayers = new LinkedBlockingDeque<>();
        exitPlayers = new LinkedBlockingDeque<>();
        teamPlayers = new LinkedBlockingDeque<>();
    }

    private WebSocketStompClient 웹_소켓_STOMP_CLIENT() {
        StandardWebSocketClient standardWebSocketClient = new StandardWebSocketClient();
        WebSocketTransport webSocketTransport = new WebSocketTransport(standardWebSocketClient);
        List<Transport> transports = Collections.singletonList(webSocketTransport);
        SockJsClient sockJsClient = new SockJsClient(transports);

        return new WebSocketStompClient(sockJsClient);
    }


    @Test
    @Transactional
    void entry() throws InterruptedException, ExecutionException, TimeoutException {
        Game game = gameRepository.findAll().get(9);
        Member member = memberRepository.findAll().get(94);

        // Settings
        WebSocketStompClient webSocketStompClient = 웹_소켓_STOMP_CLIENT();
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        // Connection
        ListenableFuture<StompSession> connect = webSocketStompClient
                .connect("ws://localhost:" + port + "/websocket", new StompSessionHandlerAdapter() {
                });
        StompSession stompSession = connect.get(120, TimeUnit.SECONDS);
        //  클라이언트가 구독
        stompSession.subscribe(String.format("/sub/games/%s/entry", game.getId()), new StompFrameHandlerImpl((new PlayerEntryResponse()), entryPlayers));
        //  클라이언트가 요청 발행
        stompSession.send(String.format("/pub/games/%s/entry", game.getId()), member.getId());

        PlayerEntryResponse playerEntryResponse = entryPlayers.poll(10, TimeUnit.SECONDS);

        //  Then
        Assertions.assertThat(playerEntryResponse.getMemberId()).isEqualTo(1L);
        Assertions.assertThat(playerEntryResponse.getNickname()).isEqualTo(member.getNickname());
    }

    @Test
    @Disabled
    @Transactional
    void exit() throws InterruptedException, ExecutionException, TimeoutException {
        Game game = gameRepository.findAll().get(0);
        Member member = memberRepository.findAll().get(0);
        Player player = playerRepository.findAll().get(0);

        // Settings
        WebSocketStompClient webSocketStompClient = 웹_소켓_STOMP_CLIENT();
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        // Connection
        ListenableFuture<StompSession> connect = webSocketStompClient
                .connect("ws://localhost:" + port + "/websocket", new StompSessionHandlerAdapter() {
                });
        StompSession stompSession = connect.get(60, TimeUnit.SECONDS);
        //  클라이언트가 구독
        stompSession.subscribe(String.format("/sub/games/%s/exit", game.getId()), new StompFrameHandlerImpl((new Object()), exitPlayers));
        //  클라이언트가 요청 발행
        stompSession.send(String.format("/pub/games/%s/exit", game.getId()), member.getId());

        long playerExitResponse = exitPlayers.poll(10, TimeUnit.SECONDS).longValue();

        //  Then
        Assertions.assertThat(player.getId()).isEqualTo(1L);
        Assertions.assertThat(playerRepository.findAll().size()).isEqualTo(9);
    }

    @Test
    @Transactional
    void team_websocket() throws InterruptedException, ExecutionException, TimeoutException {
        Game game = gameRepository.findAll().get(0);
        Member member = memberRepository.findAll().get(0);
        Player player = playerRepository.findAll().get(0);
        PlayerTeamRequest playerTeamRequest = new PlayerTeamRequest();
        playerTeamRequest.setTeam(Team.B);
        // Settings
        WebSocketStompClient webSocketStompClient = 웹_소켓_STOMP_CLIENT();
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        // Connection
        ListenableFuture<StompSession> connect = webSocketStompClient
                .connect("ws://localhost:" + port + "/websocket", new StompSessionHandlerAdapter() {
                });
        StompSession stompSession = connect.get(60, TimeUnit.SECONDS);
        //  클라이언트가 구독
        stompSession.subscribe(String.format("/sub/games/%s/team", game.getId()), new StompFrameHandlerImpl((new PlayerTeamResponse()), teamPlayers));
        //  클라이언트가 요청 발행
        stompSession.send(String.format("/pub/games/%s/team", game.getId()), playerTeamRequest);

        PlayerTeamResponse result = teamPlayers.poll(10, TimeUnit.SECONDS);

        Assertions.assertThat(result.getTeam()).isEqualTo("B");
    }

//
//    @Test
//    void ready() {
//    }
}