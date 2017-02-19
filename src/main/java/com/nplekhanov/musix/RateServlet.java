package com.nplekhanov.musix;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nplekhanov on 2/19/2017.
 */
public class RateServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Repository repository = (Repository) req.getServletContext().getAttribute("repository");
        String userId = (String) req.getSession().getAttribute("userId");
        String track = req.getParameter("track");
        Map<Role, Rating> ratings = new HashMap<>();
        for (Role role: Role.values()) {
            String ratingText = req.getParameter(role.name());
            ratings.put(role, Rating.valueOf(ratingText));
        }
        repository.addRating(userId, track, ratings);

        resp.sendRedirect("rate.jsp");
    }
}
