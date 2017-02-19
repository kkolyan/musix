<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="mx" tagdir="/WEB-INF/tags" %>
<html>
<head>
    <jsp:include page="mobile.jsp"/>
    <title>Title</title>
    <link rel="stylesheet" href="common.css" type="text/css"/>
</head>
<body>
<%String userId = request.getParameter("userId"); %>
<mx:myrates userId="<%=userId%>"/>
</body>
</html>
