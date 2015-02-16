import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");
        String user = req.getParameter("user").replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("&", "&amp;")
                .replace("\"", "&#34;")
                .replace("\'", "&#34;");
        if (!user.equals("") && !UserMap.getInstance().hasUser(user)) {
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            boolean colorFound = false;
            for (Color c : Color.values()) {
                if (c.name().equals(req.getParameter("color"))) {
                    colorFound = true;
                    break;
                }
            }
            if (colorFound) {
                session.setAttribute("color", Color.valueOf(req.getParameter("color")));
                resp.sendRedirect("chat.jsp");
            } else {
                req.setAttribute("message", "Введён неверный цвет.");
                req.setAttribute("colors", Color.values());
                req.getRequestDispatcher("login.jsp").forward(req, resp);
            }
        } else {
            req.setAttribute("message", "Пользователь с данным именем уже существует.");
            req.setAttribute("colors", Color.values());
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}