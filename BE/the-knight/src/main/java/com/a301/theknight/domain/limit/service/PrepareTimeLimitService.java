package com.a301.theknight.domain.limit.service;

import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.dto.prepare.response.GameOrderDto;
import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.Weapon;
import com.a301.theknight.domain.game.entity.redis.GameWeaponData;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import com.a301.theknight.domain.game.entity.redis.TeamInfoData;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.limit.template.TimeLimitServiceTemplate;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.global.error.errorcode.GameErrorCode;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class PrepareTimeLimitService extends TimeLimitServiceTemplate {

    private final GameRedisRepository redisRepository;
    private final GameRepository gameRepository;

    public PrepareTimeLimitService(GameRedisRepository redisRepository, SendMessageService sendMessageService,
                                   RedissonClient redissonClient, GameRepository gameRepository) {
        super(redisRepository, sendMessageService, redissonClient);
        this.redisRepository = redisRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    public void runLimitLogic(long gameId, InGame inGame) {
        if (inGame.isAllSelected()) {
            return;
        }

        TeamInfoData teamAInfoData = inGame.getTeamInfoData(Team.A);
        saveSelectData(gameId, Team.A, teamAInfoData);
        TeamInfoData teamBInfoData = inGame.getTeamInfoData(Team.B);
        saveSelectData(gameId, Team.B, teamBInfoData);

        inGame.changeStatus(GameStatus.PREDECESSOR);
        redisRepository.saveInGame(gameId, inGame);
    }

    private void saveSelectData(long gameId, Team team, TeamInfoData teamInfoData) {
        if (teamInfoData.isSelected()) {
            return;
        }
        saveOrderData(gameId, team, teamInfoData);
        saveWeaponData(gameId, team);
    }

    private void saveWeaponData(long gameId, Team team) {
        /*
         * 무기 랜덤 지정
         * 1. Game에서 무기 정보 받아오기
         * 2. LinkedList에 해당 무기들을 하나씩 저장
         * 3. 무기 랜덤 선택
         *       -> List길이로 Random돌려서 나온 숫자를 리스트 인덱스, 사용 후 그 인덱스 지우기
         * 4. 해당 무기 넣어주기.
         * */
        Game game = getGame(gameId);
        GameWeaponData weaponData = GameWeaponData.toWeaponData(game);
        List<Weapon> weaponList = makeWeaponList(weaponData);

        List<InGamePlayer> teamPlayerList = redisRepository.getTeamPlayerList(gameId, team);
        teamPlayerList.forEach(inGamePlayer -> {
            inGamePlayer.randomChoiceWeapon(randomChoiceInList(weaponList));
            inGamePlayer.randomChoiceWeapon(randomChoiceInList(weaponList));
        });

        redisRepository.saveInGamePlayerAll(gameId, teamPlayerList);
    }

    private void saveOrderData(long gameId, Team team, TeamInfoData teamInfoData) {
        /*
         * 랜덤 순서 지정
         * 1. redis에서 팀 플레이어 리스트 가져오기
         * 2. 랜덤 초이스
         * 3. 해당 플레이어를 order에 저장.
         * */
        List<InGamePlayer> teamPlayerList = redisRepository.getTeamPlayerList(gameId, team);
        int playerSize = teamPlayerList.size();
        for (int i = 0; i < playerSize; i++) {
            InGamePlayer randomPlayer = randomChoiceInList(teamPlayerList);

            teamInfoData.getOrderList()[i] = GameOrderDto.builder()
                    .memberId(randomPlayer.getMemberId())
                    .nickname(randomPlayer.getNickname())
                    .image(randomPlayer.getImage()).build();
        }
    }

    private List<Weapon> makeWeaponList(GameWeaponData weaponData) {
        List<Weapon> weaponList = new LinkedList<>();
        fillWeaponInList(weaponList, Weapon.SWORD, weaponData.getSword());
        fillWeaponInList(weaponList, Weapon.TWIN, weaponData.getTwin());
        fillWeaponInList(weaponList, Weapon.SHIELD, weaponData.getShield());
        fillWeaponInList(weaponList, Weapon.HAND, weaponData.getHand());
        return weaponList;
    }

    private void fillWeaponInList(List<Weapon> weaponList, Weapon weaponType, int size) {
        for (int i = 0; i < size; i++) {
            weaponList.add(weaponType);
        }
    }

    private Game getGame(long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new CustomWebSocketException(GameErrorCode.GAME_IS_NOT_EXIST));
    }

    private <T> T randomChoiceInList(List<T> list) {
        int size = list.size();
        if (size <= 0) {
            return null;
        }
        int randomIndex = (int) (Math.random() * (size - 1));
        return list.remove(randomIndex);
    }
}
