<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>구직자 검색 결과</title>
</head>
<body>

<h2>구직자 검색 결과</h2>

<!-- 개별 구직자 기본 정보 -->
<c:if test="${not empty wor_name}">
    <h3>구직자 정보</h3>

    <table border="1">
        <tr>
            <th>이름</th>
            <td>${wor_name}</td>
        </tr>
        <tr>
            <th>희망직종</th>
            <td>${wor_want}</td>
        </tr>
        <tr>
            <th>생년</th>
            <td>${wor_birth}</td>
        </tr>
        <tr>
            <th>전화번호</th>
            <td>${wor_phone}</td>
        </tr>
        <tr>
            <th>주소</th>
            <td>${wor_address}</td>
        </tr>
    </table>
</c:if>

<br/>

<h3>구직 이력</h3>

<table border="1">
    <tr>
        <th>No</th>
        <th>이름</th>
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
