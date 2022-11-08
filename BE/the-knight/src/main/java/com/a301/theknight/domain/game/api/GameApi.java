package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.auth.annotation.LoginMemberId;
import com.a301.theknight.domain.game.dto.waiting.response.GameCreationResponse;
import com.a301.theknight.domain.game.dto.waiting.response.GameInfoResponse;
import com.a301.theknight.domain.game.dto.waiting.request.GameCreateRequest;
import com.a301.theknight.domain.game.dto.waiting.response.GameListResponse;
import com.a301.theknight.domain.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@RequestMapping("/api/games")
@RequiredArgsConstructor
@RestController
public class GameApi {

    private final GameService gameService;

    @GetMapping
    public ResponseEntity<?> getGameList(@Size(max = 100) @RequestParam(required = false) String keyword, Pageable pageable,
                                         @LoginMemberId long memberId) {
        GameListResponse gameListResponse = gameService.getGameList(keyword, pageable, memberId);
        return ResponseEntity.ok(gameListResponse);
    }

    @PostMapping
    public ResponseEntity<?> createGame(@Valid @RequestBody GameCreateRequest gameCreateRequest,
                                        @LoginMemberId long memberId) {
        GameCreationResponse newGameId = gameService.createGame(gameCreateRequest, memberId);
        return ResponseEntity.ok(newGameId);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<?> getGameInfo(@Min(1L) @PathVariable long gameId, @LoginMemberId long memberId) {
        GameInfoResponse gameInfoResponse = gameService.getGameInfo(gameId, memberId);
        return ResponseEntity.ok(gameInfoResponse);
    }
}
