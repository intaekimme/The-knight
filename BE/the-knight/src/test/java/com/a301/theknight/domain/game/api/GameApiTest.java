package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.game.dto.waiting.GameListDto;
import com.a301.theknight.domain.game.dto.waiting.response.GameListResponse;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.service.GameService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.mockito.BDDMockito.given;

@Disabled
@WebMvcTest(GameApi.class)
class GameApiTest {

    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    protected MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    private String keyword = "";
    private long memberId = 1L;



    @DisplayName("[API] [GET] 게임 리스트 조회")
    @Test
    void getGameList() throws Exception{
        //given
        Pageable pageable = PageRequest.of(0,1);
        given(gameService.getGameList(keyword, memberId, pageable))
                .willReturn(new GameListResponse(1, Arrays.asList(GameListDto.builder()
                        .gameId(1L)
                        .title("game1")
                        .status(GameStatus.WAITING.name())
                        .maxMember(10)
                        .currentMembers(1)
                        .build())));

    }

    @Test
    void createGame() {
    }

    @Test
    void getGameInfo() {
    }
}