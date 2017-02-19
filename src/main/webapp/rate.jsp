<%@ page import="com.nplekhanov.musix.Repository" %>
<%@ page import="com.nplekhanov.musix.User" %>
<%@ page import="com.nplekhanov.musix.TrackInfo" %>
<%@ page import="static org.apache.commons.lang3.StringEscapeUtils.*" %>
<%@ page import="com.nplekhanov.musix.Role" %>
<%@ page import="com.nplekhanov.musix.Rating" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
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
    private String roleLabel(Role role) {
        switch (role) {

            case LISTEN: return "Слушать";
            case DRUMS: return "Играть барабанах";
            case LEAD_VOCAL: return "Петь";
            case BASS: return "Играть на басу";
            case RHYTM_GUITAR: return "Играть на ритм- или единственной гитаре";
            case SOLO_GUITAR: return "Играть на второй (соло-) гитаре, если она есть";
            case KEYBOARD: return "Играть на клавишных";
        }
        throw new IllegalArgumentException(role+"");
    }

    private String ratingLabel(Rating rating) {
        switch (rating) {
            case DISLIKE: return "Не хочу";
            case NEUTRAL: return "Согласен";
            case LIKE: return "Хочу";
            case VERY_LIKE: return "Очень хочу";
            case NOT_ENOUGH_SKILLS_OR_EQUIPMENT: return "Не хватает навыка или инструмента";
        }
        throw new IllegalStateException(""+rating);
    }
%>
<%

    String userId = (String) session.getAttribute("userId");
    Repository repository = (Repository) application.getAttribute("repository");
    User user = repository.getUser(userId);

    String track = request.getParameter("track");
%>
<%
if (track != null) {
    %>
    <b><%=escapeHtml4(track)%></b>
    <div>
        <a href="rate.jsp">Проголосовать за другой</a>
    </div>
    <div>
        <form action="Rate" method="post">
            <input type="hidden" name="track" value="<%=escapeHtml4(track)%>"/>
            <%
            Map<Role, Rating> previousRatings = user.getRatingByTrack().get(track);
            for (Role role: Role.values()) {
                Rating previousRating;
                if (previousRatings == null) {
                    previousRating = role.getDefaultRating();
                } else {
                    previousRating = previousRatings.get(role);
                }
                %>
                <fieldset>
                    <legend> <b><%=roleLabel(role)%></b> </legend>
                    <div>
                        <%
                            for (Rating rating: Rating.values()) {
                                if (!role.getSupportedRatings().contains(rating)) {
                                    continue;
                                }
                        %>
                        <label class="radio-option">
                            <input type="radio" name="<%=role%>" value="<%=rating%>"
                                    <%   if (previousRating == rating) {%> checked <%} %>
                            />
                            <span><%=ratingLabel(rating)%></span>
                        </label>
                        <%
                            }
                        %>
                    </div>
                </fieldset>
                <%
            }
            %>
            <input type="submit" value="Проголосовать"/>
        </form>
    </div>
    <%
} else {
    %>
    <div>
        <form>
            <label>
                Новый трек
                <input name="track"/>
            </label>
            <input type="submit" value="Добавить"/>
        </form>
    </div>
    <mx:myrates userId="<%=userId%>"/>

    <%
}
%>
</body>
</html>
