package com.a301.theknight.domain.game.util;

import com.a301.theknight.domain.game.entity.GameStatus;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GameConvertUtil {
    private final Map<String, List<String>> postfixMap;

    public GameConvertUtil() {
        postfixMap = new HashMap<>();
        init(postfixMap);
    }

    private void init(Map<String, List<String>> postfixMap) {
        postfixMap.put(GameStatus.WAITING.name(), new ArrayList<>(List.of("/entry")));
        postfixMap.put(GameStatus.PREPARE.name(), new ArrayList<>(List.of("/prepare")));
        postfixMap.put(GameStatus.PREDECESSOR.name(), new ArrayList<>(List.of("/pre-attack")));
        postfixMap.put(GameStatus.ATTACK.name(), new ArrayList<>(List.of("/attacker")));
        postfixMap.put(GameStatus.ATTACK_DOUBT.name(), new ArrayList<>(List.of("/attack-info")));
        postfixMap.put(GameStatus.DEFEND.name(), new ArrayList<>(List.of("/attack-info")));
        postfixMap.put(GameStatus.DEFEND_DOUBT.name(), new ArrayList<>(List.of("/defend-info")));
        postfixMap.put(GameStatus.DOUBT_RESULT.name(), new ArrayList<>(List.of("doubt-info")));
        postfixMap.put(GameStatus.EXECUTE.name(), new ArrayList<>(List.of("/execute")));
        postfixMap.put(GameStatus.RESULT.name(), new ArrayList<>(Arrays.asList("/action")));
    }

    public List<String> getPostfixList(String status) {
        return postfixMap.get(status);
    }
}
