import React from "react";
import GoogleLogin from "../commons/login/GoogleLogin";

export default function Login() {
  return (
    <input type="button" onClick={GoogleLogin} value = "button"/>
  );
}