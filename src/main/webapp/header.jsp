<%@ page import="com.nplekhanov.musix.User" %>
<%@ page import="com.nplekhanov.musix.Repository" %>
<%@ page import="static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4" %>
<%@ page import="com.nplekhanov.musix.AuthException" %>
<%@ page import="com.nplekhanov.musix.Chart" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String userId = (String) session.getAttribute("userId");
    if (userId == null) {
        throw new AuthException();
    }
    Repository repository = (Repository) application.getAttribute("repository");
    User user = repository.getUser(userId);
%>
<div>Привет, <b><%=escapeHtml4(user.getFullName())%></b> <a class="menu" href="opinions.jsp">Мнения</a> </div>

