<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>작업일보 목록</title>
</head>
<body>

<h2>작업일보 목록</h2>

<table border="1">
    <tr>
        <th>No</th>
        <th>근무자명</th>
        <th>업체명</th>
        <th>배정시간</th>
        <th>임금</th>
        <th>지급방식</th>
        <th>작업내용</th>
        <th>작업일자</th>
    </tr>

    <c:forEach var="d" items="${dList}">
        <tr>
            <td>${d.w_no}</td>
            <td>${d.wor_name}</td>
            <td>${d.com_name}</td>
            <td>${d.w_allo}</td>
            <td>${d.w_price}</td>
            <td>${d.w_price_how}</td>
            <td>${d.w_memo}</td>
            <td>${d.w_day}</td>
        </tr>
    </c:forEach>

</table>

</body>
</html>
