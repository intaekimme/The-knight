import { useDispatch, useSelector } from "react-redux";
import api from "../../api/api";
import { Box, Grid, Button } from "@mui/material";
import handIconImg from "../../_assets/game/image/hand-icon.png";
import swordIconImg from "../../_assets/game/image/sword-icon.png";
import twinIconImg from "../../_assets/game/image/twin-icon.png";
import shieldIconImg from "../../_assets/game/image/shield-icon.png";
import weaponSelectSound from "../../_assets/game/sound/sound-weapon-select.mp3"

function WeaponPicker() {
  const dispatch = useDispatch();
  const weapons = ["SWORD", "TWIN", "SHIELD", "HAND"];
  const countWeapon = useSelector((state) => state.game.countWeapon);
  const isSelectComplete = useSelector((state) => state.game.isSelectComplete);

  const stompClient = useSelector((state) => state.websocket.stompClient);
  const memberId = parseInt(window.localStorage.getItem("memberId"));
  const myTeam = useSelector((state) => state.game.me).team;
  const gameId = useSelector((state) => state.room.roomInfo).gameId;

  const weaponSelectAudio = new Audio(weaponSelectSound)

  const onPubSelectWeapon = (payload) => {
    // {
    //   weapon : String
    //    (SWORD, TWIN, SHEILD, HAND, HIDE)
    // }
    const data = {
      weapon: payload,
    };
    stompClient.send(api.pubSelectWeapon(gameId), {}, JSON.stringify(data));
  };

  const onClick = (weapon) => {
    weaponSelectAudio.play();
    onPubSelectWeapon(weapon);
  };

  const weaponsKr = {
    SWORD: "검",
    TWIN: "쌍검",
    SHIELD: "방패",
    HAND: "맨손",
  };

  const weaponsImg = {
    SWORD: swordIconImg,
    TWIN: twinIconImg,
    SHIELD: shieldIconImg,
    HAND: handIconImg,
  };

  return (
    <Grid container>
      {weapons.map((weapon, index) => {
        return (
          <Grid
            item
            xs={12 / weapons.length}
            key={index}
            sx={{ display: "flex", justifyContent: "center" }}
          >
            <Button
              onClick={() => onClick(weapon)}
              color="dark"
              style={{
                width: "12vmin",
                height: "12vmin",
                ...(isSelectComplete || !countWeapon[weapon.toLowerCase()]
                  ? { backgroundColor: "#646464" }
                  : { backgroundColor: "#f0f0f0" }),
                border: ".65vmin solid #424242",
                borderRadius: "1.3vmin",
                position: "relative",
                display: "flex",
                justifyContent: "center",
              }}
              disabled={isSelectComplete || !countWeapon[weapon.toLowerCase()]}
            >
              <img
                src={weaponsImg[weapon]}
                alt={weaponsKr[weapon]}
                style={{
                  width: "10vmin",
                  height: "10vmin",
                  position: "absolute",
                  top: "50%",
                  left: "50%",
                  transform: "translate(-50%, -50%)",
                }}
              />
              <Box
                sx={{
                  width: "4vmin",
                  height: "4vmin",
                  backgroundColor: "#d9d9d9",
                  display: "flex",
                  justifyContent: "center",
                  alignItems: "center",
                  position: "absolute",
                  bottom: 0,
                  right: 0,
                  transform: "translate(50%, 50%)",
                  border: ".65vmin solid #ffe600",
                  borderRadius: "1.3vmin",
                  fontSize: "2.5vmin",
                }}
              >
                {countWeapon[weapon.toLowerCase()]}
              </Box>
            </Button>
          </Grid>
        );
      })}
    </Grid>
  );
}

export default WeaponPicker;
