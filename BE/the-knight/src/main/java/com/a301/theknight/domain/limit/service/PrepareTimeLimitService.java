package com.a301.theknight.domain.limit.service;

import com.a301.theknight.domain.game.dto.prepare.response.GameOrderDto;
import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.entity.Weapon;
import com.a301.theknight.domain.game.entity.redis.GameWeaponData;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import com.a301.theknight.domain.game.entity.redis.TeamInfoData;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.game.util.GameLockUtil;
import com.a301.theknight.domain.limit.template.TimeLimitServiceTemplate;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.global.error.errorcode.GameErrorCode;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_IS_NOT_EXIST;

@Component
public class PrepareTimeLimitService extends TimeLimitServiceTemplate {

    private final GameRedisRepository redisRepository;
    private final GameRepository gameRepository;

    public PrepareTimeLimitService(GameRedisRepository redisRepository, GameLockUtil gameLockUtil, GameRepository gameRepository) {
        super(redisRepository, gameLockUtil);
        this.redisRepository = redisRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    @Transactional
    public void runLimitLogic(long gameId) {
        InGame inGame = getInGame(gameId);
        if (inGame.isAllSelected()) {
            return;
        }
        saveSelectData(gameId, inGame, Team.A);
        saveSelectData(gameId, inGame, Team.B);

        redisRepository.saveInGame(gameId, inGame);
    }

    private void saveSelectData(long gameId, InGame inGame, Team team) {
        TeamInfoData teamInfoData = inGame.getTeamInfoData(team);
        if (teamInfoData.isSelected()) {
            return;
        }
        List<InGamePlayer> inGamePlayerList = saveWeaponData(gameId, team);
        saveOrderData(gameId, teamInfoData.getOrderList(), inGamePlayerList);
    }

    private List<InGamePlayer>  saveWeaponData(long gameId, Team team) {
        Game game = getGame(gameId);
        GameWeaponData weaponData = GameWeaponData.toWeaponData(game);

        List<Weapon> weaponList = makeWeaponList(weaponData);
        List<InGamePlayer> teamPlayerList = redisRepository.getTeamPlayerList(gameId, team);

        teamPlayerList.forEach(inGamePlayer -> {
            inGamePlayer.clearWeapon();
            inGamePlayer.randomChoiceWeapon(randomChoiceInList(weaponList));
            inGamePlayer.randomChoiceWeapon(randomChoiceInList(weaponList));
        });
        return teamPlayerList;
    }

    private void saveOrderData(long gameId, GameOrderDto[] orderList, List<InGamePlayer> teamPlayerList) {
        int playerSize = teamPlayerList.size();
        List<InGamePlayer> savePlayerList = new ArrayList<>(playerSize);

        for (int i = 0; i < playerSize; i++) {
            InGamePlayer randomPlayer = randomChoiceInList(teamPlayerList);
            randomPlayer.saveOrder(i + 1);

            orderList[i] = GameOrderDto.builder()
                    .memberId(randomPlayer.getMemberId())
                    .nickname(randomPlayer.getNickname())
                    .image(randomPlayer.getImage()).build();

            savePlayerList.add(randomPlayer);
        }
        redisRepository.saveInGamePlayerAll(gameId, savePlayerList);
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

    private InGame getInGame(long gameId) {
        return redisRepository.getInGame(gameId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_IS_NOT_EXIST));
    }
}
