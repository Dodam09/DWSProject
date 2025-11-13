package com.dws.user.dw.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dws.user.dw.dao.DWListDAO;
import com.dws.user.dw.dao.WorkersDAO;
import com.dws.user.dw.dao.CompanysDAO;
import com.dws.user.dw.dto.DailyWorkExcelRow;
import com.dws.user.dw.dto.ExcelUploadResult;
import com.dws.user.dw.util.PagingVO;
import com.dws.user.dw.vo.CompanysVO;
import com.dws.user.dw.vo.DWListVO;
import com.dws.user.dw.vo.WorkersVO;

import lombok.Setter;

@Service
public class DWListServiceImpl implements DWListService {

    @Setter(onMethod_ = @Autowired)
    private DWListDAO dwListDao;

    // 근무자 / 회사 테이블도 건드려야 하니까 DAO 주입
    @Setter(onMethod_ = @Autowired)
    private WorkersDAO workersDao;

    @Setter(onMethod_ = @Autowired)
    private CompanysDAO companysDao;

    @Override
    public int countDw() {
        return dwListDao.countDw();
    }

    @Override
    public ArrayList<DWListVO> dwList(PagingVO vo) {
        ArrayList<DWListVO> dvo = dwListDao.dwList(vo);
        return dvo;
    }

    /**
     * 엑셀에서 읽어온 작업일보 rows를 기준으로
     * 1) TB_WORKERS 에 근무자 저장
     * 2) TB_COMPANYS 에 회사 저장
     * 3) TB_DW 에 작업일보 저장
     */
    @Override
    @Transactional
    public ExcelUploadResult saveFromExcel(List<DailyWorkExcelRow> rows) {

        ExcelUploadResult result = new ExcelUploadResult();

        // 같은 이름/회사 여러 번 조회하지 않도록 캐시
        Map<String, Integer> workerIdCache = new HashMap<>();
        Map<String, Integer> companyIdCache = new HashMap<>();

        for (DailyWorkExcelRow r : rows) {

            // 이름이 없으면 스킵
            if (r.getWorkerName() == null || r.getWorkerName().isBlank()) {
                continue;
            }

            // ===================== 1. 근무자 처리 (TB_WORKERS) =====================
            Integer workerId = workerIdCache.get(r.getWorkerName());

            if (workerId == null) {
                // DAO에 이런 메서드 추가해줘야 함: WorkersVO findByName(String name)
                WorkersVO existWorker = workersDao.findByName(r.getWorkerName());
                if (existWorker != null) {
                    workerId = existWorker.getWor_no(); // PK 컬럼명에 맞게 수정
                    result.setExistWorkers(result.getExistWorkers() + 1);
                } else {
                    WorkersVO newWorker = new WorkersVO();
                    newWorker.setWor_name(r.getWorkerName());
                    // 필요하면 전화번호, 생년월일 등도 엑셀에서 받아서 세팅

                    // DAO에 insert 메서드 추가: void insert(WorkersVO vo)
                    workersDao.insert(newWorker);

                    // MyBatis useGeneratedKeys 사용 시 PK가 vo에 셋팅됨
                    workerId = newWorker.getWor_no();
                    result.setNewWorkers(result.getNewWorkers() + 1);
                }
                workerIdCache.put(r.getWorkerName(), workerId);
            }

            // ===================== 2. 회사 처리 (TB_COMPANYS) =====================
            Integer companyId = null;
            if (r.getCompanyName() != null && !r.getCompanyName().isBlank()) {
                companyId = companyIdCache.get(r.getCompanyName());

                if (companyId == null) {
                    // DAO에 이런 메서드 추가해줘야 함: CompanysVO findByName(String name)
                    CompanysVO existCompany = companysDao.findByName(r.getCompanyName());
                    if (existCompany != null) {
                        companyId = existCompany.getCom_no();  // PK 컬럼명에 맞게 수정
                        result.setExistCompanys(result.getExistCompanys() + 1);
                    } else {
                        CompanysVO newCompany = new CompanysVO();
                        newCompany.setCom_name(r.getCompanyName());
                        // 필요하면 주소/전화번호 등도 세팅

                        // DAO에 insert 메서드 추가: void insert(CompanysVO vo)
                        companysDao.insert(newCompany);

                        companyId = newCompany.getCom_no();
                        result.setNewCompanys(result.getNewCompanys() + 1);
                    }
                    companyIdCache.put(r.getCompanyName(), companyId);
                }
            }

            // ===================== 3. 작업일보 저장 (TB_DW) =====================
            DWListVO dw = new DWListVO();

            // 날짜는 문자열로 받았으므로, DB 타입에 따라 변환해서 넣어야 함
            // 예) DB 컬럼이 VARCHAR2라면 그냥 set, DATE라면 Mapper에서 TO_DATE 사용
            dw.setWorkDateStr(r.getWorkDateStr());   // DWListVO에 필드 하나 추가해두면 편함
            dw.setDw_week(r.getWeek());

            // FK 매핑
            dw.setWor_no(workerId);    // 근무자 FK (필드명 필요에 따라 수정)
            dw.setCom_no(companyId);   // 회사 FK (nullable 가능)

            // 기타 칼럼
            dw.setDw_days(r.getDays());
            dw.setDw_position(r.getPosition());
            dw.setDw_pay(r.getPay());
            dw.setDw_payType(r.getPayType());
            dw.setDw_memo(r.getMemo());

            // DAO에 insert 메서드 추가: void insert(DWListVO vo)
            dwListDao.insert(dw);
            result.setDwInserted(result.getDwInserted() + 1);
        }

        return result;
    }

}
