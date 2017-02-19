package com.nplekhanov.musix;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by nplekhanov on 2/19/
 * 2017.
 */
public class DumpServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Repository repository = (Repository) req.getServletContext().getAttribute("repository");
        resp.setContentType("text/javascript");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print(repository.getDump());
    }
}
