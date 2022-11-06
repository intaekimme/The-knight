import PlayerWithWeaponList from "./PlayerWithWeaponList";
import OrderPicker from "./OrderPicker";
import WeaponPicker from "./WeaponPicker";
import TimeLimit from "./TimeLimit";

export default function WeaponSelectPhase() {
  return (
    <div>
    <h1>진행 순서와 무기를 선택하세요</h1>
      <TimeLimit></TimeLimit>
      <div style={{ backgroundColor: "grey" }}>당신은 리더입니다.</div>
      <OrderPicker />
      <WeaponPicker></WeaponPicker>
      <PlayerWithWeaponList></PlayerWithWeaponList>
    </div>
  )
}