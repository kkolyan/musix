<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="mobile.jsp"/>
    <title>Вход в систему</title>
</head>
<body>
<%
if (request.getServerName().equals("localhost")) {
    %>
    <form action="Login">
        <label>
            uid
            <input name="uid"/>
        </label>
        <label>
            first_name
            <input name="first_name"/>
        </label>
        <label>
            last_name
            <input name="last_name"/>
        </label>
        <input type="hidden" name="hash" value=""/>
        <input type="submit" value="Войти"/>
    </form>
    <%
} else {
    %>
    <!-- Put this script tag to the <head> of your page -->
    <script type="text/javascript" src="//vk.com/js/api/openapi.js?139"></script>

    <script type="text/javascript">
        VK.init({apiId: 5882753});
    </script>
    <!-- Put this div tag to the place, where Auth block will be -->
    <div id="vk_auth"></div>
    <script type="text/javascript">
        VK.Widgets.Auth("vk_auth", {width: "200px", authUrl: '${pageContext.request.contextPath}/Login'});
    </script>
    <%
}
%>
</body>
</html>
