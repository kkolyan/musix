<%@ tag import="com.nplekhanov.musix.Rating" %>
<%@ tag import="com.nplekhanov.musix.Repository" %>
<%@ tag import="com.nplekhanov.musix.Role" %>
<%@ tag import="static org.apache.commons.lang3.StringEscapeUtils.*" %>
<%@ tag import="com.nplekhanov.musix.TrackInfo" %>
<%@ tag import="com.nplekhanov.musix.User" %>
<%@ tag language="java" pageEncoding="utf-8" %>
<%@ attribute name="userId" required="true" %>
<%!


    private String roleColumn(Role role) {
        switch (role) {
            case LISTEN: return "Слушать";
            case DRUMS: return "Ударные";
            case LEAD_VOCAL: return "Вокал";
            case BASS: return "Бас";
            case RHYTM_GUITAR: return "Гитара";
            case SOLO_GUITAR: return "Вторая гитара";
            case KEYBOARD: return "Клавишные";
        }
        throw new IllegalArgumentException(role+"");
    }
    private String ratingCssClass(Rating rating) {
        switch (rating) {
            case DISLIKE: return "dislike";
            case NEUTRAL: return "";
            case LIKE: return "like";
            case VERY_LIKE: return "verylike";
            case NOT_ENOUGH_SKILLS_OR_EQUIPMENT: return "cant";
        }
        throw new IllegalStateException(""+rating);
    }

    private String ratingCell(Rating rating) {
        switch (rating) {
            case DISLIKE: return "Не хочу";
            case NEUTRAL: return "Согласен";
            case LIKE: return "Хочу";
            case VERY_LIKE: return "Очень хочу";
            case NOT_ENOUGH_SKILLS_OR_EQUIPMENT: return "Не могу";
        }
        throw new IllegalStateException(""+rating);
    }
%>
<%

    Repository repository = (Repository) application.getAttribute("repository");
    User user = repository.getUser(userId);
%>
<table>
    <tr>
        <th>Трэк</th>
        <th>Всего проголосовало</th>
        <%
            for (Role role: Role.values()) {
        %> <th><%=roleColumn(role)%></th> <%
        }
    %>
    </tr>

    <%
        for (TrackInfo ti: repository.getTrackInfoList(userId)) {
    %>
    <tr>
        <td><a href="?track=<%=escapeHtml4(ti.getTrack())%>"><%=escapeHtml4(ti.getTrack())%></a></td>
        <td><%=ti.getRatedUsersCount()%></td>
        <%
            for (Role role: Role.values()) {
                if (ti.isYouRated()) {
                    Rating rating = user.getRatingByTrack().get(ti.getTrack()).get(role);
        %> <td class="<%=ratingCssClass(rating)%>"><%=ratingCell(rating)%></td> <%
    } else {
    %> <td>-</td> <%
            }
        }
    %>
    </tr>
    <%
        }
    %>
</table>