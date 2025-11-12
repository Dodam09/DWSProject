
# 🗂️ DWSProject - Excel 기반 인력 · 작업일보 자동 등록 시스템

## 📌 개요

DWSProject는 건설 및 현장 인력 관리를 위한 사내 전용 웹 어드민 시스템입니다.  
사내에서 엑셀로 작성한 작업일보 파일을 업로드하면, 그 내용을 바탕으로 근무자, 업체, 작업 정보, 구인·구직 데이터를 자동으로 등록하고 정리합니다.  
복잡한 수작업 등록을 자동화하여 현장 운영 효율을 크게 향상시킨 프로젝트입니다.

---

## ⚙️ 주요 기능

- **엑셀 파일 기반 자동 등록**
  - 하나의 작업일보 엑셀 파일을 업로드하면 관련 데이터를 VO별로 자동 분리 및 저장
  - (근무자, 업체, 작업 내역, 금액, 날짜 등)
- **작업일보 관리**
  - 일자별 정산 금액(w_price), 지급 방식(w_price_how), 메모 등 기록
- **근무자 및 업체 관리**
  - 구직자/구인업체 등록 및 정보 조회
  - 이름, 업체명 등으로 조건 검색 가능
- **리스트 페이징 처리**
  - 대용량 데이터를 위한 페이징 처리 및 정렬 기능
- **JSP 기반 사용자 인터페이스**
  - 간단하고 직관적인 UI 제공

---

## 🛠 기술 스택

- **Backend**: Java 11, Spring Framework, Spring MVC, MyBatis
- **Frontend**: JSP, JSTL, HTML/CSS
- **Database**: Oracle SQL
- **기타 사용 기술**:
  - Lombok (VO 자동 Getter/Setter)
  - Log4j (로깅)
  - Apache POI (엑셀 파싱) *(사용된 경우)*

---

## 🙋🏻 담당 업무

- VO 설계 및 데이터 구조 정리 (`WorkersVO`, `CompanysVO`, `DWListVO`, `OfferVO`, `SearcherVO`)
- 조건 검색 기능 컨트롤러 및 서비스 구현 (`searchOffer`, `searcherSearch`)
- 리스트 페이지 페이징 로직 구현
- 엑셀 업로드 → 데이터 자동 등록 흐름 연동
- MyBatis 매퍼 및 Service 계층 로직 작성

---

## 📁 프로젝트 구조 (일부)

```
com.dws.user.dw.controller       // 사용자 컨트롤러
com.dws.user.dw.service          // 서비스 계층
com.dws.user.dw.vo               // VO (데이터 객체)
com.dws.user.dw.util             // 유틸리티 (페이징 등)
resources/mapper                 // MyBatis 매퍼
```

---

## ✅ 기대 효과

- 반복적인 작업일보 등록 업무 자동화
- 구직자와 업체 정보를 효율적으로 관리
- 실무 현장에서 운영 효율 및 데이터 정확도 향상

