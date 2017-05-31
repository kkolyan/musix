<%@ page import="com.nplekhanov.musix.Attitude" %>
<%@ page import="com.nplekhanov.musix.Opinion" %>
<%@ page import="com.nplekhanov.musix.OpinionTrack" %>
<%@ page import="com.nplekhanov.musix.Musix" %>
<%@ page import="static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4" %>
<%@ page import="com.nplekhanov.musix.User" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Мнения</title>
    <jsp:include page="mobile.jsp"/>
    <link rel="stylesheet" href="common.css" type="text/css"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <style>
        .comment-button:hover, .comment-button-all:hover {
            cursor: pointer;
        }

        img.cell-img {
            margin-top: -7px;
            margin-right: 0;
            margin-left: 0;
            margin-bottom: -8px;
            padding: 0;
        }
    </style>
    <script>
        var showAllComments = false;

        $(document).ready(function() {
            $(".comment-button").click(function() {
                $(this).next().toggle();
            });
            $(".comment-button-all").click(function() {
                showAllComments = ! showAllComments;
                if (showAllComments) {
                    $(".comment").show();
                } else {
                    $(".comment").hide();
                }
            });
        });
    </script>
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
    <img class="comment-button-all" src="chat-24.png" alt="показать/скрыть все комментарии" />
    <table>
        <tr>
            <th>Трек</th>
            <th title="Кол-во желающих / Позиция в группе с учетом рейтинга">#</th>
            <%
                for (User u: users) {
                %> <th> <img width="64" src="<%=u.getPhotoUrl()%>" title="<%=escapeHtml4(u.getFullName())%>"/> </th> <%
            }
            %>
        </tr>
        <%
        String userId = (String) session.getAttribute("userId");
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
                    <a href="rate.jsp?track=<%=URLEncoder.encode(t.getTrack(), "utf-8")%>"><%=escapeHtml4(t.getTrack())%></a>
                </td>
                <% if (unacceptable == 0 && t.getOpinionByUser().size() == users.size()) {
                    Collection<String> ratedFullNames = new ArrayList<>();
                    for (User user: t.getRatedWithinGroup().keySet()) {
                        long rate = t.getRatedWithinGroup().get(user);
                        ratedFullNames.add(user.getFullName()+": "+ rate);
                    }
                    %><!-- <td class="rate<%=desired%>" title="Рейтинги:<%="\n"+String.join("\n", ratedFullNames)%>" onclick="alert(this.title)"><%=desired%> / <%=t.getPositionInsideGroup()%> </td> --><%
                    %><td class="rate<%=desired%>"><%=desired%> / <%=t.getPositionInsideGroup()%> </td> <%
                } else {
                    %><td></td> <%
                }%>
                <%
                    for (User user: users) {
                        Opinion o = t.getOpinionByUser().get(user.getUid());
                        if (o != null) {
                            %>
                                <td class="<%=o.getAttitude()%> opinions" style="max-width: 200px" title="<%=attitudeLabel(o.getAttitude())%>">
                                    <% if (user.getUid().equals(userId) && userId.equals("37466302")) {%>
                                        <form style="display: inline" action="PersonalTrackRating" class="mini-form" method="post">
                                            <input type="hidden" name="track" value="<%=escapeHtml4(t.getTrack())%>">
                                            <input type="hidden" name="step" value="-1">
                                            <input type="hidden" name="source" value="<%=request.getRequestURI()%>">
                                            <input type="submit" value="-" class="rating-button left-round"/>
                                        </form>
                                        <form style="display: inline" action="PersonalTrackRating" class="mini-form" method="post">
                                            <input type="hidden" name="track" value="<%=escapeHtml4(t.getTrack())%>">
                                            <input type="hidden" name="step" value="1">
                                            <input type="hidden" name="source" value="<%=request.getRequestURI()%>">
                                            <input type="submit" value="+" class="rating-button right-round"/>
                                        </form>
                                    <%}%>
                                    <span class="stars"><%
                                        for (int i = 0; i < o.getRating(); i ++) {
                                    %>*<%
                                        }
                                    %></span>
                                    <% if (o.isNotEnoughSkills()) {
                                    %> <img src="Error-25.png" class="cell-img" alt="(Too hard)" title="Недостаточно навыка" /> <%
                                    }%>
                                    <%
                                        if (o.getComment() != null && !o.getComment().trim().isEmpty()) {
                                    %><img class="comment-button cell-img" src="chat-24.png" alt="comment" title="<%=escapeHtml4(o.getComment())%>"/><span style="display: none;" class="comment"><%=escapeHtml4(o.getComment())%></span>  <%
                                    }
                                %>
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
