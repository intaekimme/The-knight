import { useSpring, animated } from "react-spring";

export default function Number ({from, to, style}) {
  const { number } = useSpring({
    from: { number: from},
    number: to,
    delay: 1500,
    config: {
      duration: 300,
    }
});

  return (
    <div>
      <animated.div style={style}>
        {number.to(n => n.toFixed(0))}
      </animated.div>
    </div>
  );
}
