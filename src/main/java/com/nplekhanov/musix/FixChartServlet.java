package com.nplekhanov.musix;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by nplekhanov on 21/02/2017.
 */
public class FixChartServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String chartName = req.getParameter("chartName");
        Repository repository = (Repository) req.getServletContext().getAttribute("repository");
        repository.fixChart(chartName);

        resp.sendRedirect("chart.jsp?chartName="+URLEncoder.encode(chartName, "utf8"));
    }
}
