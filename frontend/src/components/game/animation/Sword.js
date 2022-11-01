import { useSpring, useChain, useSpringRef, animated } from "react-spring";

export default function Sword() {
  const appearRef = useSpringRef()
  const appearProps = useSpring({ from: { opacity: 0 }, to: { opacity: 1 }, config: { duration: 1500 }, ref: appearRef })
  const rotateRef = useSpringRef()
  const rotateProps = useSpring({ from: { rotateZ: 0 }, to: { rotateZ: 180 }, config: { duration: 1500 }, ref: rotateRef })
  const shootRef = useSpringRef()
  const shootProps = useSpring({ from: { x: 0 }, to: { x: 100 }, config: { duration: 1500 }, ref: shootRef })
  useChain([appearRef, rotateRef, shootRef])
  
  return (
    <animated.div style={{
      ...appearProps,
      ...rotateProps,
      ...shootProps,
      background: "#46e891",
      width: 120,
      height: 20,
    }}></animated.div>
  )
}