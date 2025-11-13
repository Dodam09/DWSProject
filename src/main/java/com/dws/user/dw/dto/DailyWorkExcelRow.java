package com.dws.user.dw.dto;

import lombok.Data;

@Data
public class DailyWorkExcelRow {

    private String workDateStr;   // 날짜 (문자열 또는 LocalDate로 바꿔도 됨)
    private String week;          // 요일
    private String workerName;    // 성명
    private String companyName;   // 업체명
    private Integer days;         // 일수
    private String position;      // 직종(전공/조공)
    private Integer pay;          // 일당
    private String payType;       // 입금 방식(개인직수/사무실입금 등)
    private String memo;          // 비고
}
