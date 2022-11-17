import { useSelector } from "react-redux"
import { useSpring, useChain, useSpringRef, animated } from "react-spring";
import swordImg from "../../../_assets/game/sword.png";

export default function Sword(props) {
  const playersDOM = useSelector((state) => state.game.playersDOM)

  const vmin = ((window.innerHeight > window.innerWidth) ? window.innerWidth : window.innerHeight) / 100
  const swordHeight = 18 * vmin
  const width = playersDOM[props.from.toString()].width
  const height = playersDOM[props.from.toString()].height
  let startX = playersDOM[props.from.toString()].x + (width / 3.6)
  let startY = playersDOM[props.from.toString()].y
  let endX = playersDOM[props.to.toString()].x + (width / 3.6)
  let endY = playersDOM[props.to.toString()].y
  
  function radianToDegree(x) {
    return (x * 180) / Math.PI;
  }
  
  
  if (startY > endY) {
    startY -= height * 1.5
  } else {
    startY -= height * 0.5
    endY -= height * 1.8
  }
  
  let quadrant = null
  if (startX < endX && startY > endY) {
    quadrant = 1
  } else if (startX > endX && startY > endY) {
    quadrant = 2
  } else if (startX > endX && startY < endY) {
    quadrant = 3
  } else if (startX < endX && startY < endY) {
    quadrant = 4
  } else if (startX === endX && startY < endY) {
    quadrant = 9
  } else if (startX === endX && startY > endY) { 
    quadrant = 10
  }
  
  if (quadrant === 1) {
    endX = endX - 8 * Math.log(Math.abs(endX - startX))
    endY = endY - 4 * Math.log(Math.abs(endX - startX))
  } else if (quadrant === 2) {
    endX = endX + 8 * Math.log(Math.abs(endX - startX))
    endY = endY - 4 * Math.log(Math.abs(endX - startX))
  } else if (quadrant === 3) {
    endX = endX + 8 * Math.log(Math.abs(endX - startX))
    endY = endY + 4 * Math.log(Math.abs(endX - startX))
  } else if (quadrant === 4) {
    endX = endX - 8 * Math.log(Math.abs(endX - startX))
    endY = endY + 4 * Math.log(Math.abs(endX - startX))
  }

  const rotateRef = useSpringRef();
  const shootRef = useSpringRef();
  const disappearRef = useSpringRef();
  
  // 각도 계산
  const radian = Math.atan((endX - startX) / (endY - startY))
  const degree = radianToDegree(radian)
  const startRotate = (quadrant === 1 || quadrant === 2 || quadrant === 10) ? 180 : 0
  const endRotate = startRotate - degree


  const rotateProps = useSpring({
    from: { rotateZ: startRotate },
    to: { rotateZ: endRotate },
    config: { duration: 800 },
    ref: rotateRef,
  });
  const shootProps = useSpring({
    from: { x: startX, y: startY },
    to: { x: endX, y: endY },
    config: { duration: 130 },
    ref: shootRef,
  });
  const disappearProps = useSpring({
    from: {opacity: 1},
    to: { opacity: 0 },
    config: { duration: 600 },
    ref: disappearRef,
  })
  useChain([rotateRef, shootRef, disappearRef]);

  return (
    <animated.div
      style={{
        ...rotateProps,
        ...shootProps,
        ...disappearProps,
        position: "absolute",
        zIndex: 2,
      }}
    >
      <img
        src={swordImg}
        alt="sword"
        style={{ width: "6vmin", height: "18vmin" }}
      />
    </animated.div>
  );
  
}
