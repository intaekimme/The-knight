import { useSelector } from "react-redux";

export default function FindMemberById(memberId) {
  const players = useSelector((state) => state.game.players);

  return players.players.filter((player) => player.memberId === memberId)[0]
}