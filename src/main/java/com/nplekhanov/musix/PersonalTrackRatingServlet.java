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
        boolean onlyDesired = "true".equals(req.getParameter("onlyDesired"));
        boolean allTracks = "true".equals(req.getParameter("allTracks"));

        String userId = (String) req.getSession().getAttribute("userId");
        Musix musix = (Musix) req.getServletContext().getAttribute("musix");

        if (onlyDesired) {
            musix.increaseTrackOrderOfDesired(userId, Integer.parseInt(step));
        } else if (allTracks) {
            musix.increaseTrackOrderOfAll(userId, Integer.parseInt(step));
        } else {
            musix.increaseTrackOrder(userId, track, Integer.parseInt(step));
        }

        String s= "";
        if (track != null) {
            s = URLEncoder.encode(track, "utf-8");
        }
        resp.sendRedirect("personal_chart_frame_content.jsp?highlight="+ s);
    }
}
