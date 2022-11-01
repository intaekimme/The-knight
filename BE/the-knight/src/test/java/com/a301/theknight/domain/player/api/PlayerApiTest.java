package com.a301.theknight.domain.player.api;

import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.member.repository.MemberRepository;
import com.a301.theknight.domain.player.dto.*;
import com.a301.theknight.domain.player.entity.Player;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.domain.player.repository.PlayerRepository;
import com.a301.theknight.domain.player.service.PlayerWebsocketService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
@Disabled
@ExtendWith(MockitoExtension.class)
public class PlayerApiTest {
    @Mock
    MemberRepository memberRepository;

    @Mock
    GameRepository gameRepository;

    @Mock
    PlayerRepository playerRepository;

    PlayerWebsocketService playerWebsocketService;

    static Member[] testMembers;
    static Game testGame;

    static Player[] testPlayers;

    static PlayerTeamRequest playerTeamRequest;
    static PlayerReadyRequest playerReadyRequest;

    @BeforeAll
    static void beforeAll() {
        testMembers = new Member[11];

        for(int i=1; i<=10; i++){
            testMembers[i] = new Member(i,
                    "testPlayer" + Integer.toString(i) + "@email.com", "testPlayer" + Integer.toString(i),
                    "testPlayer" + Integer.toString(i),
                    "testPlayer" + Integer.toString(i),
                    "testImage" + Integer.toString(i));
        }

        testGame = new Game(1L, "testGame", 4,3,2,1,10 );

        testPlayers = new Player[11];
        for(int i = 1; i < testMembers.length-1; i++){
            testPlayers[i] = new Player(i, testMembers[i], testGame);
            if(i % 2 == 0){
                testPlayers[i].selectTeam(Team.B);
            }
            testPlayers[i].ready(true);
        }

        playerTeamRequest  = new PlayerTeamRequest();
        playerTeamRequest.setTeam("B");

        playerReadyRequest = new PlayerReadyRequest();
        playerReadyRequest.setReadyStatus(true);

    }

    @BeforeEach
    void setup() { playerWebsocketService = new PlayerWebsocketService(memberRepository, gameRepository, playerRepository); }

    @Test
    @Disabled
    @DisplayName("Player entry test")
    void PlayerEntryResponse() {
        given(memberRepository.findById(10L)).willReturn(Optional.of(testMembers[10]));
        given(gameRepository.findById(1L)).willReturn(Optional.of(testGame));

        PlayerEntryResponse playerEntryResponse = playerWebsocketService.entry(1L, 10L);

    }


    @Test
    @Disabled
    @DisplayName("Player exit test")
    void PlayerExitResponse_exitPlayerId() {
        given(memberRepository.findById(1L)).willReturn(Optional.of(testMembers[1]));
        given(gameRepository.findById(1L)).willReturn(Optional.of(testGame));
        given(playerRepository.findByGameAndMember(testGame, testMembers[1])).willReturn(Optional.of(testPlayers[1]));

        PlayerExitResponse exitPlayerId = playerWebsocketService.exit(1L, 1L);
        assertEquals(1L, exitPlayerId.getExitPlayerId());
    }

    @Test
    @DisplayName("Team change test")
    void playerTeamResponse() {
        given(memberRepository.findById(1L)).willReturn(Optional.of(testMembers[1]));
        given(gameRepository.findById(1L)).willReturn(Optional.of(testGame));
        given(playerRepository.findByGameAndMember(testGame, testMembers[1])).willReturn(Optional.of(testPlayers[1]));

        PlayerTeamResponse playerTeamResponse = playerWebsocketService.team(1L, 1L, playerTeamRequest);

        assertEquals(playerTeamRequest.getTeam(), playerTeamResponse.getTeam());
        assertEquals(1L, playerTeamResponse.getPlayerId());
    }

    @Test
    @DisplayName("Player ready test")
    void normalPlayerRequest() {
        given(memberRepository.findById(2L)).willReturn(Optional.of(testMembers[2]));
        given(gameRepository.findById(1L)).willReturn(Optional.of(testGame));
        given(playerRepository.findByGameAndMember(testGame, testMembers[2])).willReturn(Optional.of(testPlayers[2]));

        ReadyResponseDto readyResponseDto = playerWebsocketService.ready(1L, 2L, playerReadyRequest);
        PlayerReadyResponse playerReadyResponse = readyResponseDto.getPlayerReadyResponseList().getPlayerReadyResponseList().get(0);

        assertEquals(2L, playerReadyResponse.getPlayerId());
        assertTrue(playerReadyResponse.isReadyStatus());
        assertFalse(playerReadyResponse.isStartFlag());

    }

    @Test
    @DisplayName("Owner ready test")
    void ownerPlayerRequest() {
        testPlayers[10] = new Player(10L, testMembers[10], testGame);
        testPlayers[10].selectTeam(Team.B);
        testPlayers[10].ready(true);

        given(memberRepository.findById(1L)).willReturn(Optional.of(testMembers[1]));
        given(gameRepository.findById(1L)).willReturn(Optional.of(testGame));
        given(playerRepository.findByGameAndMember(testGame, testMembers[1])).willReturn(Optional.of(testPlayers[1]));

        ReadyResponseDto readyResponseDto = playerWebsocketService.ready(1L, 1L, playerReadyRequest);

        assertTrue(readyResponseDto.isOwner());
        assertEquals(GameStatus.PLAYING, testGame.getStatus());
        assertEquals(readyResponseDto.getSetGame(), testGame.getSetGame());
    }

}
