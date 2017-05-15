<%@ page import="com.nplekhanov.musix.Attitude" %>
<%@ page import="com.nplekhanov.musix.Opinion" %>
<%@ page import="com.nplekhanov.musix.OpinionTrack" %>
<%@ page import="com.nplekhanov.musix.Musix" %>
<%@ page import="static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4" %>
<%@ page import="com.nplekhanov.musix.User" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Мнения</title>
    <jsp:include page="mobile.jsp"/>
    <link rel="stylesheet" href="common.css" type="text/css"/>
</head>
<style>
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
    Musix musix = (Musix) application.getAttribute("musix");

    Collection<User> users = musix.getDefaultBand();
    Collection<OpinionTrack> tracks = musix.getTracksWithOpinions();
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
    <table>
        <tr>
            <th>Трек</th>
            <th>Желающих / Рейтинг</th>
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
                    Collection<String> ratedFullNames = new ArrayList<>();
                    double subRate = 0;
                    for (User user: t.getRatedWithinGroup().keySet()) {
                        double rate = t.getRatedWithinGroup().get(user);
                        ratedFullNames.add(user.getFullName()+": "+ String.format("%.02f", rate));
                        subRate += rate;
                    }
                    %><td class="rate<%=desired%>" title="<%=String.join("\n", ratedFullNames)%>"><%=desired%> / <%=String.format("%.02f", subRate)%> </td> <%
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
