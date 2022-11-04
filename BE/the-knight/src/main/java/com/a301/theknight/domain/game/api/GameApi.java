package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.auth.annotation.LoginMemberId;
import com.a301.theknight.domain.game.dto.GameCreationResponse;
import com.a301.theknight.domain.game.dto.GameListRequest;
import com.a301.theknight.domain.game.dto.waiting.response.GameInfoResponse;
import com.a301.theknight.domain.game.dto.waiting.request.GameCreateRequest;
import com.a301.theknight.domain.game.dto.waiting.response.GameListResponse;
import com.a301.theknight.domain.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/games")
@RequiredArgsConstructor
@RestController
public class GameApi {

    private final GameService gameService;

    @GetMapping
    public ResponseEntity<?> getGameList(@RequestParam(required = false) GameListRequest keyword,
                                         Pageable pageable,
                                         @LoginMemberId long memberId) {
        GameListResponse gameListResponse = gameService.getGameList(keyword.getKeyword(), pageable, memberId);
        return ResponseEntity.ok(gameListResponse);
    }

    @PostMapping
    public ResponseEntity<?> createGame(@RequestBody GameCreateRequest gameCreateRequest,
                                        @LoginMemberId long memberId) {
        GameCreationResponse newGameId = gameService.createGame(gameCreateRequest, memberId);
        return ResponseEntity.ok(newGameId);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<?> getGameInfo(@PathVariable long gameId,
                                         @LoginMemberId long memberId) {
        GameInfoResponse gameInfoResponse = gameService.getGameInfo(gameId, memberId);
        return ResponseEntity.ok(gameInfoResponse);
    }
}
