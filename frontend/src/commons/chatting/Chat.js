import React, { useEffect, useState } from "react";
import { over } from "stompjs";
import SockJS from "sockjs-client";

var stompClient = null;
export default function Chat(){
  const [privateChats, setPrivateChats] = useState(new Map());
  const [publicChats, setPublicChats] = useState([]);
  const [tab, setTab] = useState("CHATROOM");
  const [userData, setUserData] = useState({
    sender: "",
    receiver: "",
    connected: false,
    message: "",
  });
  useEffect(() => {
    console.log(userData);
  }, [userData]);
  const connect = () => {
    // config/WebsocketConfig.java registerStompEndpoints 를 pub로 설정했기 때문에 마지막에 ws 있어야함
    // let Sock = new SockJS("https://j7a301.p.ssafy.io/pub");
    let Sock = new SockJS("http://localhost:8080/pub");
    stompClient = over(Sock);
    stompClient.connect({}, onConnected, onError);
  };

  const onConnected = () => {
    setUserData({ ...userData, connected: true });
    stompClient.subscribe("/chatroom/public", onMessageReceived);
    stompClient.subscribe("/user/" + userData.sender + "/private",onPrivateMessage);
    userJoin();
  };

  const userJoin = () => {
    var chatMessage = {
      sender: userData.sender,
      status: "JOIN",
    };
    stompClient.send("/app/message", {}, JSON.stringify(chatMessage));
  };

  const onMessageReceived = (payload) => {
    var payloadData = JSON.parse(payload.body);
    switch (payloadData.status) {
      case "JOIN":
        if (!privateChats.get(payloadData.sender)) {
          privateChats.set(payloadData.sender, []);
          setPrivateChats(new Map(privateChats));
        }
        break;
      case "MESSAGE":
        publicChats.push(payloadData);
        setPublicChats([...publicChats]);
        break;
    }
  };

  const onPrivateMessage = (payload) => {
    console.log(payload);
    var payloadData = JSON.parse(payload.body);
    if (privateChats.get(payloadData.sender)) {
      privateChats.get(payloadData.sender).push(payloadData);
      setPrivateChats(new Map(privateChats));
    } else {
      let list = [];
      list.push(payloadData);
      privateChats.set(payloadData.sender, list);
      setPrivateChats(new Map(privateChats));
    }
  };

  const onError = (err) => {
    console.log(err);
  };

  const handleMessage = (event) => {
    const { value } = event.target;
    setUserData({ ...userData, message: value });
  };
  const sendValue = () => {
    if (stompClient) {
      var chatMessage = {
        sender: userData.sender,
        message: userData.message,
        status: "MESSAGE",
      };
      console.log("public", chatMessage);
      stompClient.send("/app/message", {}, JSON.stringify(chatMessage));
      setUserData({ ...userData, message: "" });
    }
  };

  const sendPrivateValue = () => {
    if (stompClient) {
      var chatMessage = {
        sender: userData.sender,
        receiver: tab.toString(),
        message: userData.message,
        status: "MESSAGE",
      };

      if (userData.sender !== tab) {
        privateChats.get(tab).push(chatMessage);
        setPrivateChats(new Map(privateChats));
      }
      console.log("private", chatMessage);
      stompClient.send("/app/private-message", {}, JSON.stringify(chatMessage));
      setUserData({ ...userData, message: "" });
    }
  };

  const handleUsername = (event) => {
    const { value } = event.target;
    setUserData({ ...userData, sender: value });
  };

  const registerUser = () => {
    connect();
  };
  return (
    <div className="container">
      {userData.connected ? (
        <div className="chat-box">
          <div className="member-list">
            <ul>
              <li onClick={() => { setTab("CHATROOM"); }} className={`member ${tab === "CHATROOM" && "active"}`}>
                Chatroom
              </li>
              {[...privateChats.keys()].map((name, index) => (
                <li onClick={() => { setTab(name); }} className={`member ${tab === name && "active"}`} key={index}>
                  {name}
                </li>
              ))}
            </ul>
          </div>
          {tab === "CHATROOM" && (
            <div className="chat-content">
              <ul className="chat-messages">
                {publicChats.map((chat, index) => (
                  <li className={`message ${chat.sender === userData.sender && "self"}`} key={index}>
                    {chat.sender !== userData.sender && (
                      <div className="avatar">{chat.sender}</div>
                    )}
                    <div className="message-data">{chat.message}</div>
                    {chat.sender === userData.sender && (
                      <div className="avatar self">{chat.sender}</div>
                    )}
                  </li>
                ))}
              </ul>

              <div className="send-message">
                <input type="text" className="input-message"
                placeholder="enter the message" value={userData.message} onChange={handleMessage}/>
                <button type="button" className="send-button" onClick={sendValue}>send</button>
              </div>
            </div>
          )}
          {tab !== "CHATROOM" && (                                          
            <div className="chat-content">
              <ul className="chat-messages">
                {[...privateChats.get(tab)].map((chat, index) => (
                  <li className={`message ${chat.sender === userData.sender && "self"}`} key={index}>
                    {chat.sender !== userData.sender && (
                      <div className="avatar">{chat.sender}</div>
                    )}
                    <div className="message-data">{chat.message}</div>
                    {chat.sender === userData.sender && (
                      <div className="avatar self">{chat.sender}</div>
                    )}
                  </li>
                ))}
              </ul>

              <div className="send-message">
                <input type="text" className="input-message"
                placeholder="enter the message" value={userData.message} onChange={handleMessage}/>
                <button type="button" className="send-button" onClick={sendPrivateValue}>send</button>
              </div>
            </div>
          )}
        </div>
      ) : (
        <div className="register">
          <input id="user-name" name="userName" margin="normal"
          placeholder="Enter your name" value={userData.sender} onChange={handleUsername}/>
          <button type="button" onClick={registerUser}>connect</button>
        </div>
      )}
    </div>
  );
};