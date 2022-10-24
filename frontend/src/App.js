import React from 'react';
import { Outlet } from 'react-router-dom';
import { useSelector } from 'react-redux';
import NavBar from './components/header/NavBar';
import './App.css';

function App() {
  return (
    <>
      <NavBar />
      <Outlet />
    </>
  )
}

export default App;
