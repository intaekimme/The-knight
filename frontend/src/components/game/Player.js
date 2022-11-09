import { useState } from "react";
import { useSelector } from "react-redux";
import PersonIcon from "@mui/icons-material/Person";
import Box from "@mui/material/Box";

// 해당 Player가 본인이면 녹색아이콘
function Player({ player, isOpp }) {
  const me = useSelector((state) => state.game.me);
  const currentAttacker = useSelector((state) => state.game.currentAttacker);
  const isAttackPhase = useSelector((state) => state.game.phase) === "ATTACK";
  const isAttackDoubtPhase = useSelector((state) => state.game.phase) === "ATTACK_DOUBT";
  const isDefenseDoubtPhase = useSelector((state) => state.game.phase) === "DEFENSE_DOUBT";
  const isMe = player.memberId === me.memberId;
  const isAttacker = player.memberId === currentAttacker.memberId;
  const AttackerIsMe = me.memberId === currentAttacker.memberId;
  const [isHovering, setIsHovering] = useState(false);

  return (
    <Box sx={{ display: "flex", flexDirection: "column", alignItems: "center", position: "relative"}}>
      <PersonIcon
        // color={isMe && "success"}
        sx={{
          ...(isMe && { color: "green" }),
          ...(isAttacker && { backgroundColor: "yellow", borderRadius: "50%" }),
          ...(isAttackPhase && AttackerIsMe && isOpp && isHovering && { backgroundColor: "red", borderRadius: "50%" }),
          fontSize: 100,
        }}
        onMouseOver={() => setIsHovering(true)}
        onMouseOut={() => setIsHovering(false)}
      />
      <div style={{ ...(isMe && { color: "green" }) }}>{player.nickname}</div>
      {(isAttackDoubtPhase || isDefenseDoubtPhase) && isMe && me.pass && <div style={{ position: "absolute", top: "50%", left: "50%", transform: "translate(-50%, -50%)" }}>PASS</div>}
    </Box>
  );
}

export default Player;
