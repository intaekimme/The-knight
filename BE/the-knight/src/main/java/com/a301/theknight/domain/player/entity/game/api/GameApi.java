package com.a301.theknight.domain.player.entity.game.api;

import com.a301.theknight.domain.player.entity.game.dto.GameCreateRequest;
import com.a301.theknight.domain.player.entity.game.dto.GameInfoResponse;
import com.a301.theknight.domain.player.entity.game.dto.GameListResponse;
import com.a301.theknight.domain.player.entity.game.service.GameService;
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
        GameListResponse gameListResponse = new GameListResponse();
        return ResponseEntity.ok(gameListResponse);
    }

    @PostMapping
    public ResponseEntity<?> createGame(@RequestBody GameCreateRequest gameCreateRequest) {
        return null;
//        return ResponseEntity.created(??);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<?> getGameInfo(@PathVariable int gameId) {
        GameInfoResponse gameInfoResponse = new GameInfoResponse();
        return ResponseEntity.ok(gameInfoResponse);
    }
}
