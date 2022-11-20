import { useEffect } from "react";
import { useSelector } from "react-redux";
import { useSpring, useChain, useSpringRef, animated } from "react-spring";
import swordImg from "../../../_assets/game/image/sword.png";
import swordUpImg from "../../../_assets/game/image/sword-up.png";
import swordSound from "../../../_assets/game/sound/sound-sword.mp3"

export default function Sword(props) {
  const playersDOM = useSelector((state) => state.game.playersDOM);
  const swordAudio = new Audio(swordSound);

  const vmin =
    (window.innerHeight > window.innerWidth
      ? window.innerWidth
      : window.innerHeight) / 100;
  const swordHeight = 18 * vmin;
  const width = playersDOM[props.from.toString()].width;
  const height = playersDOM[props.from.toString()].height;
  let startX = playersDOM[props.from.toString()].x + width / 2 - 3 * vmin;
  let startY = playersDOM[props.from.toString()].y;
  let endX = playersDOM[props.to.toString()].x + width / 2 - 3 * vmin;
  let endY = playersDOM[props.to.toString()].y;

  const rotateDuration = 500
  const shootDuration = 300
  const disappearDuration = 600

  function radianToDegree(x) {
    return (x * 180) / Math.PI;
  }

  let quadrant = null;
  if (startX < endX && startY > endY) {
    quadrant = 1;
  } else if (startX > endX && startY > endY) {
    quadrant = 2;
  } else if (startX > endX && startY < endY) {
    quadrant = 3;
  } else if (startX < endX && startY < endY) {
    quadrant = 4;
  } else if (startX === endX && startY < endY) {
    quadrant = 9;
  } else if (startX === endX && startY > endY) {
    quadrant = 10;
  }

  const isMyTeamAttack = quadrant === 1 || quadrant === 2 || quadrant === 10;
  if (isMyTeamAttack) {
    startY = startY - 0.5 * height;
    endY = endY + 0.25 * height;
  } else {
    startY = startY + 0.5 * height;
    endY = endY - 0.2 * height;
  }

  let adjustedEndX
  let adjustedEndY

  if (quadrant === 1) {
    adjustedEndX = endX - 9 * vmin;
    adjustedEndY = endY
  } else if (quadrant === 2) {
    adjustedEndX = endX + 9 * vmin;
    adjustedEndY = endY
  } else if (quadrant === 3) {
    adjustedEndX = endX + 9 * vmin;
    adjustedEndY = endY - 9 * vmin / Math.abs(endX - startX) * (endY - startY);
  } else if (quadrant === 4) {
    adjustedEndX = endX - 9 * vmin;
    adjustedEndY = endY - 9 * vmin / Math.abs(endX - startX) * (endY - startY);
  }

  const rotateRef = useSpringRef();
  const shootRef = useSpringRef();
  const disappearRef = useSpringRef();

  // 각도 계산
  let radian = Math.atan((adjustedEndX - startX) / (adjustedEndY - startY));
  let degree = radianToDegree(radian);
  let startRotate = 0;
  let endRotate = startRotate - degree;

  const rotateProps = useSpring({
    from: { rotateZ: startRotate },
    to: { rotateZ: endRotate },
    config: { duration: rotateDuration },
    ref: rotateRef,
  });
  const shootProps = useSpring({
    from: { x: startX, y: startY },
    to: { x: adjustedEndX, y: adjustedEndY },
    ...(startRotate === endRotate && { delay: rotateDuration }),
    config: { duration: shootDuration },
    ref: shootRef,
  });
  const disappearProps = useSpring({
    from: { opacity: 1 },
    to: { opacity: 0 },
    config: { duration: disappearDuration },
    ref: disappearRef,
  });
  useChain([rotateRef, shootRef, disappearRef]);

  useEffect(() => {
    setTimeout(() => {
      swordAudio.play();
    }, rotateDuration)
  }, [])

  return (
    <animated.div
      style={{
        ...rotateProps,
        ...shootProps,
        ...disappearProps,
        position: "absolute",
        zIndex: 3,
      }}
    >
      <img
        src={isMyTeamAttack ? swordUpImg : swordImg}
        alt="sword"
        style={{ width: "6vmin", height: "18vmin" }}
      />
    </animated.div>
  );
}
