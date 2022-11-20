import { useSelector } from "react-redux"
import { useSpring, useChain, useSpringRef, animated } from "react-spring";
import swordImg from "../../../_assets/game/sword.png";
import swordUpImg from "../../../_assets/game/sword-up.png";

export default function Sword(props) {
  const playersDOM = useSelector((state) => state.game.playersDOM)

  const vmin = ((window.innerHeight > window.innerWidth) ? window.innerWidth : window.innerHeight) / 100
  const swordHeight = 18 * vmin
  const width = playersDOM[props.from.toString()].width
  const height = playersDOM[props.from.toString()].height
  let startX = playersDOM[props.from.toString()].x + width / 2 - 3 * vmin
  let startY = playersDOM[props.from.toString()].y
  let endX = playersDOM[props.to.toString()].x + width / 2 - 3 * vmin
  let endY = playersDOM[props.to.toString()].y
  
  function radianToDegree(x) {
    return (x * 180) / Math.PI;
  }
  
  
  // if (startY > endY) {
  //   startY -= height * 1.5
  // } else {
  //   startY -= height * 0.5
  //   endY -= height * 1.8
  // }
  
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
  
  const isMyTeamAttack = (quadrant === 1 || quadrant === 2 || quadrant === 10)
  if (isMyTeamAttack) {
    startY = startY - 0.5 * height
    endY = endY + 0.5 * height
  } else {
    startY = startY + 0.5 * height
    endY = endY - 0.5 * height
  }
  

  if (quadrant === 1) {
    endX = endX - 12 * Math.log(Math.abs(endX - startX))
    endY = endY - 6 * Math.log(Math.abs(endX - startX))
  } else if (quadrant === 2) {
    endX = endX + 12 * Math.log(Math.abs(endX - startX))
    endY = endY - 6 * Math.log(Math.abs(endX - startX))
  } else if (quadrant === 3) {
    endX = endX + 12 * Math.log(Math.abs(endX - startX))
    endY = endY + 2 * Math.log(Math.abs(endX - startX))
  } else if (quadrant === 4) {
    endX = endX - 12 * Math.log(Math.abs(endX - startX))
    endY = endY + 2 * Math.log(Math.abs(endX - startX))
  }

  const rotateRef = useSpringRef();
  const shootRef = useSpringRef();
  const disappearRef = useSpringRef();
  
  // 각도 계산
  const radian = Math.atan((endX - startX) / (endY - startY))
  const degree = radianToDegree(radian)
  const startRotate = 0
  const endRotate = startRotate - degree


  const rotateProps = useSpring({
    from: { rotateZ: startRotate },
    to: { rotateZ: endRotate },
    config: { duration: 1000 },
    ref: rotateRef,
  });
  const shootProps = useSpring({
    from: { x: startX, y: startY },
    to: { x: endX, y: endY },
    ...(startRotate === endRotate && {delay: 1000}),
    config: { duration: 300 },
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
    <div>
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
          src={isMyTeamAttack ? swordUpImg : swordImg}
          alt="sword"
          style={{ width: "6vmin", height: "18vmin" }}
        />
      </animated.div>
    </div>
  );
  
}
