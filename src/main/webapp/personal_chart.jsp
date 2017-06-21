<%@ page import="com.nplekhanov.musix.Musix" %>
<%@ page import="com.nplekhanov.musix.model.Opinion" %>
<%@ page import="com.nplekhanov.musix.model.User" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4" %>
<%@ page import="java.util.Objects" %>
<%@ page import="com.nplekhanov.musix.model.Attitude" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Персональный чарт</title>
    <jsp:include page="mobile.jsp"/>
    <link rel="stylesheet" href="common.css" type="text/css"/>
    <script>
        var lastSize = 0;

        function resizeIframe(obj) {
            var desiredSize = obj.contentWindow.document.body.scrollHeight;
            if (lastSize != desiredSize) {
                obj.style.height = obj.contentWindow.document.body.scrollHeight + 'px';
                lastSize = desiredSize;
            }
        }
    </script>
</head>
<body>
<jsp:include page="header.jsp"/>

Оценки треков используются для определения приоритета между треками с одинаковым кол-вом желающих.
Абсолютные значения оценок не важны - учитываются только сравнительные отношения между треками каждого пользователя.
<div><%
    String userId = request.getParameter("userId");
    Musix musix = (Musix) application.getAttribute("musix");
    for (User user: musix.getDefaultBand()) {
%>
    <a href="personal_chart.jsp?userId=<%=escapeHtml4(user.getUid())%>">
        <img width="64" src="<%=escapeHtml4(user.getPhotoUrl())%>" title="<%=escapeHtml4(user.getFullName())%>" alt="<%=escapeHtml4(user.getFullName())%>"/>
    </a>
    <%
        }
    %></div>
<iframe src="personal_chart_frame_content.jsp<% if (userId != null) {%>?userId=<%=escapeHtml4(userId)%><%}%>" width="100%" frameborder="0" scrolling="0" onload="resizeIframe(this)">

</iframe>
</body>
</html>
