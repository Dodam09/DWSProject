<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>구인접수 검색 결과</title>
</head>
<body>

<h2>업체 검색 결과</h2>

<!-- 업체 기본 정보 -->
<c:if test="${not empty com_name}">
    <h3>업체 정보</h3>
    <table border="1">
        <tr>
            <th>업체명</th>
            <td>${com_name}</td>
        </tr>
        <tr>
            <th>업종</th>
            <td>${com_major}</td>
        </tr>
        <tr>
            <th>주소</th>
            <td>${com_address}</td>
        </tr>
        <tr>
            <th>전화번호</th>
            <td>${com_phone}</td>
        </tr>
    </table>
</c:if>


<h3 style="margin-top: 20px;">구인접수 이력</h3>

<table border="1">
    <tr>
        <th>No</th>
        <th>근무자명</th>
        <th>업체명</th>
        <th>업종</th>
        <th>주소</th>
        <th>전화번호</th>
        <th>근무자전공</th>
        <th>임금</th>
        <th>근무자번호</th>
        <th>작업일자</th>
