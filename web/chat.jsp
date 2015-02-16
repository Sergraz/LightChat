<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
  <link href='http://fonts.googleapis.com/css?family=Open+Sans:400,600,800italic&subset=latin,cyrillic,cyrillic-ext' rel='stylesheet' type='text/css'>
  <link href="chat.css" rel="stylesheet" type="text/css">
  <title>Light Chat</title>
</head>

<body>
  <div id="content">
    <div id="chat" style="display:none;">
      <div id="hello"><span>Hello, ${user}!</span><button type="button" onclick="logout();" >Выход</button></div>
      <div id="messages"></div>
      <div id="usersBox">
        <span>Сейчас в чате:</span>
        <div id="users"></div>
      </div>
      <div id="input">
        <span>Написать:</span>
        <input type="text" id="messageinput"/>
        <button type="button" onkeydown="if (event.keyCode == 13) send();" onclick="send();" >Отправить</button>
      </div>
    </div>
    <div id="reconnect">
      Соединение потеряно. <button type="button" onclick="logout();" >Переподключиться</button>
    </div>
  </div>
  <script type="text/javascript">
    var colors =  ${colors};
    var webSocket;
    var users;
    var messages = document.getElementById("messages");
    var usersDiv = document.getElementById("users");
    var input = document.getElementById("messageinput");

    input.onkeydown= function(event) {
      if (event == undefined) {
        event = window.event;
      }

      if (event.keyCode == 13) {
        send();
      }
    };

    function addMessage(message, color) {
      var row = document.createElement('div');
      row.setAttribute('class', 'message ' + color);
      var dateSpan = document.createElement('span');
      dateSpan.setAttribute('class', 'date');
      dateSpan.innerHTML = message.d;
      var usernameSpan = document.createElement('span');
      usernameSpan.setAttribute('class', 'username');
      usernameSpan.innerHTML = message.u + ':';
      var messageSpan = document.createElement('span');
      messageSpan.setAttribute('class', 'message');
      messageSpan.innerHTML = message.m;
      row.appendChild(dateSpan);
      row.appendChild(usernameSpan);
      row.appendChild(messageSpan);
      messages.appendChild(row);
      input.value = '';
      messages.scrollTop = messages.scrollHeight;
      input.focus();
    }

    function updateUsers() {
      usersDiv.innerHTML = '';
      for (var user in users) {
        var row = document.createElement('div');
        row.setAttribute('class', 'user ' + users[user]);
        row.innerHTML = user;
        usersDiv.appendChild(row);
      }


    }

    function openSocket(){
      if(webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED){
        return;
      }
      webSocket = new WebSocket("${socketurl}");

      webSocket.onopen = function(event){
        document.getElementById('chat').style.display = 'block';
        document.getElementById('reconnect').style.display = 'none';
      };

      webSocket.onmessage = function(event){
        var resp = JSON.parse(event.data);
        if (resp.u) {
          users = resp.u;
          updateUsers();
        } else if (users) {
          if (resp.m) {
            addMessage(resp.m, users[resp.m.u]);
          } else if (resp.h) {
            for (var i = 0; i < resp.h.length; i++) {
              addMessage(resp.h[i], 'gray');
            }
          } else if (resp.a) {
            users[resp.a.u] = resp.a.c;
            updateUsers();
          } else if (resp.q) {
            delete users[resp.q];
            updateUsers();
          }
        }
      };

      webSocket.onclose = function(event){
        messages.innerHTML = '';
        document.getElementById('chat').style.display = 'none';
        document.getElementById('reconnect').style.display = 'block';
      };
    }
    window.onload = openSocket();

    function send(){
      if (webSocket == undefined || webSocket.readyState == WebSocket.CLOSED) {
        openSocket();
      }
      var text = document.getElementById("messageinput").value;
      webSocket.send(text);
    }

    function closeSocket(){
      webSocket.close();
    }

    function logout() {
      window.location.href = "logout";
    }
  </script>
</body>

</html>