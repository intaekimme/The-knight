import { useDispatch } from "react-redux"
import { selectWeapon } from "../../_slice/gameSlice"

import Grid from "@mui/material/Grid";

function WeaponPicker() {
  const dispatch = useDispatch();
  const weapons = ["sword", "twin swords", "shield", "hand"];
  function onClick(weapon) {
    dispatch(selectWeapon(weapon));
  }

  return (
    <Grid container>
      {weapons.map((weapon, index) => {
        return (
          <Grid
            item
            xs={12 / weapons.length}
            key={index}
          >
            <div onClick={() => onClick(weapon)} style={{ width: 80, height: 80, backgroundColor: "#e2e2e2" }}>{weapon}</div>
          </Grid>
        );
      })}
    </Grid>
  );
}

export default WeaponPicker;
