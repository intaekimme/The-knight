import React, { useEffect } from 'react';
import styled from 'styled-components';
import { Link } from 'react-router-dom';
import { AppBar, Avatar, Button, Grid, IconButton, Menu, MenuItem, Toolbar, Tooltip, Typography } from "@mui/material";
import Person from '@mui/icons-material/Person';
import logo from '../../_assets/logo.png'
import name from '../../_assets/name.png'

const Header = styled.nav`
  position: sticky;
  top: 0;
  left: 0;
  right: 0;
  background-image: linear-gradient(to right, white, #424242);
  // background: rgba(255, 255, 255);
  // background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(2px);
  z-index: 2;
`;
const StyledMenu = styled.div`
  display: flex;
  justify-content: space-between;
  background: transparent;
  height: 65px;
  line-height: 65px;
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
  height: 65px;
  margin-left: 30px;
  > a {
    display: inline-block;
    height: 60px;
    margin-right: 25px;
    text-decoration: none;
    font-weight: 700;
    font-size: 24px;
    color: #424242 !important;
  }
  > button {
    height: 65px;
    border: none;
    background-color: transparent;
    font-weight: 700;
    font-size: 18px;
  }
  > button:hover,
  a:hover {
    border-bottom: 5px solid #4F585B;
    transition-property: border-bottom;
    transition-duration: 0.2s;
    cursor: pointer;
  }
  > a:visited {
    color: black;
    text-decoration: none;
  }
`;

// const pages = ['Products', 'Pricing', 'Blog'];
// const settings = ['Profile', 'Account', 'Dashboard', 'Logout'];

export default function NavBar() {
  const [auth, setAuth] = React.useState(true);
  const [anchorEl, setAnchorEl] = React.useState(null);

  const handleChange = (event) => {
    setAuth(event.target.checked);
  };

  const handleMenu = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };
  return (
    <Header>
      <StyledMenu>
        <StyledNav>
          <Logo>
            {/* <Link to="/"><img src={logo} alt="logo" height='70px'/></Link> */}
            <Link to="/">
              <Grid container direction="row" alignItems="center">
                <Grid item>
                  <Avatar src={logo} sx={{ width: 65, height: 65 }} variant="square"></Avatar>
                </Grid>
                <Grid item sx={{ display: 'flex', alignItems: 'center' }}>
                  <Avatar src={name} sx={{ width: 200 }} variant="square"></Avatar>
                </Grid>
              </Grid>
            </Link>
          </Logo>
          <Navigation>
            <Link to="/info">&nbsp;&nbsp;소개&nbsp;&nbsp;</Link>
            <Link to="/lobby">&nbsp;&nbsp;로비&nbsp;&nbsp;</Link>
            <Link to="/rank">&nbsp;&nbsp;랭킹&nbsp;&nbsp;</Link>
            {/* <Link to="/login">test Login</Link> */}
            {/* <Link to="/makeroom">test makeRoom</Link>
            <Link to="/room/1">test enterRoom</Link> */}
          </Navigation>
        </StyledNav>
        <StyledNav>
          <Navigation>
            <Link to={`/game`}>진행중인 게임</Link>
            {/* <Link to={`/friendList`}>친구목록</Link> */}
            <Link to={`/memberPage`}>
              <IconButton
                edge="end"
                aria-label="account of current user"
                // aria-controls={menuId}
                aria-haspopup="true"
                onClick={handleMenu}
                color="inherit"
              >
                <Person sx={{ fontSize: 45, color: '#DCD7C9' }} />
              </IconButton>
              &nbsp;&nbsp;
            </Link>
            {/* <button type="button" onClick={handdleLogout}>Logout</button> */}
          </Navigation>
        </StyledNav>
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