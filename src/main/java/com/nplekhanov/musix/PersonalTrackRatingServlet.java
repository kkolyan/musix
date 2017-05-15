package com.nplekhanov.musix;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by nplekhanov on 5/14/2017.
 */
public class PersonalTrackRatingServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String track = req.getParameter("track");
        String step = req.getParameter("step");

        String userId = (String) req.getSession().getAttribute("userId");
        Musix musix = (Musix) req.getServletContext().getAttribute("musix");

        musix.increaseTrackOrder(userId, track, Integer.parseInt(step));

        String s = URLEncoder.encode(track, "utf-8");
        resp.sendRedirect("personal_chart_frame_content.jsp?highlight="+ s);
    }
}
