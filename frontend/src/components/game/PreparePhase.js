import PlayerWithWeaponList from "./PlayerWithWeaponList";
import OrderPicker from "./OrderPicker";
import WeaponPicker from "./WeaponPicker";
import TimeLimit from "./TimeLimit";
import LeaderMessage from "./LeaderMessage";
import SelectCompleteButton from "./SelectCompleteButton";
import { Box } from "@mui/material";

export default function PreparePhase() {
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
        <h1 style={{ marginBottom: ".5rem" }}>진행 순서와 무기를 선택하세요</h1>
        <TimeLimit></TimeLimit>
        <LeaderMessage></LeaderMessage>
      </Box>
      <OrderPicker />
      <WeaponPicker></WeaponPicker>
      <SelectCompleteButton></SelectCompleteButton>
      <PlayerWithWeaponList></PlayerWithWeaponList>
    </Box>
  );
}
