package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.waiting.request.GameModifyRequest;
import com.a301.theknight.domain.game.dto.waiting.response.GameMembersInfoDto;
import com.a301.theknight.domain.game.dto.waiting.response.MemberDataDto;
import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.player.entity.Player;
import com.a301.theknight.domain.player.repository.PlayerRepository;
import com.a301.theknight.global.error.errorcode.GameErrorCode;
import com.a301.theknight.global.error.errorcode.GameWaitingErrorCode;
import com.a301.theknight.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GameWaitingService {


    private final GameRepository gameRepository;

    private final PlayerRepository playerRepository;

    @Transactional
    public GameMembersInfoDto getMembersInfo(long gameId) {
        Game game = gameRepository.findGameWithPlayers(gameId)
                .orElseThrow(() -> new CustomException(GameErrorCode.GAME_IS_NOT_EXIST));

        List<MemberDataDto> members = game.getPlayers().stream()
                .map(player -> MemberDataDto.builder()
                        .id(player.getMember().getId())
                        .nickname(player.getMember().getNickname())
                        .team(player.getTeam().name())
                        .build())
                .collect(Collectors.toList());

        return GameMembersInfoDto.builder()
                .maxUser(game.getCapacity())
                .members(members)
                .build();
    }

    @Transactional
    public void modify(long gameId, long memberId, GameModifyRequest gameModifyRequest){
        Game findGame = getGame(gameId);

        if(findGame.getStatus() == GameStatus.WAITING){
            if(isOwner(findGame, memberId)){
                findGame.ModifyGame(
                        gameModifyRequest.getTitle(),
                        gameModifyRequest.getCapacity(),
                        gameModifyRequest.getSword(),
                        gameModifyRequest.getTwin(),
                        gameModifyRequest.getShield(),
                        gameModifyRequest.getHand()
                );
            }else{
                throw new CustomException(GameWaitingErrorCode.NO_PERMISSION_TO_MODIFY_GAME_ROOM);
            }

        }else{
            throw new CustomException(GameWaitingErrorCode.GAME_IS_NOT_READY_STATUS);
        }
    }

    @Transactional
    public void delete(long gameId, long memberId){
        Game findGame = getGame(gameId);
        if(isOwner(findGame, memberId)){
            playerRepository.deleteAll(findGame.getPlayers());
            gameRepository.delete(findGame);
        }else{
            throw new CustomException(GameWaitingErrorCode.NO_PERMISSION_TO_DELETE_GAME_ROOM);
        }
    }


    private Game getGame(long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new CustomException(GameErrorCode.GAME_IS_NOT_EXIST));
    }

    private boolean isOwner(Game game, long memberId){
        Player owner = game.getPlayers().get(0);
        return owner.getMember().getId().equals(memberId);
    }
}
