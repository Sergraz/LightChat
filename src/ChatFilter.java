import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "ChatFilter", urlPatterns = {"/chat.jsp"})
public class ChatFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null && session.getAttribute("color") != null) {
            servletRequest.setAttribute("colors", Color.getJson());
            servletRequest.setAttribute("user", session.getAttribute("user"));
            String curUrl = request.getRequestURL().toString();
            servletRequest.setAttribute("socketurl", "ws" + curUrl.substring("http".length(), curUrl.lastIndexOf("/chat.jsp")) + "/echo");


            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            httpResponse.sendRedirect("login.jsp");
        }
    }

    @Override
    public void destroy() {

    }
}