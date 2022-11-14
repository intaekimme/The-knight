import { useState } from "react";
import { useSelector } from "react-redux";
import PersonIcon from "@mui/icons-material/Person";
import Box from "@mui/material/Box";

// 해당 Player가 본인이면 녹색아이콘
function Player({ player, isOpp }) {
  const me = useSelector((state) => state.game.me);
  const currentAttacker = useSelector((state) => state.game.currentAttacker);
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
  const [isHovering, setIsHovering] = useState(false);
  const size = {
    2: "14vmin",
    3: "13vmin",
    4: "12vmin",
    5: "11vmin",
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
          ...(isAttacker && { backgroundColor: "yellow", borderRadius: "50%" }),
          ...(isAttackPhase &&
            AttackerIsMe &&
            isOpp &&
            isHovering && { backgroundColor: "red", borderRadius: "50%" }),
          fontSize: size[String(players.maxMember / 2)],
        }}
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
