import java.io.IOException;
import java.util.Date;

import javax.json.Json;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/echo", configurator = GetHttpSessionConfigurator.class)
public class ChatServer {

    @OnOpen
    public void onOpen(Session session, EndpointConfig config){
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        String user = ((String) httpSession.getAttribute("user"));
        if (!session.isOpen() || user != null) {
            Color color = (Color) httpSession.getAttribute("color");
            session.getUserProperties().put("user", user);
            broadcast(session, Json.createObjectBuilder().add("a", Json.createObjectBuilder().add("u", user).add("c", color.name().toLowerCase())).build().toString());
            UserMap.getInstance().addUser(user, color);
            try {
                session.getBasicRemote().sendText(UserMap.getInstance().getJson());
                session.getBasicRemote().sendText(History.getInstance().getMessages());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                session.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @OnMessage
    public void onMessage(String message, Session session){
        message = message.replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("&", "&amp;")
                .replace("\"", "&#34;")
                .replace("\'", "&#34;");
        if (!message.equals("")) {
            String user = (String) session.getUserProperties().get("user");
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setDate(new Date());
            chatMessage.setUser(user);
            chatMessage.setMessage(message);
            History.getInstance().putMessage(chatMessage);
            broadcast(session, Json.createObjectBuilder().add("m", chatMessage.asJsonObjectBuilder()).build().toString());
        }
    }

    @OnClose
    public void onClose(Session session){
        String user = (String) session.getUserProperties().get("user");
        if (user != null) {
            UserMap.getInstance().removeUser(user);
            broadcast(session, Json.createObjectBuilder().add("q", (String) session.getUserProperties().get("user")).build().toString());
        }
        System.out.println("Session " + session.getId() + " has ended");
    }

    private void broadcast(Session session, String message) {
        try {
            for (Session s : session.getOpenSessions()) {
                if (s.isOpen()) {
                    s.getBasicRemote().sendText(message);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}