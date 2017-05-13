<%@ page import="com.nplekhanov.musix.Attitude" %>
<%@ page import="com.nplekhanov.musix.Opinion" %>
<%@ page import="com.nplekhanov.musix.OpinionTrack" %>
<%@ page import="com.nplekhanov.musix.Repository" %>
<%@ page import="static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4" %>
<%@ page import="com.nplekhanov.musix.ShowMode" %>
<%@ page import="com.nplekhanov.musix.User" %>
<%@ page import="java.util.Collection" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Мнения</title>
    <jsp:include page="mobile.jsp"/>
    <link rel="stylesheet" href="common.css" type="text/css"/>
</head>
<style>
    .DESIRED {
        background-color: rgba(0, 255, 0, 0.25);
    }
    .ACCEPTABLE {
        background-color: rgba(0, 0, 255, 0.12);
    }
    .UNACCEPTABLE {
        background-color: rgba(255, 0, 0, 0.25);
    }

    .rate0 {
        background-color:  rgba(0, 0, 0, 0.0);
    }

    .rate1 {
        background-color:  rgba(0, 0, 0, 0.07);
    }

    .rate2 {
        background-color:  rgba(0, 0, 0, 0.0);
    }

    .rate3 {
        background-color:  rgba(0, 0, 0, 0.07);
    }

    .rate4 {
        background-color:  rgba(0, 0, 0, 0.0);
    }

    .rate5 {
        background-color:  rgba(0, 0, 0, 0.07);
    }
</style>
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
    Repository repository = (Repository) application.getAttribute("repository");
    String userId = (String) session.getAttribute("userId");

    String showModeText = request.getParameter("showMode");
    ShowMode showMode = showModeText == null ? ShowMode.NON_VOTED_FIRST : ShowMode.valueOf(showModeText);

    Collection<User> users = repository.getDefaultBand();
    Collection<OpinionTrack> tracks = OpinionTrack.aggregate(users);
    tracks = showMode.sort(tracks, userId);
%>
<body>
<jsp:include page="header.jsp"/>
<div>
    <form action="rate.jsp">
        <label>
            Новый трек
            <input name="track"/>
        </label>
        <input type="submit" value="Добавить"/>
    </form>
</div>
<div>
    <a href="?showMode=<%=escapeHtml4(ShowMode.DESIRED_FIRST.toString())%>">Лучшие сверху</a>
    <a href="?showMode=<%=escapeHtml4(ShowMode.NON_VOTED_FIRST.toString())%>">Непроголосованные сверху</a>
</div>
    <table>
        <tr>
            <th>Трек</th>
            <th>Рейтинг</th>
            <%
                for (User u: users) {
                %> <th> <img width="64" src="<%=u.getPhotoUrl()%>" title="<%=escapeHtml4(u.getFullName())%>"/> </th> <%
            }
            %>
        </tr>
        <%
        for (OpinionTrack t: tracks) {
            int desired = 0;
            int unacceptable = 0;
            for (Opinion o: t.getOpinionByUser().values()) {
                if (o.getAttitude() == Attitude.DESIRED) {
                    desired ++;
                }
                if (o.getAttitude() == Attitude.UNACCEPTABLE) {
                    unacceptable ++;
                }
            }
            %>
            <tr>
                <td style="text-align: left">
                    <a href="rate.jsp?track=<%=escapeHtml4(t.getTrack())%>"><%=escapeHtml4(t.getTrack())%></a>
                </td>
                <% if (unacceptable == 0 && t.getOpinionByUser().size() == users.size()) {
                    %><td class="rate<%=desired%>"><%=desired%></td> <%
                } else {
                    %><td></td> <%
                }%>
                <%
                    for (User user: users) {
                        Opinion o = t.getOpinionByUser().get(user.getUid());
                        if (o != null) {
                            %>
                                <td class="<%=o.getAttitude()%>" title="<%=attitudeLabel(o.getAttitude())%>">
                                    <% if (o.isNotEnoughSkills()) {
                                    %> <img src="Error-16.png" alt="(Too hard)" title="Недостаточно навыка"/> <%
                                    }%>
                                    <%=o.getComment() == null ? "" : escapeHtml4(o.getComment())%>
                                </td>
                            <%
                        } else {
                            %>
                                <td title="Не проголосовал">
                                </td>
                            <%
                        }
                    }
                %>
            </tr>
            <%
        }
        %>
    </table>

<table>
    <%
    for (Attitude a: Attitude.values()) {
        %> <tr>
            <td class="<%=a%>"></td>
            <td><%=attitudeLabel(a)%></td>
        </tr> <%
    }
    %>

</table>
</body>
</html>
