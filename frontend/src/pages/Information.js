import React from "react";
import Info from '../components/info/Info'

import styled from '../_css/Info.module.css'

export default function Information() {
  return (
    <div className={styled.imgInfo}>
      <Info />
    </div>
  );
}