import { useState, useRef, useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { setDOM } from "../../_slice/gameSlice";
import api from "../../api/api";
import PersonIcon from "@mui/icons-material/Person";
import Box from "@mui/material/Box";
import deadImg from "../../_assets/game/image/dead.png";
import knightImg from "../../_assets/game/image/knight.png";
import knightGreenImg from "../../_assets/game/image/knight-green.png";
import clickSound from "../../_assets/game/sound/sound-click.mp3";

// 해당 Player가 본인이면 녹색아이콘
function Player({
  player,
  isOpp,
  size,
  fontColor,
  meFontColor,
  nicknameLength,
}) {
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

  let isPass = doubtPassList.includes(player.memberId);
  useEffect(() => {
    isPass = doubtPassList.includes(player.memberId);
  }, [doubtPassList]);

  const clickAudio = new Audio(clickSound);

  const stompClient = useSelector((state) => state.websocket.stompClient);
  const memberId = parseInt(window.localStorage.getItem("memberId"));
  const myTeam = useSelector((state) => state.game.me).team;
  const gameId = useSelector((state) => state.room.roomInfo).gameId;

  const [isHovering, setIsHovering] = useState(false);
  const person = useRef();

  useEffect(() => {
    dispatch(
      setDOM({
        memberIdString: player.memberId.toString(),
        dom: person.current.getBoundingClientRect(),
      })
    );
  }, []);

  const defaultSize = {
    2: "14vmin",
    3: "13vmin",
    4: "12vmin",
    5: "11vmin",
  };

  const onPubAttack = () => {
    const data = {
      ...selectAttack,
      defender: {
        memberId: player.memberId,
      },
    };
    stompClient.send(api.pubAttack(gameId), {}, JSON.stringify(data));
  };

  const onSelectPlayer = () => {
    clickAudio.play();
    onPubAttack();
  };

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        position: "relative",
      }}
    >
      <img
        ref={person}
        src={isMe ? knightGreenImg : knightImg}
        alt="player"
        style={{
          ...(isMe && { color: "green" }),
          ...(isAttacker && {
            background: "radial-gradient(yellow 10%, white 90%)",
            borderRadius: "50%",
          }),
          ...(isAttackPhase &&
            AttackerIsMe &&
            isOpp &&
            selectAttack.weapon &&
            isHovering && {
              background: "radial-gradient(red 10%, white 90%)",
              borderRadius: "50%",
              cursor: "pointer",
            }),
          ...(size
            ? {
                width: size,
                height: size,
              }
            : {
                width: defaultSize[String(players.maxMember / 2)],
                height: defaultSize[String(players.maxMember / 2)],
              }),
        }}
        onClick={
          isAttackPhase && AttackerIsMe && isOpp && selectAttack.weapon
            ? () => onSelectPlayer()
            : undefined
        }
        onMouseOver={() => setIsHovering(true)}
        onMouseOut={() => setIsHovering(false)}
      />
      {/* <PersonIcon
        ref={person}
        sx={{
          ...(isMe && { color: "green" }),
          ...(isAttacker && {
            background: "radial-gradient(yellow 10%, white 90%)",
            borderRadius: "50%",
          }),
          ...(isAttackPhase &&
            AttackerIsMe &&
            isOpp &&
            selectAttack.weapon &&
            isHovering && {
              background: "radial-gradient(red 10%, white 90%)",
              borderRadius: "50%",
            }),
          fontSize: size[String(players.maxMember / 2)],
        }}
        onClick={
          isAttackPhase && AttackerIsMe && isOpp && selectAttack.weapon
            ? () => onSelectPlayer()
            : undefined
        }
        onMouseOver={() => setIsHovering(true)}
        onMouseOut={() => setIsHovering(false)}
      /> */}
      {player.isDead && (
        <img
          src={deadImg}
          alt="dead"
          style={{
            position: "absolute",
            width: "20vmin",
            height: "20vmin",
            top: "50%",
            left: "50%",
            transform: "translate(-50%,-50%)",
            opacity: "0.8",
          }}
        ></img>
      )}
      <Box
        sx={{
          ...(isMe
            ? meFontColor
              ? { color: meFontColor }
              : { color: "#c1ffa6" }
            : fontColor
            ? { color: fontColor }
            : { color: "white" }),
          fontSize: "2vmin",
        }}
      >
        {nicknameLength && (0 < nicknameLength && nicknameLength < player.nickname.length)
          ? player.nickname.slice(0, nicknameLength - 1) + "..."
          : player.nickname}
      </Box>
      {(isAttackDoubtPhase || isDefenseDoubtPhase) && isPass && (
        <Box
          sx={{
            position: "absolute",
            top: "40%",
            left: "50%",
            transform: "translate(-50%, -50%)",
            fontSize: "3.5vmin",
            color: "white",
            textShadow:
              "-1.5px 0 black, 0 1.5px black, 1.5px 0 black, 0 -1.5px black",
          }}
        >
          PASS
        </Box>
      )}
    </Box>
  );
}

export default Player;
