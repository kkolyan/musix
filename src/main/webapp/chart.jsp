<%@ page import="com.nplekhanov.musix.Composition" %>
<%@ page import="com.nplekhanov.musix.Rating" %>
<%@ page import="com.nplekhanov.musix.RatingDetail" %>
<%@ page import="com.nplekhanov.musix.Repository" %>
<%@ page import="com.nplekhanov.musix.Role" %>
<%@ page import="static org.apache.commons.lang3.StringEscapeUtils.*" %>
<%@ page import="com.nplekhanov.musix.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.time.LocalDate" %><%--
  Created by IntelliJ IDEA.
  User: nplekhanov
  Date: 2/19/2017
  Time: 4:31 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="mobile.jsp"/>
    <title>Title</title>
    <link rel="stylesheet" href="common.css" type="text/css"/>
</head>
<body>
<%!

    private String roleText(Role role) {
        switch (role) {

            case DRUMS: return "Ударные";
            case LEAD_VOCAL: return "Вокал";
            case BASS: return "Бас";
            case RHYTM_GUITAR: return "Гитара";
            case SOLO_GUITAR: return "Вторая гитара";
            case KEYBOARD: return "Клавишные";
        }
        throw new IllegalArgumentException(role+"");
    }
    private String roleForDetail(Role role) {
        switch (role) {
            case LISTEN: return "исполнять";
            case DRUMS: return "играть на ударных";
            case LEAD_VOCAL: return "петь";
            case BASS: return "играть на басу";
            case RHYTM_GUITAR: return "играть на гитаре";
            case SOLO_GUITAR: return "играть на второй гитаре";
            case KEYBOARD: return "играть на клавишных";
        }
        throw new IllegalArgumentException(role+"");
    }

    private String ratingForDetail(Rating rating) {
        switch (rating) {
            case DISLIKE: return "не хочет";
            case LIKE: return "хочет";
            case VERY_LIKE: return "очень хочет";
        }
        throw new IllegalStateException(""+rating);
    }
%>
<%
    Repository repository = (Repository) application.getAttribute("repository");
    String chartName = request.getParameter("chartName");
    if (chartName == null) {
        chartName = "";
    }
    List<Composition> compositions = repository.calculateChart(chartName);
    Map<String, User> users = repository.getUsers();
%>
<jsp:include page="header.jsp"/>
<h4>
    <%
        if (!chartName.isEmpty()) {

            %><%=escapeHtml4(chartName)%>  <%
        } else {
            %>Рабочий чарт<%
        }
    %>
</h4>
<table>
    <tr>
        <th>Трэк</th>
        <th>Рейтинг</th>
        <%
        for (Role role: Role.values()) {
            if (role == Role.LISTEN) {
                continue;
            }
            %> <th><%=roleText(role)%></th> <%
        }
        %>
    </tr>
    <%
    for (Composition comp: compositions) {
        Collection<String> ratingDetails = new ArrayList<>();
        for (RatingDetail detail: comp.getRatingDetails()) {
            ratingDetails.add(users.get(detail.getUserId()).getFullName()+" "+ratingForDetail(detail.getRating())+" "+roleForDetail(detail.getRole()));
        }
        Collection<String> usersRatedFullNames = new ArrayList<>();
        for (String userId: comp.getUsersRated()) {
            usersRatedFullNames.add(users.get(userId).getFullName());
        }
        %> <tr>
            <td><%=escapeHtml4(comp.getTrack())%></td>
            <td title="Проголосовали: <%=escapeHtml4(String.join(", ", usersRatedFullNames)+"\n\n")%><%=escapeHtml4(String.join("\n", ratingDetails))%>">
                <%=String.format("%.02f", (1.0 * comp.getRating() / comp.getUsersRated().size()))%>
                (
                <%=comp.getRating()%>
                /

                <%=comp.getUsersRated().size()%>
                )

            </td>
         <%
        for (Role role: Role.values()) {
            if (role == Role.LISTEN) {
                continue;
            }
            %> <td>
                <%
                    Set<String> userIds = comp.getUserIdsByRole().get(role);
                    if (userIds != null) {
                        for (String userId: userIds) {
                            User user = users.get(userId);
                            %> <p class="member"><%=escapeHtml4(user.getFullName())%></p> <%
        }
                    }
                %>
            </td> <%
        }
        %> </tr> <%
    }
    %>
</table>
<p>
    <b>Расчет рейтинга</b><br/>
    Рейтинги всех пользователей суммируруются и делятся на кол-во проголосовавших пользователей.<br/>
    Рейтинг от каждого пользователя - сумма рейтинга по инструментам и рейтинга по "слушать". <br/>
    По инструментам рейтинг не суммируется, а берется лучший.<br/>
    "Хочу": +1<br/>
    "Очень хочу": +2<br/>
    "Не хочу": -2 (для инуструментов не учитывается)<br/>

</p>
<% if (chartName.isEmpty()) {

    %>
    <form action="FixChart">
        <label>
            <input name="chartName" value="<%=escapeHtml4(LocalDate.now().toString())%>"/>
        <input type="submit" value="Создать"/>
            чарт с таким списком треков
        </label>
    </form>
    <%
}%>
</body>
</html>
