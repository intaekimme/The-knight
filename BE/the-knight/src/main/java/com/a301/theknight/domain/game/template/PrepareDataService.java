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
import com.a301.theknight.domain.game.util.GameLockUtil;
import com.a301.theknight.domain.player.entity.Player;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.global.error.errorcode.GameErrorCode;
import com.a301.theknight.global.error.exception.CustomRestException;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.CAN_NOT_PLAYING_GAME;
import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.LEADER_IS_NOT_SELECTED;

@Service
public class PrepareDataService extends GameDataService {

    private final GameRedisRepository redisRepository;
    private final GameRepository gameRepository;

    public PrepareDataService(GameLockUtil gameLockUtil, GameRedisRepository redisRepository,
                              GameRepository gameRepository) {
        super(gameLockUtil, redisRepository);
        this.redisRepository = redisRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    public void makeAndSendData(long gameId, SendMessageService messageService) {
        Game game = getGame(gameId);
        if (!game.isCanStart()) {
            throw new CustomWebSocketException(CAN_NOT_PLAYING_GAME);
        }
        List<Player> players = game.getPlayers();

        makeAndSendLeader(gameId, messageService, players);
        makeInGamePlayerData(gameId, players);
        makeAndSendWeapon(gameId, messageService, game);
        initInGameData(game);
    }

    private void makeAndSendWeapon(long gameId, SendMessageService messageService, Game game) {
        GameWeaponData weaponData = makeWeaponsData(game);
        messageService.sendData(gameId, "/a/weapons", weaponData);
        messageService.sendData(gameId, "/b/weapons", weaponData);
    }

    private void makeAndSendLeader(long gameId, SendMessageService messageService, List<Player> players) {
        GameLeaderDto gameLeaderDto = choiceLeader(players);
        messageService.sendData(gameId, "/a/leader", gameLeaderDto.getTeamA());
        messageService.sendData(gameId, "/b/leader", gameLeaderDto.getTeamB());
    }

    private GamePlayersInfoResponse getGamePlayerData(List<InGamePlayer> inGamePlayerList) {
        GamePlayersInfoDto playersAInfo = getTeamPlayersInfo(inGamePlayerList, Team.A);
        GamePlayersInfoDto playersBInfo = getTeamPlayersInfo(inGamePlayerList, Team.B);

        return GamePlayersInfoResponse.builder()
                .playersAInfoDto(playersAInfo)
                .playersBInfoDto(playersBInfo)
                .build();
    }

    private GamePlayersInfoDto getTeamPlayersInfo(List<InGamePlayer> inGamePlayerList, Team team) {
        List<PlayerDataDto> playerDtoList = inGamePlayerList.stream()
                .map(inGamePlayer -> PlayerDataDto.toDto(inGamePlayer, team))
                .sorted((o1, o2) -> o1.getOrder() == o2.getOrder()
                        ? (int) (o1.getMemberId() - o2.getMemberId())
                        : o1.getOrder() - o2.getOrder())
                .collect(Collectors.toList());

        return GamePlayersInfoDto.builder()
                .maxMember(playerDtoList.size())
                .players(playerDtoList).build();
    }

    private GameLeaderDto choiceLeader(List<Player> players) {
        List<Player> teamA = getTeamPlayerList(players, Team.A);
        List<Player> teamB = getTeamPlayerList(players, Team.B);

        Random random = new Random();
        int teamALeaderIndex = random.nextInt(teamA.size());
        int teamBLeaderIndex = random.nextInt(teamB.size());

        Player teamALeader = teamA.get(teamALeaderIndex);
        Player teamBLeader = teamB.get(teamBLeaderIndex);

        //TODO : 시연을 위한 하드코딩, 추후 삭제!
        Optional<Player> optionalLeader = players.stream()
                .filter(player -> player.getMember().getId().equals(113L))
                .findFirst();
        if (optionalLeader.isPresent()) {
            Player leader = optionalLeader.get();
            if (Team.A.equals(leader.getTeam())) {
                teamALeader = leader;
            } else {
                teamBLeader = leader;
            }
        }

        teamALeader.becomeLeader();
        teamBLeader.becomeLeader();

        return GameLeaderDto.builder()
                .teamA(new TeamLeaderDto(teamALeader.getMember().getId()))
                .teamB(new TeamLeaderDto(teamBLeader.getMember().getId()))
                .build();
    }

    private List<Player> getTeamPlayerList(List<Player> players, Team team) {
        return players.stream().filter(player -> player.getTeam().equals(team))
                .collect(Collectors.toList());
    }

    private void initInGameData(Game game) {
        Team firstAttackTeam = getRandomFirstAttackTeam();

        TeamInfoData teamAInfo = makeTeamInfoData(game, getTeamLeaderId(game, Team.A));
        TeamInfoData teamBInfo = makeTeamInfoData(game, getTeamLeaderId(game, Team.B));
        //TODO 시연을 위한 하드코딩
        if (teamAInfo.getLeaderId() == 113L) {
            firstAttackTeam = Team.A;
        } else if (teamBInfo.getLeaderId() == 113L) {
            firstAttackTeam = Team.B;
        }

        redisRepository.saveInGame(game.getId(), InGame.builder()
                .gameStatus(GameStatus.PREPARE)
                .currentAttackTeam(firstAttackTeam)
                .maxMemberNum(game.getCapacity())
                .teamAInfo(teamAInfo)
                .teamBInfo(teamBInfo)
                .turnData(makeTurnData()).build());
    }

    private TurnData makeTurnData() {
        TurnData turnData = new TurnData();
        turnData.setAttackData(AttackData.builder().build());
        turnData.setDefenseData(DefendData.builder().build());
        turnData.setDoubtData(DoubtData.builder().build());

        return turnData;
    }

    private Team getRandomFirstAttackTeam() {
        return (int) (Math.random() * 10) % 2 == 0 ? Team.A : Team.B;
    }

    private TeamInfoData makeTeamInfoData(Game game, Long leaderId) {
        int peopleNum = game.getPlayers().size() / 2;

        return TeamInfoData.builder()
                .currentAttackIndex(peopleNum - 1) //attacker 조회 로직에 의해 마지막 인덱스로 초기화
                .orderList(new GameOrderDto[peopleNum])
                .leaderId(leaderId == null ? 0 : leaderId).build();
    }

    private List<InGamePlayer> makeInGamePlayerData(long gameId, List<Player> players) {
        List<InGamePlayer> inGamePlayers = players.stream()
                .map(player -> InGamePlayer.builder()
                        .memberId(player.getMember().getId())
                        .nickname(player.getMember().getNickname())
                        .image(player.getMember().getImage())
                        .team(player.getTeam())
                        .leader(player.isLeader())
                        .leftCount(0)
                        .rightCount(0).build()).collect(Collectors.toList());
        redisRepository.saveInGamePlayerAll(gameId, inGamePlayers);

        return inGamePlayers;
    }

    private GameWeaponData makeWeaponsData(Game game) {
        GameWeaponData gameWeaponDataA = GameWeaponData.toWeaponData(game);
        GameWeaponData gameWeaponDataB = GameWeaponData.toWeaponData(game);

        redisRepository.saveGameWeaponData(game.getId(), Team.A, gameWeaponDataA);
        redisRepository.saveGameWeaponData(game.getId(), Team.B, gameWeaponDataB);
        return gameWeaponDataA;
    }

    private Long getTeamLeaderId(Game game, Team team) {
        return game.getTeamLeader(team)
                .orElseThrow(() -> new CustomWebSocketException(LEADER_IS_NOT_SELECTED))
                .getMember().getId();
    }

    private Game getGame(long gameId) {
        return gameRepository.findByIdFetchJoin(gameId)
                .orElseThrow(() -> new CustomRestException(GameErrorCode.GAME_IS_NOT_EXIST));
    }
}
