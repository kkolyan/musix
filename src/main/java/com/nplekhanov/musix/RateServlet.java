package com.nplekhanov.musix;

import com.nplekhanov.musix.model.Attitude;
import com.nplekhanov.musix.model.Opinion;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by nplekhanov on 2/19/2017.
 */
public class RateServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Musix musix = (Musix) req.getServletContext().getAttribute("musix");
        String userId = (String) req.getSession().getAttribute("userId");
        String track = req.getParameter("track");
        String notEnoughSkills = req.getParameter("notEnoughSkills");
        String comment = req.getParameter("comment");
        String attitude = req.getParameter("attitude");

        if (!musix.isFromDefaultBand(userId)) {
            resp.sendError(403);
            return;
        }
        Opinion opinion = new Opinion();
        opinion.setAttitude(Attitude.valueOf(attitude));
        if (opinion.getAttitude() == Attitude.DESIRED) {
            opinion.setRating(1);
        }
        opinion.setComment(comment);
        opinion.setNotEnoughSkills("on".equals(notEnoughSkills));
        musix.addOpinion(userId, track, opinion);

        resp.sendRedirect("opinions.jsp");
    }
}
