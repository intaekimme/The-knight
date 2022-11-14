import { useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import api from "../../api/api"
import PersonIcon from "@mui/icons-material/Person";
import Box from "@mui/material/Box";

// 해당 Player가 본인이면 녹색아이콘
function Player({ player, isOpp }) {
  const dispatch = useDispatch();
  const me = useSelector((state) => state.game.me);
  const currentAttacker = useSelector((state) => state.game.currentAttacker);
  const selectAttack = useSelector((state) => state.game.selectAttack);
  const doubtPassList = useSelector((state) => state.game.doubtPassList);
  const players = useSelector((state) => state.game.players);
  const isAttackPhase = useSelector((state) => state.game.phase) === "ATTACK";
  const isAttackDoubtPhase =
    useSelector((state) => state.game.phase) === "ATTACK_DOUBT";
  const isDefenseDoubtPhase =
    useSelector((state) => state.game.phase) === "DEFENSE_DOUBT";
  const isMe = player.memberId === me.memberId;
  const isAttacker = player.memberId === currentAttacker.memberId;
  const AttackerIsMe = me.memberId === currentAttacker.memberId;
  const isPass = doubtPassList.includes(player.memberId);

  // const stompClient = useSelector((state) => state.websocket.stompClient);
  // const memberId = parseInt(window.localStorage.getItem("memberId"));
  // const myTeam = useSelector((state) => state.room.usersInfo).find(user => user.id === memberId).team;
  // const gameId = useSelector((state) => state.room.roomInfo).gameId;
  
  const [isHovering, setIsHovering] = useState(false);
  const size = {
    2: "14vmin",
    3: "13vmin",
    4: "12vmin",
    5: "11vmin",
  }

  const onPubAttack = () => {
    const data = {
      ...selectAttack,
      defenser: {
        memberId: player.memberId
      }
    }
    // stompClient.send(api.pubAttack(gameId), {}, JSON.stringify(data));
    console.log(data)
  }

  const onSelectPlayer = () => {
    onPubAttack();
  }

  return (
    <Box
    sx={{
      display: "flex",
      flexDirection: "column",
      alignItems: "center",
      position: "relative",
    }}
    >
      <PersonIcon
        sx={{
          ...(isMe && { color: "green" }),
          ...(isAttacker && { background: "radial-gradient(yellow 10%, white 90%)", borderRadius: "50%" }),
          ...(isAttackPhase &&
            AttackerIsMe &&
            isOpp &&
            selectAttack.weapon &&
            isHovering && { background: "radial-gradient(red 10%, white 90%)", borderRadius: "50%" }),
            fontSize: size[String(players.maxMember / 2)],
          }}
        onClick={isAttackPhase && AttackerIsMe && isOpp && selectAttack.weapon && onSelectPlayer}
        onMouseOver={() => setIsHovering(true)}
        onMouseOut={() => setIsHovering(false)}
      />
      <Box sx={{ ...(isMe && { color: "green" }), fontSize: "2vmin" }}>{player.nickname}</Box>
      {(isAttackDoubtPhase || isDefenseDoubtPhase) && isPass && (
        <div
          style={{
            position: "absolute",
            top: "50%",
            left: "50%",
            transform: "translate(-50%, -50%)",
          }}
        >
          PASS
        </div>
      )}
    </Box>
  );
}

export default Player;
