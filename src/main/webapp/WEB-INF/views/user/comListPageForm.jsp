<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>업체 목록</title>
</head>
<body>
<h2>업체 목록</h2>

<table border="1">
    <tr>
        <th>업체명</th>
        <th>주소</th>
        <th>전화번호</th>
        <th>업종/전문분야</th>
    </tr>
    <c:forEach var="c" items="${cList}">
        <tr>
            <td>${c.com_name}</td>
            <td>${c.com_address}</td>
            <td>${c.com_phone}</td>
            <td>${c.com_major}</td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
