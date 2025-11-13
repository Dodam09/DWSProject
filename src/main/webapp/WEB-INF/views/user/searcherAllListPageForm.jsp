<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>구직접수대장</title>
</head>
<body>

<h2>구직접수대장</h2>

<table border="1">
    <tr>
        <th>No</th>
        <th>근무자명</th>
        <th>희망일자</th>
        <th>희망임금</th>
        <th>생년</th>
        <th>전공</th>
        <th>희망직종</th>
        <th>전화번호</th>
        <th>주소</th>
        <th>업체명</th>
        <th>근무자번호</th>
    </tr>

    <c:forEach var="s" items="${sList}">
        <tr>
            <td>${s.s_no}</td>
            <td>${s.wor_name}</td>
            <td>${s.w_day}</td>
            <td>${s.w_price}</td>
            <td>${s.wor_birth}</td>
            <td>${s.wor_major}</td>
            <td>${s.wor_want}</td>
            <td>${s.wor_phone}</td>
            <td>${s.wor_address}</td>
            <td>${s.com_name}</td>
            <td>${s.w_no}</td>
        </tr>
    </c:forEach>

</table>

</body>
</html>
