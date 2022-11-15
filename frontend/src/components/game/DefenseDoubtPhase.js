import { useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { addDoubtPass, initializePass } from "../../_slice/gameSlice";
import PlayerWithWeaponList from "./PlayerWithWeaponList";
import Box from "@mui/material/Box";

export default function DefenseDoubtPhase() {
  const dispatch = useDispatch();
  const me = useSelector((state) => state.game.me);
  const timer = useSelector((state) => state.game.timer).timer;
  const defenseInfo = useSelector((state) => state.game.defenseInfo);
  const side = {
    LEFT: "왼쪽",
    RIGHT: "오른쪽",
  };

  // const stompClient = useSelector((state) => state.websocket.stompClient);
  // const memberId = parseInt(window.localStorage.getItem("memberId"));
  // const myTeam = useSelector((state) => state.room.usersInfo).find(user => user.id === memberId).team;
  // const gameId = useSelector((state) => state.room.roomInfo).gameId;

  const onPubDoubt = () => {
    // {
    //   suspected : {
    //     memberId : long : 의심 당하는 사람
    //   },
    //   doubtStatus: String
    //    (ATTACK_DOUBT, DEFENSE_DOUBT)
    // }
    const data = {
      suspected: {
        memberId: defenseInfo.defender.memberId,
      },
      doubtStatus: "DEFENSE_DOUBT",
    };
    // stompClient.send(api.pubDoubt(gameId), {}, JSON.stringify(data));
    console.log(data);
  };

  const onPubDoubtPass = () => {
    // stompClient.send(api.pubDoubtPass(gameId), {}, {});
    console.log("패스");
  };

  function clickDoubt() {
    onPubDoubt();
  }

  function clickPass() {
    onPubDoubtPass();
    dispatch(addDoubtPass());
  }

  useEffect(() => {
    dispatch(initializePass());
  }, []);

  function BoxRender() {
    // 방어자가 우리 팀일 때
    if (me.team === defenseInfo.defender.team) {
      return (
        <Box
          sx={{
            width: "70vmin",
            height: "40vmin",
            backgroundColor: "#d9d9d9",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            position: "relative",
          }}
        >
          <Box sx={{ fontSize: "2.5vmin" }}>적팀이 의심여부를 선택 중입니다</Box>
          <Box sx={{ position: "absolute", bottom: "2vmin", fontSize: "2vmin" }}>
            제한시간 : {timer}
          </Box>
        </Box>
      );
      // 방어자가 적팀일 때
    } else {
      return (
        <Box
          sx={{
            width: "70vmin",
            height: "40vmin",
            backgroundColor: "#d9d9d9",
            display: "flex",
            flexDirection: "column",
            justifyContent: "space-evenly",
          }}
        >
          <Box sx={{ textAlign: "center" }}>
            {defenseInfo.defender.nickname}이(가) {side[defenseInfo.hand]} 방패로 방어했습니다.
          </Box>
          <Box sx={{ display: "flex", alignItems: "flex-end", justifyContent: "space-evenly" }}>
            <Box onClick={() => clickDoubt} sx={{ width: "11vmin", height: "11vmin", backgroundColor: "#e2e2e2" }}>의심</Box>
            <Box
              onClick={() => clickPass}
              sx={{ width: "11vmin", height: "11vmin", backgroundColor: "#e2e2e2" }}
            >
              Pass
            </Box>
          </Box>
          <Box sx={{ textAlign: "center" }}>제한시간: 100</Box>
        </Box>
      );
    }
  }

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        justifyContent: "space-between",
        alignItems: "center",
        height: "90vh",
      }}
    >
      <PlayerWithWeaponList isOpp={true}></PlayerWithWeaponList>
      <BoxRender></BoxRender>
      <PlayerWithWeaponList></PlayerWithWeaponList>
    </Box>
  );
}
