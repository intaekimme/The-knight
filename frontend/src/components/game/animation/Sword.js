import { useSpring, useChain, useSpringRef, animated } from "react-spring";
import swordImg from "../../../_assets/game/sword.png";

export default function Sword(props) {
  let quadrant = null
  if (props.startX < props.endX && props.startY > props.endY) {
    quadrant = 1
  } else if (props.startX > props.endX && props.startY > props.endY) {
    quadrant = 2
  } else if (props.startX > props.endX && props.startY < props.endY) {
    quadrant = 3
  } else if (props.startX < props.endX && props.startY < props.endY) {
    quadrant = 4
  }
  const appearRef = useSpringRef();
  const rotateRef = useSpringRef();
  const radian = Math.atan((props.endX - props.startX) / (props.endY - props.startY))
  const degree = radian * 180 / Math.PI;
  const startRotate = (quadrant === 1 || quadrant === 2) ? 180 : 0
  const endRotate = startRotate - degree
  const rotateProps = useSpring({
    from: { rotateZ: startRotate, opacity: 0 },
    to: { rotateZ: endRotate, opacity: 1 },
    config: { duration: 800 },
    ref: rotateRef,
  });
  const shootRef = useSpringRef();
  const shootProps = useSpring({
    from: { x: props.startX, y: props.startY },
    to: { x: props.endX, y: props.endY },
    config: { duration: 130 },
    ref: shootRef,
  });
  useChain([appearRef, rotateRef, shootRef]);

  return (
    <animated.div
      style={{
        ...rotateProps,
        ...shootProps,
        position: "absolute",
      }}
    >
      <img
        src={swordImg}
        style={{ width: "6vmin", height: "18vmin" }}
      />
    </animated.div>
  );
}
