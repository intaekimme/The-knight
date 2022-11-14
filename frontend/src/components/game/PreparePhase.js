import { useSelector } from "react-redux";
import PlayerWithWeaponList from "./PlayerWithWeaponList";
import OrderPicker from "./OrderPicker";
import WeaponPicker from "./WeaponPicker";
import { Box } from "@mui/material";

export default function PreparePhase() {
  const timer = useSelector((state) => state.game.timer.timer)
  const leader = useSelector((state) => state.game.leader)
  const me = useSelector((state) => state.game.me)
  const isLeader = me.memberId === leader

  function onClick() {
    console.log("선택완료");
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
      <Box sx={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
        <h1 style={{ marginBottom: ".5rem" }}>
          진행 순서와 무기를 선택하세요
        </h1>
        <h2 style={{marginTop: ".5rem", marginBottom: ".5rem"}}>{timer}</h2>
        <div
      style={{
        width: "70vw",
        height: "7vh",
        backgroundColor: "#d9d9d9",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
      }}
    >
      {isLeader ?
      "당신은 리더입니다" :
      "제한시간 내에 선택하지 않으면 순서와 무기가 랜덤으로 배정됩니다" }
    </div>
      </Box>
      <OrderPicker />
      <WeaponPicker></WeaponPicker>
      {isLeader &&
      <div
      style={{
        width: "30vw",
        height: "7vh",
        backgroundColor: "#d9d9d9",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
      }}
      onClick={onClick}
    >
      선택완료
    </div>}
      <PlayerWithWeaponList></PlayerWithWeaponList>
    </Box>
  );
}