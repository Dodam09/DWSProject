<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>구인접수대장(일용직용)</title>
  <style>
    /* A4 인쇄셋 */
    @page { size: A4; margin: 10mm; }
    @media print { .no-print { display:none; } }

    body { margin:0; font-family: "Malgun Gothic", "맑은 고딕", sans-serif; color:#000; }
    .sheet {
      width: 210mm; min-height: 297mm;
      border: 2px solid #245b9e;
      box-sizing: border-box;
      padding: 8mm 8mm 6mm 8mm;
      background: #f7fbff;
    }

    .title {
      text-align: center;
      font-weight: 700;
      font-size: 18px;
      margin: 4mm 0 6mm;
      letter-spacing: .5px;
    }

    table { width: 100%; border-collapse: collapse; table-layout: fixed; }
    th, td { border: 1px solid #245b9e; padding: 6px 6px; word-break: keep-all; }
    th { background: #e8f2ff; font-weight: 700; }

    /* 상단 회사정보 표 */
    .company {
      margin-bottom: 8mm;
    }
    .company th, .company td { text-align: center; }
    .company .label { width: 18%; }
    .company .value { width: 32%; }

    /* 구인접수현황 표 */
    .section-title {
      text-align: center; font-weight: 700; margin: -2mm 0 2mm;
    }
    .jobs thead th { font-size: 13px; }
    .jobs td, .jobs th { font-size: 12px; }
    .w-date{width:12%} .w-job{width:8%} .w-gender{width:6%} .w-cnt{width:6%}
    .w-skill{width:16%} .w-age{width:6%} .w-edu{width:8%} .w-hrs{width:10%}
    .w-pay{width:10%} .w-meal{width:8%} .w-place{width:10%} .w-name{width:10%}

    /* 하단 각주 */
    .footer {
      margin-top: 6mm;
      display: grid;
      grid-template-columns: 1fr 1fr 1fr;
      gap: 4px;
    }
    .footer > div {
      border: 1px solid #245b9e; padding: 8px; text-align: center; font-size: 12px; background:#eef6ff;
    }

    /* 보조 */
    .right { text-align:right; }
    .center { text-align:center; }
  </style>
</head>
<body>
<div class="sheet">

  <div class="title">구인접수대장(일용직용)</div>

  <!-- 회사(사업체) 정보 표 -->
  <table class="company">
    <colgroup>
      <col class="label"><col class="value">
      <col class="label"><col class="value">
      <col class="label"><col class="value">
      <col class="label"><col class="value">
      <col class="label"><col class="value">
    </colgroup>
    <tr>
      <th>사업체명</th>
      <td>
        <c:out value="${company.name != null ? company.name : '고려전력'}"/>
      </td>
      <th>대표자(담당자)</th>
      <td><c:out value="${company.manager}"/></td>
      <th>소재지</th>
      <td><c:out value="${company.address != null ? company.address : '십정동'}"/></td>
      <th>전화번호</th>
      <td><c:out value="${company.phone != null ? company.phone : '032-589-4788'}"/></td>
      <th>업종(주생산품목)</th>
      <td><c:out value="${company.bizType != null ? company.bizType : '전기'}"/></td>
    </tr>
    <tr>
      <th>종업원수</th>
      <td><c:out value="${company.employees}"/></td>
      <!-- 나머지 셀 병합 느낌 유지용 빈칸 -->
      <td colspan="8"></td>
    </tr>
  </table>

  <div class="section-title">구인접수현황</div>

  <!-- 구인접수현황 표 -->
  <table class="jobs">
    <thead>
      <tr>
        <th class="w-date">접수일자</th>
        <th class="w-job">직종</th>
        <th class="w-gender">성별</th>
        <th class="w-cnt">인원</th>
        <th class="w-skill">자격및기능정도</th>
        <th class="w-age">연령</th>
        <th class="w-edu">학력</th>
        <th class="w-hrs">근로시간</th>
        <th class="w-pay">임금</th>
        <th class="w-meal">숙식제공</th>
        <th class="w-place">근무지</th>
        <th class="w-name">성명</th>
      </tr>
    </thead>
    <tbody>
      <!-- 실제 데이터 렌더 -->
      <c:forEach var="r" items="${entries}">
        <tr>
          <td class="center">
            <fmt:formatDate value="${r.date}" pattern="yyyy-MM-dd"/>
          </td>
          <td class="center"><c:out value="${r.jobType}"/></td>
          <td class="center"><c:out value="${r.gender}"/></td>
          <td class="center"><c:out value="${r.count}"/></td>
          <td class="center"><c:out value="${r.skill}"/></td>
          <td class="center"><c:out value="${r.age}"/></td>
          <td class="center"><c:out value="${r.education}"/></td>
          <td class="center"><c:out value="${r.workTime}"/></td>
          <td class="right">
            <c:choose>
              <c:when test="${not empty r.wage}">
                <fmt:formatNumber value="${r.wage}" type="currency" currencySymbol="₩" pattern="₩#,##0"/>
              </c:when>
              <c:otherwise></c:otherwise>
            </c:choose>
          </td>
          <td class="center"><c:out value="${r.mealProvide}"/></td>
          <td class="center"><c:out value="${r.workPlace}"/></td>
          <td class="center"><c:out value="${r.name}"/></td>
        </tr>
      </c:forEach>

      <!-- 빈 줄 패딩(예: 총 28행 맞추기) -->
      <c:set var="totalRows" value="28"/>
      <c:set var="remain" value="${totalRows - fn:length(entries)}"/>
      <c:if test="${remain > 0}">
        <c:forEach begin="1" end="${remain}" var="i">
          <tr>
            <td>&nbsp;</td><td></td><td></td><td></td><td></td><td></td>
            <td></td><td></td><td></td><td></td><td></td><td></td>
          </tr>
        </c:forEach>
      </c:if>
    </tbody>
  </table>

  <!-- 하단 각주(양식 번호/승인일/용지 등) -->
  <div class="footer">
    <div><strong>32324-05611비</strong></div>
    <div><strong>98.3.19 승인</strong></div>
    <div><strong>210mm × 297mm</strong><br/>(보존용지(1종) 60g/㎡)</div>
  </div>

  <div class="no-print" style="margin-top:8mm; text-align:right;">
    <button onclick="window.print()">인쇄하기</button>
  </div>
</div>
</body>
</html>
