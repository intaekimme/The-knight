package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.GameCreateRequest;
import com.a301.theknight.domain.game.dto.GameInfoResponse;
import com.a301.theknight.domain.game.dto.GameListDto;
import com.a301.theknight.domain.game.dto.GameListResponse;
import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.member.repository.MemberRepository;
import com.a301.theknight.global.error.errorcode.GameErrorCode;
import com.a301.theknight.global.error.errorcode.MemberErrorCode;
import com.a301.theknight.global.error.exception.CustomException;
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

    //  테스트용
    private static long memberId = 1L;

    @Transactional(readOnly = true)
    public GameListResponse getGameList(@Nullable String keyword, Pageable pageable){
        GameListResponse gameListResponse = new GameListResponse();
        Page<Game> gamePage = null;
        if(keyword != null){
            gamePage = gameRepository.findByTitleIsContaining(keyword, pageable);
        }else{
            gamePage = gameRepository.findAll(pageable);
        }

        List<GameListDto> gameListDtos = gamePage.stream().map(game -> {
            return GameListDto.builder()
                    .gameId(game.getId())
                    .title(game.getTitle())
                    .status(game.getStatus().name())
                    .capacity(game.getCapacity())
                    .participant(game.getPlayers().size())
                    .build();
        }).collect(Collectors.toList());

        gameListResponse.setGames(gameListDtos);

        return gameListResponse;
    }

    @Transactional
    public long createGame(GameCreateRequest gameCreateRequest){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_IS_NOT_EXIST));

        Game newGame = gameCreateRequest.toEntity();
        gameRepository.save(newGame);
        return newGame.getId();
    }

    @Transactional(readOnly = true)
    public GameInfoResponse getGameInfo(long gameId){
        Game findGame = gameRepository.findById(gameId)
                .orElseThrow(() -> new CustomException(GameErrorCode.GAME_IS_NOT_EXIST));

        return GameInfoResponse.builder()
                .gameId(gameId)
                .title(findGame.getTitle())
                .capacity(findGame.getCapacity())
                .participant(findGame.getPlayers().size())
                .sword(findGame.getSword())
                .twin(findGame.getTwin())
                .shield(findGame.getShield())
                .hand(findGame.getHand())
                .build();
    }
}
