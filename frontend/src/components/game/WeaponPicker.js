import Grid from "@mui/material/Grid";

function WeaponPicker() {
  const weapons = ["sword", "twin swords", "shield", "hand"];

  return (
    <Grid container>
      {weapons.map((weapon, index) => {
        return (
          <Grid
            item
            xs={12 / weapons.length}
            key={index}
          >
            <div style={{ width: 80, height: 80, backgroundColor: "#e2e2e2" }}></div>
          </Grid>
        );
      })}
    </Grid>
  );
}

export default WeaponPicker;
