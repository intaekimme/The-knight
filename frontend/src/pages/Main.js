import React from "react";
import MainBtn from '../components/main/MainBtn'

import styled from '../_css/Main.module.css'

export default function Main() {
  return (
    <div className={styled.imgMain}>
      <MainBtn />
    </div>
  );
}