package com.a301.theknight.domain.player.api;

import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.game.util.GameConvertUtil;
import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.member.repository.MemberRepository;
import com.a301.theknight.domain.player.dto.ReadyDto;
import com.a301.theknight.domain.player.dto.request.PlayerReadyRequest;
import com.a301.theknight.domain.player.dto.request.PlayerTeamRequest;
import com.a301.theknight.domain.player.dto.response.PlayerEntryResponse;
import com.a301.theknight.domain.player.dto.response.PlayerExitDto;
import com.a301.theknight.domain.player.dto.response.PlayerTeamResponse;
import com.a301.theknight.domain.player.entity.Player;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.domain.player.repository.PlayerRepository;
import com.a301.theknight.domain.player.service.PlayerService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
@Disabled
@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {
    @Mock
    MemberRepository memberRepository;

    @Mock
    GameRepository gameRepository;

    @Mock
    PlayerRepository playerRepository;

    @Mock
    GameRedisRepository redisRepository;

    @Mock
    GameConvertUtil gameConvertUtil;

    PlayerService playerService;

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
                    "testPlayer" + i + "@email.com", "testPlayer" + i,
                    "testPlayer" + i,
                    "testPlayer" + i,
                    "testImage" + i);
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
        playerTeamRequest.setTeam(Team.B);

        playerReadyRequest = new PlayerReadyRequest();
        playerReadyRequest.setReadyStatus(true);

    }

    @BeforeEach
    void setup() { playerService = new PlayerService(memberRepository, gameRepository, playerRepository, redisRepository, gameConvertUtil); }

    @Test
    @Disabled
    @DisplayName("Player entry test")
    void PlayerEntryResponse() {
        given(memberRepository.findById(10L)).willReturn(Optional.of(testMembers[10]));
        given(gameRepository.findById(1L)).willReturn(Optional.of(testGame));

        PlayerEntryResponse playerEntryResponse = playerService.entry(1L, 10L);

    }


    @Test
    @Disabled
    @DisplayName("Player exit test")
    void PlayerExitResponse_exitPlayerId() {
        given(memberRepository.findById(1L)).willReturn(Optional.of(testMembers[1]));
        given(gameRepository.findById(1L)).willReturn(Optional.of(testGame));
        given(playerRepository.findByGameAndMember(testGame, testMembers[1])).willReturn(Optional.of(testPlayers[1]));

        PlayerExitDto exitPlayerId = playerService.exit(1L, 1L);
        assertEquals(1L, exitPlayerId.getPlayerExitResponse().getMemberId());
    }

    @Test
    @DisplayName("Team change test")
    void playerTeamResponse() {
        given(memberRepository.findById(1L)).willReturn(Optional.of(testMembers[1]));
        given(gameRepository.findById(1L)).willReturn(Optional.of(testGame));
        given(playerRepository.findByGameAndMember(testGame, testMembers[1])).willReturn(Optional.of(testPlayers[1]));

        PlayerTeamResponse playerTeamResponse = playerService.team(1L, 1L, playerTeamRequest);

        assertEquals(playerTeamRequest.getTeam().name(), playerTeamResponse.getTeam());
        assertEquals(1L, playerTeamResponse.getMemberId());
    }

    @Test
    @DisplayName("Player ready test")
    void normalPlayerRequest() {
        given(memberRepository.findById(2L)).willReturn(Optional.of(testMembers[2]));
        given(gameRepository.findById(1L)).willReturn(Optional.of(testGame));
        given(playerRepository.findByGameAndMember(testGame, testMembers[2])).willReturn(Optional.of(testPlayers[2]));

        ReadyDto readyDto = playerService.ready(1L, 2L, playerReadyRequest);

//        assertEquals(2L, readyDto.getReadyResponseDto().getMemberId());
//        assertTrue(readyDto.getReadyResponseDto().isReadyStatus());
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

        ReadyDto readyDto = playerService.ready(1L, 1L, playerReadyRequest);

        assertEquals(GameStatus.PLAYING, testGame.getStatus());
    }

}
