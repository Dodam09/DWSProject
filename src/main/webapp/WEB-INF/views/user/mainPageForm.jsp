<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>메인 페이지</title>
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        .container {
            max-width: 800px;
            margin: 40px auto;
            text-align: center;
        }
        h1 {
            margin-bottom: 30px;
        }
        .btn-wrap {
            display: flex;
            flex-wrap: wrap;
            gap: 12px;
            justify-content: center;
        }
        .btn-link {
            display: inline-block;
            padding: 10px 18px;
            border-radius: 6px;
            border: 1px solid #999;
            text-decoration: none;
            color: #333;
            background: #f5f5f5;
        }
        .btn-link:hover {
            background: #e0e0e0;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>메인 페이지</h1>
    <p>아래 버튼을 눌러 각 리스트/대장 페이지로 이동하세요.</p>

    <div class="btn-wrap">
        <!-- 근무자 목록 -->
        <a class="btn-link" href="<c:url value='/user/worListPage'/>">
            근무자 목록
        </a>

        <!-- 업체 목록 -->
        <a class="btn-link" href="<c:url value='/user/comListPage'/>">
            업체 목록
        </a>

        <!-- 작업일보 목록 -->
        <a class="btn-link" href="<c:url value='/user/dwListPage'/>">
            작업일보 목록
        </a>

        <!-- 구인접수대장 (전체 리스트) -->
        <a class="btn-link" href="<c:url value='/user/offerListPage'/>">
            구인접수대장
        </a>

        <!-- 구직접수대장 (전체 리스트) -->
        <a class="btn-link" href="<c:url value='/user/searcherListPage'/>">
            구직접수대장
        </a>

        <!-- 테스트 페이지(optional) -->
        <a class="btn-link" href="<c:url value='/user/test'/>">
            TEST 페이지
        </a>
    </div>
</div>
</body>
</html>
