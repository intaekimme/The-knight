package com.a301.theknight.domain.dummy;


import com.a301.theknight.domain.game.dto.waiting.request.GameCreateRequest;
import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.game.service.GameService;
import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.member.repository.MemberRepository;
import com.a301.theknight.domain.player.dto.request.PlayerTeamRequest;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.domain.player.repository.PlayerRepository;
import com.a301.theknight.domain.player.service.PlayerService;
import com.a301.theknight.domain.ranking.entity.Ranking;
import com.a301.theknight.domain.ranking.repository.RankingRepository;
import com.a301.theknight.global.error.exception.CustomRestException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DummyDataTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private RankingRepository rankingRepository;
    @Autowired
    private GameService gameService;
    @Autowired
    private PlayerService playerService;

    private PlayerTeamRequest playerTeamRequest = new PlayerTeamRequest(Team.B);
    private boolean[] visitedMember = new boolean[95];

    private int[] maxMembers = {4, 10, 8, 6};
    private int[] underStaffed = {7,6,5,2};
    private int[] underStaffedTeam = {3,3,2,1};

    @Test
    @Order(1)
    void 초기화(){
        playerRepository.deleteAll();
        gameRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @Order(2)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void 멤버_생성(){
        for(int i=1; i<=94; i++){
            Member member = new Member(i,
                    "player" + i + "@gmail.com", "player" + i,
                    "player" + i,
                    "player" + i,
                    "ROLE_USER");
            memberRepository.save(member);
        }
    }

    @Test
    @Order(3)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void 게임_생성() {
        List<Member> allMembers = memberRepository.findAll();
        System.out.println("allMember size : " + allMembers.size());

        Random random = new Random(System.currentTimeMillis());
        for(int i=1; i<=14; i++){
            int ownerNum = getRandomMember(random, 95);

            GameCreateRequest gameCreateRequest = createRequest(i);

            gameService.createGame(gameCreateRequest , allMembers.get(ownerNum - 1).getId());
            visitedMember[ownerNum] = true;
        }
    }

    @Test
    @Order(4)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void 플레이어_입장() {
        List<Member> allMembers = memberRepository.findAll();

        Random random = new Random(System.currentTimeMillis());
        List<Game> allGames = gameRepository.findAllByFetchJoin();

        //  정원이 꽉 찬 방, 플레잉 중인 방
        enterFull(allMembers, random, allGames, 0, 8);
        enterFull(allMembers, random, allGames, 12, 14);

        //  정원이 모자른 방
        for(int i=8; i < 12; i++){
            Game game = allGames.get(i);

            while(true){
                Game g = gameRepository.findByIdFetchJoin(game.getId()).get();
                if(g.getPlayers().size() < underStaffed[i-8]){
                    enterRoom(allMembers, random, game);
                } else break;

            }
        }

        System.out.println("I'm done");
    }


    @Test
    @Order(5)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void 플레이어_팀선택(){
        Random random = new Random(System.currentTimeMillis());
        List<Game> allGames = gameRepository.findAllByFetchJoin();

        selectTeamFull(random, allGames, 0, 8);
        selectTeamFull(random, allGames, 12, 14);

        for(int i=8; i<12; i++){
            Game game = allGames.get(i);

            while(true){
                Game g = gameRepository.findByIdFetchJoin(game.getId()).get();
                if(getTeamBCount(g) < underStaffedTeam[i-8]){
                    int playerNum = random.nextInt(g.getPlayers().size());
                    long memberId = g.getPlayers().get(playerNum).getMember().getId();
                    playerService.team(g.getId(), memberId, playerTeamRequest);
                }else break;
            }
        }

    }

    @Test
    @Order(6)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void 기존_회원_랭킹_입력(){
        Random random = new Random(System.currentTimeMillis());
        List<Member> allMembers = memberRepository.findAll();

        for(int j=0; j < 94; j++){
            Ranking ranking = new Ranking(allMembers.get(j));


            int total = random.nextInt(501);
            int win = random.nextInt(501);

            while(total < win){
                win = random.nextInt(501);
            }
            int lose = total - win;

            for(int i=0; i < win; i++){
                ranking.saveWinScore();
            }
            for(int i=0; i < lose; i++){
                ranking.saveLoseScore();
            }

            rankingRepository.save(ranking);
        }


    }

    private void selectTeamFull(Random random, List<Game> allGames, int start, int end) {
        for(int i = start; i< end; i++){
            Game game = allGames.get(i);

            while(!isEqualPlayerNum(gameRepository.findByIdFetchJoin(game.getId()).get())){
                int playerNum = random.nextInt(game.getCapacity());
                long memberId = game.getPlayers().get(playerNum).getMember().getId();
                playerService.team(game.getId(), memberId, playerTeamRequest);
            }
        }
    }


    private void enterFull(List<Member> allMembers, Random random, List<Game> allGames, int start, int end) {
        for(int i = start; i< end; i++){
            Game game = allGames.get(i);

//            Game curGame = gameRepository.findById(game.getId()).get();
            System.out.println("gameId : " + game.getId() + ", maxUsers : " + game.getCapacity() + ", currentUsers : " + game.getPlayers().size());
            while(true){
                if (enterRoom(allMembers, random, game)) break;
            }
        }
    }

    private boolean enterRoom(List<Member> allMembers, Random random, Game game) {
        int playerNum = getRandomMember(random, 95);
        try{
            System.out.println("playerNum : " + playerNum + ", maxUsers : " + game.getCapacity() + ", currentUsers : " + game.getPlayers().size());
            playerService.entry(game.getId(), allMembers.get(playerNum - 1).getId());
            visitedMember[playerNum] = true;
        }catch(CustomRestException e){
            System.out.println("exception occur");
            return true;
        }
        return false;
    }

    private GameCreateRequest createRequest(int i){
        int[] setItemNum = setItem(maxMembers[i % 4]);

        return GameCreateRequest.builder()
                .title("game" + i)
                .maxMember(maxMembers[i % 4])
                .sword(setItemNum[0])
                .twin(setItemNum[1])
                .shield(setItemNum[2])
                .hand(setItemNum[3])
                .build();
    }

    private int[] setItem(int maxUsers){
        int[] setItemNum = new int[4];

        Random random = new Random(System.currentTimeMillis());

        while(true){
            int sum = 0;
            for(int i=0; i<4; i++){
                setItemNum[i] = random.nextInt(maxUsers);
                sum += setItemNum[i];
            }

            if(sum == maxUsers){
                break;
            }
        }
        return setItemNum;
    }

    private int getRandomMember(Random random, int bound){
        int memberNum = random.nextInt(bound);

        while((0 == memberNum) || visitedMember[memberNum]){
            memberNum = random.nextInt(bound);
        }
        System.out.println("memberNum : " + memberNum);

        return memberNum;
    }


    private boolean isYetToAllMemberEnterGame(){
        for(int i=1; i<visitedMember.length - 1; i++){
            if(!visitedMember[i]) return true;
        }
        return false;
    }

    private boolean isEqualPlayerNum(Game game){
        AtomicInteger teamA = new AtomicInteger();
        AtomicInteger teamB = new AtomicInteger();
        game.getPlayers().stream()
                .map(player -> Team.A.equals(player.getTeam()) ? teamA.getAndIncrement() : teamB.getAndIncrement())
                .collect(Collectors.toList());

        System.out.println("A : B = " + teamA.intValue() + " : " + teamB.intValue());
        System.out.println("--------------------");
        System.out.println("A : B = " + teamA.get() + " : " + teamB.get());
        return teamA.intValue() == teamB.intValue();
    }

    private int getTeamBCount(Game g){
        return (int)(g.getPlayers().stream().filter(p -> p.getTeam().name().equals("B")).count());
    }

}