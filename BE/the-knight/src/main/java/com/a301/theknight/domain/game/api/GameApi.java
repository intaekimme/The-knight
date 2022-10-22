package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.game.dto.GameCreateRequest;
import com.a301.theknight.domain.game.dto.GameInfoResponse;
import com.a301.theknight.domain.game.dto.GameListResponse;
import com.a301.theknight.domain.game.service.GameService;
import com.a301.theknight.domain.member.dto.MemberHistoryResponse;
import com.a301.theknight.domain.member.dto.MemberInfoResponse;
import com.a301.theknight.domain.member.dto.MemberUpdateRequest;
import com.a301.theknight.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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
    public ResponseEntity<?> getGameInfo() {
        GameInfoResponse gameInfoResponse = new GameInfoResponse();
        return ResponseEntity.ok(gameInfoResponse);
    }
}
