import React, { useEffect } from 'react';
import styled from 'styled-components';
import { Link } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import api from '../../api/api';
import { over, Client } from "stompjs";
import SockJS from "sockjs-client";
import { setStompClient } from '../../_slice/websocketSlice';

const Header = styled.nav`
  position: sticky;
  top: 0;
  left: 0;
  right: 0;
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(2px);
  z-index: 2;
`;
const StyledMenu = styled.div`
  display: flex;
  justify-content: space-between;
  background: transparent;
  height: 70px;
  line-height: 70px;
  margin: 0 30px;
  color: #fff;
  font-family: Poppins, sans-serif;
`;
const StyledNav = styled.div`
  display: flex;
  justify-content: space-around;
`;
const Logo = styled.div``;
const Navigation = styled.div`
  float: left;
  height: 70px;
  margin-left: 30px;
  > a {
    display: inline-block;
    height: 65px;
    margin-right: 25px;
    text-decoration: none;
    font-weight: 700;
    font-size: 18px;
    color: black;
  }
  > button {
    height: 60px;
    border: none;
    background-color: transparent;
    font-weight: 700;
    font-size: 18px;
  }
  > button:hover,
  a:hover {
    border-bottom: 5px solid;
    transition-property: border-bottom;
    transition-duration: 0.2s;
    cursor: pointer;
  }
  > a:visited {
    color: black;
    text-decoration: none;
  }
`;
export default function NavBar() {
  const dispatch = useDispatch();
  const stompClient = useSelector(state=>state.websocket.stompClient);
  React.useEffect(()=>{
    const loginToken = window.localStorage.getItem("loginToken");
    if(stompClient === null && loginToken){
      //  && loginToken!==undefined && loginToken!=="undefined" && loginToken!==null && loginToken!=="null"){
      const Sock = new SockJS(`${api.websocket()}?token=${window.localStorage.getItem("loginToken")}`);
      let tempClient = over(Sock);
      console.log(tempClient);
      dispatch(setStompClient(tempClient));
    }
  }, [window.localStorage.getItem("loginToken")]);
  return (
    <Header>
      <StyledMenu>
        <StyledNav>
          <Logo>
            {/* <Link to="/"><img src={logo} alt="logo" height='70px'/></Link> */}
            <Link to="/">Logo</Link>
          </Logo>
          <Navigation>
            <Link to="/">The Knight</Link>
            <Link to="/info">소개</Link>
            <Link to="/lobby">로비</Link>
            <Link to="/rank">랭킹</Link>
            <Link to="/login">test Login</Link>
            <Link to="/makeroom">test makeRoom</Link>
          </Navigation>
        </StyledNav>
        <Navigation>
          <Link to={`/game`}>진행중인 게임</Link>
          {/* <Link to={`/friendList`}>친구목록</Link> */}
          <Link to={`/memberPage`}>마이페이지</Link>
          {/* <button type="button" onClick={handdleLogout}>Logout</button> */}
        </Navigation>
        {/* {isLogin ? (
          <Navigation>
            <Link to={`/memberPage/${walletAddress}`}>진행중인 게임</Link>
            <Link to={`/memberPage/${walletAddress}`}>친구목록</Link>
            <Link to={`/memberPage`}>마이페이지</Link>
            <button type="button" onClick={handdleLogout}>
              Logout
            </button>
          </Navigation>
        ) : (
          <Navigation>
            <Link to={`/login`}>로그인</Link>
          </Navigation>
        )} */}
      </StyledMenu>
    </Header>
  );
}