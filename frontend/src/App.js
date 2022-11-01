import React from 'react';
import { Outlet } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import { resize } from "./_slice/windowSlice";
import NavBar from './components/header/NavBar';
import './App.css';

function App() {
  const windowData = useSelector((state) => state.windowData.value);
  const dispatch = useDispatch();
  React.useEffect(() => {
    window.addEventListener("resize", () => {
      dispatch(resize({ width: window.innerWidth, height: window.innerHeight }));
    });
  }, [window.innerWidth, window.innerHeight]);
  return (
    <>
      <NavBar />
      <Outlet />
    </>
  )
}

export default App;
