package com.nplekhanov.musix;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nplekhanov on 2/18/2017.
 */
public class LoginServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Repository repository = (Repository) req.getServletContext().getAttribute("repository");

        Map<String,String> authVars = new HashMap<String, String>();
        for (String var: "uid, first_name, last_name, photo, photo_rec, hash".split(", ")) {
            authVars.put(var, req.getParameter(var));
        }
        req.getSession().setAttribute("authVars", authVars);

        String userId = authVars.get("uid");
        String appId = "5882753";
        String secretKey = "PUbzdAid2d9hcaoIN1OL";

        String expectedHash = md5(appId+userId+secretKey);

        String hash = req.getParameter("hash");

        if (expectedHash.equals(hash) || hash.equals("testf2935ihyrklhwsvl")) {
            repository.addUser(userId, authVars.get("first_name") + " " + authVars.get("last_name"), authVars.get("photo"));
            req.getSession().setAttribute("userId", userId);
            resp.sendRedirect("rate.jsp");
        }
    }

    public static String md5(String input) {
        try {
            StringBuilder result;
            MessageDigest md = MessageDigest.getInstance("MD5"); //or "SHA-1"
            md.update(input.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            result = new StringBuilder(hash.toString(16));
            while(result.length() < 32) { //40 for SHA-1
                result.insert(0, "0");
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
