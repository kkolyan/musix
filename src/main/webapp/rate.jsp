<%@ page import="com.nplekhanov.musix.Repository" %>
<%@ page import="com.nplekhanov.musix.User" %>
<%@ page import="com.nplekhanov.musix.TrackInfo" %>
<%@ page import="static org.apache.commons.lang3.StringEscapeUtils.*" %>
<%@ page import="com.nplekhanov.musix.Role" %>
<%@ page import="com.nplekhanov.musix.Rating" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.nplekhanov.musix.Attitude" %>
<%@ page import="com.nplekhanov.musix.Opinion" %>
<%@ taglib prefix="mx" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="mobile.jsp"/>
    <title>Musix</title>
    <link rel="stylesheet" href="common.css" type="text/css"/>
</head>
<body>
<jsp:include page="header.jsp"/>
<%!
    private String attitudeLabel(Attitude a) {
        switch (a) {

            case DESIRED:
                return "Поддерживаю";
            case ACCEPTABLE:
                return "Согласен";
            case UNACCEPTABLE:
                return "Протестую";
        }
        throw new IllegalArgumentException(a+"");
    }
%>
<%

    String userId = (String) session.getAttribute("userId");
    Repository repository = (Repository) application.getAttribute("repository");
    User user = repository.getUser(userId);

    String track = request.getParameter("track");

    Opinion lastOpinion = user.getOpinionByTrack().get(track);
    Attitude lastAttitude = null;
    String lastComment = "";
    boolean lastNotEnoughSkills = false;
    if (lastOpinion != null) {
        lastAttitude = lastOpinion.getAttitude();
        lastComment = lastOpinion.getComment();
        lastNotEnoughSkills = lastOpinion.isNotEnoughSkills();
    }
%>
<%
if (track != null) {
    %>
    <b><%=escapeHtml4(track)%></b>
    <div>
        <form action="Rate" method="post">
            <input type="hidden" name="track" value="<%=escapeHtml4(track)%>"/>

            <fieldset>
                <legend> <b>Хотели бы включить в репертуар?</b> </legend>
                <div>
                    <%
                        for (Attitude a: Attitude.values()) {
                    %>
                    <label class="radio-option">
                        <input type="radio" name="attitude" value="<%=a%>"
                                <% if (lastAttitude == a) {%> checked <%} %>
                        />
                        <span><%=attitudeLabel(a)%></span>
                    </label>
                    <%
                        }
                    %>
                    <label>
                        Пояснение
                        <input name="comment" value="<%=escapeHtml4(lastComment)%>"/>
                    </label>
                    <label>
                        <input type="checkbox" name="notEnoughSkills" <%if (lastNotEnoughSkills) {%> checked <%}%>/>
                    </label>
                </div>
            </fieldset>

            <input type="submit" value="Проголосовать"/>
        </form>
    </div>
    <%
} else {
    %>

    <%
}
%>
</body>
</html>
