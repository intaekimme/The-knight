import React from "react";
import MakeRoom from "../../functions/lobby/MakeRoom";
export default function TestMakeRoom(){
  const testRoomInfo = {
    memberId: window.localStorage.getItem("memberId"),
    title: "방제목",
    maxMember: 4,
    currentUser: 0,
    // capacity: 4,
    // participant: 0,
    sword: 1,
    twin: 1,
    shield: 1,
    hand: 1,
  }
  // const onClick = ()=>{
  //   MakeRoom(testRoomInfo);
  // }
  return (
    // <button onClick={MakeRoom}>makeRoom</button>
    <MakeRoom roomInfo={testRoomInfo}/>
  );
}