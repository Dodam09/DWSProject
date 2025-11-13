package com.dws.user.dw.dto;

import lombok.Data;

@Data
public class ExcelUploadResult {
    private int newWorkers;      // 새로 추가된 근무자 수
    private int existWorkers;    // 이미 있던 근무자 수
    private int newCompanys;     // 새로 추가된 회사 수
    private int existCompanys;   // 이미 있던 회사 수
    private int dwInserted;      // 작업일보(TB_DW) 저장 건수
}
