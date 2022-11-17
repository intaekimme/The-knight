import { useState, useEffect } from "react";
import { useSelector } from "react-redux";
import { useSpring, useChain, useSpringRef, animated } from "react-spring";
import hurtedShieldImg from "../../../_assets/game/hurted-shield.png";
import originalShieldImg from "../../../_assets/game/original-shield.png";

export default function Shield(props) {
  const playersDOM = useSelector((state) => state.game.playersDOM);
  let width = playersDOM[props.defender].width;
  let height = playersDOM[props.defender].height;
  let defenderX = playersDOM[props.defender].x + width / 2.4;
  let defenderY = playersDOM[props.defender].y - height / 2.1;

  const [isHurted, setIsHurted] = useState(false);
  useEffect(() => {
    setTimeout(() => {
      setIsHurted(true);
    }, 930);
  }, []);

  const disappearRef = useSpringRef();

  const disappearProps = useSpring({
    from: { opacity: 1 },
    to: { opacity: 0 },
    config: { duration: 600 },
    delay: 930,
    ref: disappearRef,
  });
  useChain([disappearRef]);

  return (
    <div>
      <animated.div style={{ ...disappearProps }}>
        <img
          src={hurtedShieldImg}
          alt="hurted-shield"
          style={{
            width: "9vmin",
            height: "9vmin",
            position: "absolute",
            top: defenderY,
            left: defenderX,
            zIndex: 2,
          }}
        ></img>
      </animated.div>
      <img
        src={originalShieldImg}
        alt="original-shield"
        style={{
          width: "9vmin",
          height: "9vmin",
          ...(isHurted && { display: "none" }),
          position: "absolute",
          top: defenderY,
          left: defenderX,
          zIndex: 2,
        }}
      ></img>
    </div>
  );
}
