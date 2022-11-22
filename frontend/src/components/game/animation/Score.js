import { useSpring, animated } from "react-spring";

export default function Number ({from, to, color, fontSize}) {
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
      <animated.div style={{color: color, fontSize: fontSize}}>
        {number.to(n => n.toFixed(0))}
      </animated.div>
    </div>
  );
}
