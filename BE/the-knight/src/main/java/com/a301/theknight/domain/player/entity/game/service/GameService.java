package com.a301.theknight.domain.player.entity.game.service;

import com.a301.theknight.domain.player.entity.Player;
import com.a301.theknight.domain.player.entity.game.dto.GameCreateRequest;
import com.a301.theknight.domain.player.entity.game.dto.GameListDto;
import com.a301.theknight.domain.player.entity.game.dto.GameListResponse;
import com.a301.theknight.domain.player.entity.game.entity.Game;
import com.a301.theknight.domain.player.entity.game.repository.GameRepository;
import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.member.repository.MemberRepository;
import com.a301.theknight.domain.player.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GameService {
    private final GameRepository gameRepository;
    private final MemberRepository memberRepository;
    private final PlayerRepository playerRepository;

    //  테스트용
    private static long memberId = 1L;

    @Transactional(readOnly = true)
    public GameListResponse getGameList(String keyword, Pageable pageable){
        GameListResponse gameListResponse = new GameListResponse();
        Page<Game> gamePage = null;
        if(keyword != null){
            gamePage = gameRepository.findByTitleIsContaining(keyword, pageable)
                    .orElseThrow(() -> new NoSuchElementException("검색 결과가 없습니다."));
        }else{
            gamePage = gameRepository.findAll(pageable);
        }

        List<GameListDto> gameListDtos = gamePage.stream().map(game -> {
            int players = (int)playerRepository.countPlayers(game.getId());

            return GameListDto.builder()
                    .gameId(game.getId())
                    .title(game.getTitle())
                    .status(game.getStatus().name())
                    .capacity(game.getCapacity())
                    .participant(players)
                    .build();
        }).collect(Collectors.toList());

        gameListResponse.setGames(gameListDtos);

        return gameListResponse;
    }

    @Transactional
    public void createGame(GameCreateRequest gameCreateRequest){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));

        Game newGame = gameCreateRequest.toEntity();
        gameRepository.save(newGame);

    }
}
