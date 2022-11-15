package com.a301.theknight.domain.game.template;

import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.dto.prepare.GamePlayersInfoResponse;
import com.a301.theknight.domain.game.dto.prepare.PlayerDataDto;
import com.a301.theknight.domain.game.dto.prepare.TeamLeaderDto;
import com.a301.theknight.domain.game.dto.prepare.response.GameLeaderDto;
import com.a301.theknight.domain.game.dto.prepare.response.GameOrderDto;
import com.a301.theknight.domain.game.dto.prepare.response.GamePlayersInfoDto;
import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.*;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.player.entity.Player;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.global.error.errorcode.GameErrorCode;
import com.a301.theknight.global.error.exception.CustomRestException;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.*;

@Service
public class PrepareDataService extends GameDataService {

    private final GameRedisRepository redisRepository;
    private final GameRepository gameRepository;

    public PrepareDataService(RedissonClient redissonClient, GameRedisRepository redisRepository,
                                  GameRepository gameRepository) {
        super(redissonClient);
        this.redisRepository = redisRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    @Transactional
    public void makeData(long gameId) {

    }

    @Override
    @Transactional
    public void sendScreenData(long gameId, SendMessageService messageService) {
        Game game = getGame(gameId);
        if (!game.isCanStart()) {
            throw new CustomWebSocketException(CAN_NOT_PLAYING_GAME);
        }
        List<Player> players = game.getPlayers();

        GameWeaponData weaponData = makeWeaponsData(game);
        choiceLeader(players);
        initInGameData(game);
        makeInGamePlayerData(gameId, players);
        //======================================================

//        GameWeaponData weaponsData = getWeaponsData(gameId, Team.A);
        messageService.sendData(gameId, "/a/weapons", weaponData);
        messageService.sendData(gameId, "/b/weapons", weaponData);

//        Game game = getGame(gameId);
        GameLeaderDto leadersData = getLeadersData(game);
        messageService.sendData(gameId, "/a/leader", leadersData.getTeamA());
        messageService.sendData(gameId, "/b/leader", leadersData.getTeamB());

        GamePlayersInfoResponse response = getGamePlayerData(gameId);
        messageService.sendData(gameId, "/a/players", response.getPlayersAInfoDto());
        messageService.sendData(gameId, "/b/players", response.getPlayersBInfoDto());
    }

    private GamePlayersInfoResponse getGamePlayerData(long gameId) {
        GamePlayersInfoDto playersAInfo = getTeamPlayersInfo(gameId, Team.A);
        GamePlayersInfoDto playersBInfo = getTeamPlayersInfo(gameId, Team.B);

        return GamePlayersInfoResponse.builder()
                .playersAInfoDto(playersAInfo)
                .playersBInfoDto(playersBInfo)
                .build();
    }

    private GamePlayersInfoDto getTeamPlayersInfo(long gameId, Team team) {
        List<InGamePlayer> playerList = redisRepository.getTeamPlayerList(gameId, team);
        List<PlayerDataDto> players = playerList.stream()
                .map(PlayerDataDto::toDto)
                .collect(Collectors.toList());

        return GamePlayersInfoDto.builder()
                .maxMember(players.size())
                .players(players).build();
    }

    private void choiceLeader(List<Player> players) {
        List<Player> teamA = getTeamPlayerList(players, Team.A);
        List<Player> teamB = getTeamPlayerList(players, Team.B);

        Random random = new Random();
        int teamALeaderIndex = random.nextInt(teamA.size());
        int teamBLeaderIndex = random.nextInt(teamB.size());

        Player teamALeader = teamA.get(teamALeaderIndex);
        Player teamBLeader = teamB.get(teamBLeaderIndex);
        teamALeader.becomeLeader();
        teamBLeader.becomeLeader();
    }

    private List<Player> getTeamPlayerList(List<Player> players, Team team) {
        return players.stream().filter(player -> player.getTeam().equals(team))
                .collect(Collectors.toList());
    }

    private void initInGameData(Game game) {
        Team firstAttackTeam = getRandomFirstAttackTeam();

        TeamInfoData teamAInfo = makeTeamInfoData(game, getTeamLeaderId(game, Team.A));
        TeamInfoData teamBInfo = makeTeamInfoData(game, getTeamLeaderId(game, Team.B));

        redisRepository.saveInGame(game.getId(), InGame.builder()
                .gameStatus(GameStatus.PREPARE)
                .currentAttackTeam(firstAttackTeam)
                .maxMemberNum(game.getCapacity())
                .teamAInfo(teamAInfo)
                .teamBInfo(teamBInfo)
                .turnData(new TurnData()).build());
    }

    private Team getRandomFirstAttackTeam() {
        return (int) (Math.random() * 10) % 2 == 0 ? Team.A : Team.B;
    }

    private TeamInfoData makeTeamInfoData(Game game, Long leaderId) {
        int peopleNum = game.getPlayers().size() / 2;

        return TeamInfoData.builder()
                .currentAttackIndex(peopleNum - 1)
                .orderList(new GameOrderDto[peopleNum])
                .leaderId(leaderId == null ? 0 : leaderId).build();
    }

    private void makeInGamePlayerData(long gameId, List<Player> players) {
        List<InGamePlayer> inGamePlayers = players.stream()
                .map(player -> InGamePlayer.builder()
                        .memberId(player.getMember().getId())
                        .nickname(player.getMember().getNickname())
                        .image(player.getMember().getImage())
                        .team(player.getTeam())
                        .isLeader(player.isLeader())
                        .leftCount(3)
                        .rightCount(3).build()).collect(Collectors.toList());
        redisRepository.saveInGamePlayerAll(gameId, inGamePlayers);
    }

    private GameWeaponData makeWeaponsData(Game game) {
        GameWeaponData gameWeaponDataA = GameWeaponData.toWeaponData(game);
        GameWeaponData gameWeaponDataB = GameWeaponData.toWeaponData(game);

        redisRepository.saveGameWeaponData(game.getId(), Team.A, gameWeaponDataA);
        redisRepository.saveGameWeaponData(game.getId(), Team.B, gameWeaponDataB);
        return gameWeaponDataA;
    }

    private GameLeaderDto getLeadersData(Game game) {
        Long teamALeaderId = getTeamLeaderId(game, Team.A);
        Long teamBLeaderId = getTeamLeaderId(game, Team.B);

        return GameLeaderDto.builder()
                .teamA(new TeamLeaderDto(teamALeaderId == null ? 0 : teamALeaderId))
                .teamB(new TeamLeaderDto(teamBLeaderId == null ? 0 : teamBLeaderId)).build();
    }

    private Long getTeamLeaderId(Game game, Team team) {
        return game.getTeamLeader(team)
                .orElseThrow(() -> new CustomWebSocketException(ORDER_NUMBER_IS_INVALID))
                .getMember().getId();
    }

    private GameWeaponData getWeaponsData(long gameId, Team team) {
        return redisRepository.getGameWeaponData(gameId, team)
                .orElseThrow(() -> new CustomWebSocketException(WEAPON_DATA_IS_NOT_EXIST));
    }

    private Game getGame(long gameId) {
        return gameRepository.findByIdFetchJoin(gameId)
                .orElseThrow(() -> new CustomRestException(GameErrorCode.GAME_IS_NOT_EXIST));
    }
}
