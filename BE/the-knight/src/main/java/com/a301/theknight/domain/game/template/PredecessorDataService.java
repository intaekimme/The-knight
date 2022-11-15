package com.a301.theknight.domain.game.template;

import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.dto.prepare.GamePlayersInfoResponse;
import com.a301.theknight.domain.game.dto.prepare.PlayerDataDto;
import com.a301.theknight.domain.game.dto.prepare.response.GamePlayersInfoDto;
import com.a301.theknight.domain.game.dto.prepare.response.GamePreAttackResponse;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_IS_NOT_EXIST;

@Service
public class PredecessorDataService extends GameDataService {

    private final GameRedisRepository redisRepository;

    public PredecessorDataService(RedissonClient redissonClient, GameRedisRepository redisRepository) {
        super(redissonClient);
        this.redisRepository = redisRepository;
    }

    @Override
    public void makeAndSendData(long gameId, SendMessageService messageService) {
        InGame inGame = getInGame(gameId);
        Team preAttackTeam = inGame.getCurrentAttackTeam();
        GamePreAttackResponse response = new GamePreAttackResponse(preAttackTeam);
        messageService.sendData(gameId, "/pre-attack", response);

        GamePlayersInfoResponse playersInfo = getPlayersInfo(gameId);
        messageService.sendData(gameId, "/a/players", playersInfo.getPlayersAInfoDto());
        messageService.sendData(gameId, "/b/players", playersInfo.getPlayersBInfoDto());
    }

    public GamePlayersInfoResponse getPlayersInfo(long gameId) {
        GamePlayersInfoDto playersAInfo = getTeamPlayersInfo(gameId, Team.A);
        GamePlayersInfoDto playersBInfo = getTeamPlayersInfo(gameId, Team.B);

        return GamePlayersInfoResponse.builder()
                .playersAInfoDto(playersAInfo)
                .playersBInfoDto(playersBInfo)
                .build();
    }

    private GamePlayersInfoDto getTeamPlayersInfo(long gameId, Team team) {
        List<InGamePlayer> playerList = redisRepository.getInGamePlayerList(gameId);

        List<PlayerDataDto> players = playerList.stream()
                .map(inGamePlayer -> PlayerDataDto.toDto(inGamePlayer, team))
                .collect(Collectors.toList());

        return GamePlayersInfoDto.builder()
                .maxMember(players.size())
                .players(players).build();
    }

    private InGame getInGame(long gameId) {
        return redisRepository.getInGame(gameId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_IS_NOT_EXIST));
    }
}
