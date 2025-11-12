<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>근무자 목록</title>
</head>
<body>
<h2>근무자 목록</h2>

<table border="1">
    <tr>
        <th>이름</th>
        <th>생년</th>
        <th>주소</th>
        <th>희망직종</th>
        <th>전화번호</th>
        <th>전공</th>
    </tr>
    <c:forEach var="w" items="${wList}">
        <tr>
            <td>${w.wor_name}</td>
            <td>${w.wor_birth}</td>
            <td>${w.wor_address}</td>
            <td>${w.wor_want}</td>
            <td>${w.wor_phone}</td>
            <td>${w.wor_major}</td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
