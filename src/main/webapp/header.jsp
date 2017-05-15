<%@ page import="com.nplekhanov.musix.User" %>
<%@ page import="com.nplekhanov.musix.Musix" %>
<%@ page import="static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4" %>
<%@ page import="com.nplekhanov.musix.AuthException" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String userId = (String) session.getAttribute("userId");
    if (userId == null) {
        throw new AuthException();
    }
    Musix musix = (Musix) application.getAttribute("musix");
    User user = musix.getUser(userId);
%>
<div>
    Привет, <b><%=escapeHtml4(user.getFullName())%></b>
    <a class="menu" href="opinions.jsp">Мнения</a>
    <a class="menu" href="personal_chart.jsp">Персональный чарт</a>
</div>

