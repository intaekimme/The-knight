import { useDispatch, useSelector } from "react-redux";
import { selectWeapon } from "../../_slice/gameSlice";

import { Box, Grid } from "@mui/material";

function WeaponPicker() {
  const dispatch = useDispatch();
  const weapons = ["sword", "twin", "shield", "hand"];
  const countWeapon = useSelector((state) => state.game.countWeapon);
  const isSelectComplete = useSelector((state) => state.game.isSelectComplete);
  function onClick(weapon) {
    dispatch(selectWeapon(weapon));
  }
  const weaponsKr = {
    sword: "검",
    twin: "쌍검",
    shield: "방패",
    hand: "맨손",
  }

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
            <div
              onClick={() => onClick(weapon)}
              style={{
                width: "12vmin",
                height: "12vmin",
                ...((isSelectComplete || !countWeapon[weapon])
                  ? { backgroundColor: "#646464" }
                  : { backgroundColor: "#f0f0f0" }),
                border: "7px solid #7406ff",
                borderRadius: "10px",
                position: "relative",
                display: "flex",
                justifyContent: "center",
              }}
            >
              {weaponsKr[weapon]}
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
                  border: "7px solid #ffe600",
                  borderRadius: "10px"
                }}
              >
                {countWeapon[weapon]}
              </Box>
            </div>
          </Grid>
        );
      })}
    </Grid>
  );
}

export default WeaponPicker;
