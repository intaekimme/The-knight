import React from "react";
import { useNavigate } from 'react-router-dom';
import api from "../../api/api";

export default function GoogleLogin() {
  window.location.href = `${api.login()}?redirect_uri=${api.loginRedirect()}`;
}