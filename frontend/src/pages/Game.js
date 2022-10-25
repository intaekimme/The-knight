import Player from '../components/game/player'

export default function Information() {
  return (
    <div>
      <h1>진행 순서와 무기를 선택하세요</h1>
      <h3>100</h3>
      <Player isMe={true} />
      <Player isMe={false} />
      <Player isMe={false} />
      <Player isMe={false} />
      <Player isMe={false} />
    </div>
  );
}