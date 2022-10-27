package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.game.dto.GameInfoResponse;
import com.a301.theknight.domain.game.dto.GameCreateRequest;
import com.a301.theknight.domain.game.dto.GameListResponse;
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
    public ResponseEntity<?> getGameList(@RequestParam String keyword, Pageable pageable) {
        GameListResponse gameListResponse = gameService.getGameList(keyword, pageable);
        return ResponseEntity.ok(gameListResponse);
    }

    @PostMapping
    public ResponseEntity<?> createGame(@RequestBody GameCreateRequest gameCreateRequest) {
        long newGameId = gameService.createGame(gameCreateRequest);
        return ResponseEntity.ok(newGameId);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<?> getGameInfo(@PathVariable long gameId) {
        GameInfoResponse gameInfoResponse = gameService.getGameInfo(gameId);
        return ResponseEntity.ok(gameInfoResponse);
    }
}
