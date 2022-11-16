package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.waiting.request.GameModifyRequest;
import com.a301.theknight.domain.game.dto.waiting.response.GameExitResponse;
import com.a301.theknight.domain.game.dto.waiting.response.GameMembersInfoDto;
import com.a301.theknight.domain.game.dto.waiting.response.GameModifyResponse;
import com.a301.theknight.domain.game.dto.waiting.response.MemberDataDto;
import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.player.entity.Player;
import com.a301.theknight.domain.player.repository.PlayerRepository;
import com.a301.theknight.global.error.errorcode.GameErrorCode;
import com.a301.theknight.global.error.errorcode.GameWaitingErrorCode;
import com.a301.theknight.global.error.errorcode.PlayerErrorCode;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
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
                .orElseThrow(() -> new CustomWebSocketException(GameErrorCode.GAME_IS_NOT_EXIST));

        List<MemberDataDto> members = game.getPlayers().stream()
                .map(player -> MemberDataDto.builder()
                        .id(player.getMember().getId())
                        .nickname(player.getMember().getNickname())
                        .image(player.getMember().getImage())
                        .team(player.getTeam().name())
                        .readyStatus(player.isReady())
                        .build())
                .collect(Collectors.toList());

        return GameMembersInfoDto.builder()
                .ownerId(game.getOwner().getMember().getId())
                .members(members)
                .build();
    }

    @Transactional
    public GameModifyResponse modify(long gameId, long memberId, GameModifyRequest gameModifyRequest){
        Game findGame = getGame(gameId);

        if(findGame.getStatus() == GameStatus.WAITING){
            if(isOwner(findGame, memberId)){
                findGame.ModifyGame(
                        gameModifyRequest.getTitle(),
                        gameModifyRequest.getMaxMember(),
                        gameModifyRequest.getSword(),
                        gameModifyRequest.getTwin(),
                        gameModifyRequest.getShield(),
                        gameModifyRequest.getHand()
                );
            }else{
                throw new CustomWebSocketException(GameWaitingErrorCode.NO_PERMISSION_TO_MODIFY_GAME_ROOM);
            }
        }else{
            throw new CustomWebSocketException(GameWaitingErrorCode.GAME_IS_NOT_READY_STATUS);
        }
        return GameModifyResponse.builder()
                .title(findGame.getTitle())
                .maxMember(findGame.getCapacity())
                .sword(findGame.getSword())
                .twin(findGame.getTwin())
                .shield(findGame.getShield())
                .hand(findGame.getHand())
                .build();
    }

    @Transactional
    public GameExitResponse delete(long gameId, long memberId){
        Game findGame = getGame(gameId);

        if(isOwnerPresent(findGame)){
            if (!isOwner(findGame, memberId)) {
                throw new CustomWebSocketException(GameWaitingErrorCode.NO_PERMISSION_TO_DELETE_GAME_ROOM);
            }
        }

        playerRepository.deleteAll(findGame.getPlayers());
        gameRepository.delete(findGame);

        return gameRepository.findById(gameId).isEmpty()
                ? GameExitResponse.builder().exit(true).build()
                : GameExitResponse.builder().exit(false).build();
    }


    private Game getGame(long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new CustomWebSocketException(GameErrorCode.GAME_IS_NOT_EXIST));
    }

    private boolean isOwner(Game game, long memberId){
        return game.getPlayers().stream().filter(Player::isOwner)
                .findFirst().stream().anyMatch(owner -> owner.getMember().getId().equals(memberId));
    }

    private boolean isOwnerPresent(Game game){
        return game.getPlayers().stream().anyMatch(Player::isOwner);
    }
}
