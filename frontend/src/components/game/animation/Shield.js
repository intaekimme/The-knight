import { useState, useEffect } from "react";
import { useSelector } from "react-redux";
import { useSpring, useChain, useSpringRef, animated } from "react-spring";
import hurtedShieldImg from "../../../_assets/game/image/hurted-shield.png";
import originalShieldImg from "../../../_assets/game/image/original-shield.png";

export default function Shield(props) {
  const playersDOM = useSelector((state) => state.game.playersDOM);


  const vmin = ((window.innerHeight > window.innerWidth) ? window.innerWidth : window.innerHeight) / 100
  let width = playersDOM[props.defender].width;
  let height = playersDOM[props.defender].height;
  let defenderX = playersDOM[props.defender].x + width / 2 - 4.5 * vmin
  let defenderY = playersDOM[props.defender].y + height / 2 - 4.5 * vmin

  const [isHurted, setIsHurted] = useState(false);
  useEffect(() => {
    setTimeout(() => {
      setIsHurted(true);
    }, 800);
  }, []);

  const disappearRef = useSpringRef();

  const disappearProps = useSpring({
    from: { opacity: 1 },
    to: { opacity: 0 },
    config: { duration: (props.isTwin ? 900 : 600) },
    delay: 800,
    ref: disappearRef,
  });
  useChain([disappearRef]);

  return (
    <div>
      <animated.div style={{ ...disappearProps, position: "absolute",top: defenderY,
            left: defenderX, zIndex: 2,}}>
        <img
          src={isHurted ? hurtedShieldImg : originalShieldImg}
          alt="hurted-shield"
          style={{
            width: "9vmin",
            height: "9vmin",
          }}
        ></img>
      </animated.div>
    </div>
  );
}
