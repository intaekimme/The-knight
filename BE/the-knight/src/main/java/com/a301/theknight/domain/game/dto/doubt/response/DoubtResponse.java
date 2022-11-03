package com.a301.theknight.domain.game.dto.doubt.response;


import com.a301.theknight.domain.game.dto.doubt.request.DoubtPlayerIdDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoubtResponse {
    private DoubtPlayerDto suspect;
    private DoubtPlayerDto suspected;
    private DoubtOpenDto open;
    private boolean keepDefense;
    private DoubtPlayerIdDto nextDefender;
    private boolean SuspiciousResult;
}

/*
    * 의심
{
  suspect : {
    id : long : 의심하는 사람 Id,
    isDead : boolean : 사망 여부,
  }
  suspected : {
    id : long : 의심 당하는 사람
    isDead : boolean : 사망 여부,
  }
  open : {
    memberId : long,
    weapon : String
    (SWORD, TWIN, SHEILD, HAND, HIDE)
    hand : String (LEFT, RIGHT)
  }
  isNextDefend : boolean : 이후 방어 수행여부
  nextDefender : {
    id : long : 다음 방어자 id
  }
  isSuspectSuccess : boolean : 의심 성공 여부
  }
  *
  * }
}
    *
    * */