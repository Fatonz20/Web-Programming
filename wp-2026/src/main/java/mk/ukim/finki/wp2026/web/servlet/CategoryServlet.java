package mk.ukim.finki.wp2026.web.servlet;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CategoryServlet", urlPatterns = {"/servlet/category"})
public class CategoryServlet extends HttpServlet {
    private final SpringTemplateEngine springTemplateEngine;

    //in-memory list of categories
    private List<Category> categories = null;

    public CategoryServlet(SpringTemplateEngine springTemplateEngine) {
        this.springTemplateEngine = springTemplateEngine;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    class Category {
        private String name;
        private String description;
    }

    private void addCategory(String name, String description) {
        if (name == null || name.isBlank() || description == null || description.isBlank()) {
            return;
        }

        categories.add(new Category(name, description));
    }

    @Override
    public void init() throws ServletException {
        categories = new ArrayList<>();

        addCategory("Movies", "Movies category");
        addCategory("Books", "Books category");

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);

        WebContext context = new WebContext(webExchange);


        context.setVariable("ipAddress", req.getRemoteAddr());
        context.setVariable("userAgent", req.getHeader("user-agent"));
        context.setVariable("categories", categories);

        springTemplateEngine.process("categories", context, resp.getWriter());
    }
}
