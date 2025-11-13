<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>구인접수대장 목록</title>
</head>
<body>

<h2>구인접수대장 목록</h2>

<table border="1">
    <tr>
        <th>No</th>
        <th>근무자명</th>
        <th>업체명</th>
        <th>업체업종</th>
        <th>업체주소</th>
        <th>업체전화</th>
        <th>근무자전공</th>
        <th>임금</th>
        <th>근무자번호</th>
        <th>작업일자</th>
    </tr>

    <c:forEach var="o" items="${oList}">
        <tr>
            <td>${o.o_no}</td>
            <td>${o.wor_name}</td>
            <td>${o.com_name}</td>
            <td>${o.com_major}</td>
            <td>${o.com_address}</td>
            <td>${o.com_phone}</td>
            <td>${o.wor_major}</td>
            <td>${o.w_price}</td>
            <td>${o.w_no}</td>
            <td>${o.w_day}</td>
        </tr>
    </c:forEach>

</table>

</body>
</html>
