<%@ page import="com.nplekhanov.musix.Musix" %>
<%@ page import="com.nplekhanov.musix.Opinion" %>
<%@ page import="com.nplekhanov.musix.User" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4" %>
<%@ page import="java.util.Objects" %>
<%@ page import="com.nplekhanov.musix.Attitude" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.NavigableSet" %>
<%@ page import="java.util.TreeSet" %>
<%@ page import="java.util.Arrays" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <link rel="stylesheet" href="common.css" type="text/css"/>
</head>
<%--<html>--%>
<%--<head>--%>
    <%--<title>Персональный чарт</title>--%>
    <%--<jsp:include page="mobile.jsp"/>--%>
    <%--<link rel="stylesheet" href="common.css" type="text/css"/>--%>
<%--</head>--%>
<%--<body>--%>
<%--<jsp:include page="header.jsp"/>--%>
    <%
        Musix musix = (Musix) application.getAttribute("musix");
        String currentUserId = (String) session.getAttribute("userId");
        String userId = request.getParameter("userId");
        if (userId == null) {
            userId = currentUserId;
        }
        List<Map.Entry<String, Opinion>> orderedTracks = musix.getOrderedTracks(userId);

        NavigableSet<Integer> steps = new TreeSet<>();
        steps.addAll(Arrays.asList(1));

        %>

    <div>
        <form action="PersonalTrackRating" style="display: inline;" method="post">
            <input type="hidden" name="onlyDesired" value="true"/>
            <input type="hidden" name="step" value="-1"/>
            <input type="submit" value="-1 to desired"/>
        </form>
        <form action="PersonalTrackRating" style="display: inline;" method="post">
            <input type="hidden" name="onlyDesired" value="true"/>
            <input type="hidden" name="step" value="1"/>
            <input type="submit" value="+1 to desired"/>
        </form>
    </div>
    <div>
        <form action="PersonalTrackRating" style="display: inline;" method="post">
            <input type="hidden" name="allTracks" value="true"/>
            <input type="hidden" name="step" value="-1"/>
            <input type="submit" value="-1 to all"/>
        </form>
        <form action="PersonalTrackRating" style="display: inline;" method="post">
            <input type="hidden" name="allTracks" value="true"/>
            <input type="hidden" name="step" value="1"/>
            <input type="submit" value="+1 to all"/>
        </form>
    </div>
        <%

        for (Map.Entry<String, Opinion> track: orderedTracks) {
            %> <div  <% if (track.getKey().equals(request.getParameter("highlight"))) {%> style="font-weight: bold;" <%}%> ><%
            if (track.getValue().getAttitude() != Attitude.UNACCEPTABLE) {
            %>


            <%
            if (Objects.equals(currentUserId, userId)) {
                for (Integer step: steps.descendingSet()) {
                    %>
                    <form action="PersonalTrackRating" style="display: inline;" method="post">
                        <input type="hidden" name="track" value="<%=escapeHtml4(track.getKey())%>"/>
                        <input type="hidden" name="step" value="<%=step%>"/>
                        <input type="submit" value="+<%=step%>"/>
                    </form>
                    <%
                }
            }

            %>

            <input disabled style="width: 30px; background-color: white; border: none; text-align: center"  value="<%= track.getValue().getRating()%>"/>
            <%
                if (Objects.equals(currentUserId, userId)) {
                    for (Integer step: steps) {
                        %>
                        <form action="PersonalTrackRating" style="display: inline;" method="post">
                            <input type="hidden" name="track" value="<%=escapeHtml4(track.getKey())%>"/>
                            <input type="hidden" name="step" value="-<%=step%>"/>
                            <input type="submit" value="-<%=step%>"/>
                        </form>
                        <%
                    }
                }
            }
            %>

            <a target="_parent" name="<%=escapeHtml4(track.getKey())%>" class="<%=track.getValue().getAttitude()%>" href="rate.jsp?track=<%=escapeHtml4(track.getKey())%>">
                <%=escapeHtml4(track.getKey())%>
            </a>

        </div>
            <%
        }
    %>
<%--</body>--%>
<%--</html>--%>
