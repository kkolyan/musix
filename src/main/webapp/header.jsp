<%@ page import="com.nplekhanov.musix.User" %>
<%@ page import="com.nplekhanov.musix.Repository" %>
<%@ page import="static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4" %>
<%@ page import="com.nplekhanov.musix.AuthException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String userId = (String) session.getAttribute("userId");
    if (userId == null) {
        throw new AuthException();
    }
    Repository repository = (Repository) application.getAttribute("repository");
    User user = repository.getUser(userId);
%>
<div>Привет, <b><%=escapeHtml4(user.getFullName())%></b></div>
<a class="menu" href="rate.jsp">Голосовать</a>
<a class="menu" href="chart.jsp">Чарт</a>