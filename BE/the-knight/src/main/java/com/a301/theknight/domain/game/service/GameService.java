package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.waiting.GameListDto;
import com.a301.theknight.domain.game.dto.waiting.request.GameCreateRequest;
import com.a301.theknight.domain.game.dto.waiting.response.GameCreationResponse;
import com.a301.theknight.domain.game.dto.waiting.response.GameInfoResponse;
import com.a301.theknight.domain.game.dto.waiting.response.GameListResponse;
import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.member.repository.MemberRepository;
import com.a301.theknight.domain.player.entity.Player;
import com.a301.theknight.domain.player.repository.PlayerRepository;
import com.a301.theknight.global.error.errorcode.GameErrorCode;
import com.a301.theknight.global.error.errorcode.MemberErrorCode;
import com.a301.theknight.global.error.exception.CustomRestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GameService {
    private final GameRepository gameRepository;
    private final MemberRepository memberRepository;
    private final PlayerRepository playerRepository;


    @Transactional(readOnly = true)
    public GameListResponse getGameList(@Nullable String keyword, long memberId, Pageable pageable){
        Page<Game> gamePage = gameRepository.findGameList(keyword, pageable);

        List<GameListDto> gameListDtos = gamePage.stream()
                .map(game -> GameListDto.builder()
                        .gameId(game.getId())
                        .title(game.getTitle())
                        .status(game.getStatus().name())
                        .maxMember(game.getCapacity())
                        .currentMembers(game.getPlayers().size()).build())
                .collect(Collectors.toList());

        return GameListResponse.builder()
                .maxPageNum(gamePage.getTotalPages())
                .games(gameListDtos)
                .build();
    }

    @Transactional
    public GameCreationResponse createGame(GameCreateRequest gameCreateRequest, long memberId){
        Game newGame = gameCreateRequest.toEntity();
        newGame = gameRepository.save(newGame);

        Member owner = getMember(memberId);
        Player ownerPlayer = Player.builder()
                .game(newGame)
                .member(owner).build();
        ownerPlayer.setOwner();
        playerRepository.save(ownerPlayer);

        return new GameCreationResponse(newGame.getId());
    }

    @Transactional(readOnly = true)
    public GameInfoResponse getGameInfo(long gameId, long memberId){
        getMember(memberId);
        Game findGame = getGame(gameId);

        return GameInfoResponse.builder()
                .gameId(gameId)
                .title(findGame.getTitle())
                .maxMember(findGame.getCapacity())
                .currentMembers(findGame.getPlayers().size())
                .sword(findGame.getSword())
                .twin(findGame.getTwin())
                .shield(findGame.getShield())
                .hand(findGame.getHand())
                .ownerId(findGame.getOwner().getMember().getId())
                .build();
    }

    private Member getMember(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomRestException(MemberErrorCode.MEMBER_IS_NOT_EXIST));
    }

    private Game getGame(long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new CustomRestException(GameErrorCode.GAME_IS_NOT_EXIST));
    }

}
