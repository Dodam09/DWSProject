package com.dws.user.dw.controller;

import java.io.InputStream;
import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dws.user.dw.dto.DailyWorkExcelRow;
import com.dws.user.dw.dto.ExcelUploadResult;
import com.dws.user.dw.service.CompanysService;
import com.dws.user.dw.service.DWListService;
import com.dws.user.dw.service.OfferService;
import com.dws.user.dw.service.SearcherService;
import com.dws.user.dw.service.TestService;
import com.dws.user.dw.service.WorkersService;
import com.dws.user.dw.util.PagingVO;
import com.dws.user.dw.vo.CompanysVO;
import com.dws.user.dw.vo.DWListVO;
import com.dws.user.dw.vo.OfferVO;
import com.dws.user.dw.vo.SearcherVO;
import com.dws.user.dw.vo.WorkersVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import java.time.LocalDate;
import java.util.List;

import org.apache.poi.ss.usermodel.*;


@Controller
@Slf4j
@RequestMapping("/user/*")
@RequiredArgsConstructor   // final 필드들 생성자 주입
public class WorkersController {

    private final WorkersService workersService;
    private final CompanysService companysService;
    private final DWListService dwListService;
    private final OfferService offerService;
    private final SearcherService searcherService;
    private final TestService testService;
    
    
    @PostMapping("uploadDwExcel")
    public String uploadDwExcel(@RequestParam("file") MultipartFile file,
                                Model model) {
        log.info("uploadDwExcel 실행 - 파일명: {}", file.getOriginalFilename());

        List<DailyWorkExcelRow> rows = new ArrayList<>();

        if (file.isEmpty()) {
            model.addAttribute("message", "파일이 비어 있습니다.");
            return "user/dwExcelResultForm";
        }

        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);

            // 1) 헤더 행(날짜/요일/성명/업체명...) 찾기
            int headerRowIdx = -1;
            for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String col0 = getString(row.getCell(0)); // 날짜
                String col1 = getString(row.getCell(1)); // 요일
                String col2 = getString(row.getCell(2)); // 성명
                String col3 = getString(row.getCell(3)); // 업체명

                if ("날짜".equals(col0) && "요일".equals(col1) && "성명".equals(col2)) {
                    headerRowIdx = i;
                    break;
                }
            }

            if (headerRowIdx == -1) {
                model.addAttribute("message", "작업일보 테이블 헤더(날짜/요일/성명)를 찾지 못했습니다.");
                return "user/dwExcelResultForm";
            }

            // 2) 헤더 아래부터 테이블 데이터만 읽기
            for (int i = headerRowIdx + 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // 완전 빈 줄이면 테이블 끝이라고 보고 종료
                if (isEmptyCell(row.getCell(0)) &&
                    isEmptyCell(row.getCell(1)) &&
                    isEmptyCell(row.getCell(2)) &&
                    isEmptyCell(row.getCell(3))) {
                    break;
                }

                DailyWorkExcelRow dto = new DailyWorkExcelRow();

                Cell dateCell = row.getCell(0);
                if (!isEmptyCell(dateCell)) {
                    if (DateUtil.isCellDateFormatted(dateCell)) {
                        dto.setWorkDateStr(
                            dateCell.getLocalDateTimeCellValue().toLocalDate().toString()
                        );
                    } else {
                        dto.setWorkDateStr(getString(dateCell));
                    }
                }

                dto.setWeek(getString(row.getCell(1)));
                dto.setWorkerName(getString(row.getCell(2)));
                dto.setCompanyName(getString(row.getCell(3)));
                dto.setDays(getInt(row.getCell(4)));
                dto.setPosition(getString(row.getCell(5)));
                dto.setPay(getInt(row.getCell(6)));
                dto.setPayType(getString(row.getCell(7)));
                dto.setMemo(getString(row.getCell(8)));

                rows.add(dto);
            }

        } catch (Exception e) {
            log.error("엑셀 파싱 중 오류", e);
            model.addAttribute("message", "엑셀 파일을 읽는 중 오류가 발생했습니다: " + e.getMessage());
            return "user/dwExcelResultForm";
        }

        log.info("엑셀에서 읽은 테이블 건수: {}", rows.size());

        // 3) 이제 진짜 목표: WORKERS / COMPANYS / DW 저장
        ExcelUploadResult result = dwListService.saveFromExcel(rows);

        model.addAttribute("result", result);
        return "user/dwExcelResultForm";
    }
    
    
    
 // ======= 엑셀 파싱용 유틸 메서드들 =======

    private boolean isEmptyCell(Cell cell) {
        if (cell == null) return true;
        if (cell.getCellType() == CellType.BLANK) return true;
        String s = getString(cell);
        return (s == null || s.trim().isEmpty());
    }

    private String getString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    // 날짜를 문자열로 쓰고 싶으면 여기서 포맷
                    yield cell.getLocalDateTimeCellValue().toLocalDate().toString();
                } else {
                    // 정수처럼 보이게
                    double d = cell.getNumericCellValue();
                    long l = (long) d;
                    if (l == d) yield String.valueOf(l);
                    else yield String.valueOf(d);
                }
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }

    private Integer getInt(Cell cell) {
        if (cell == null) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return (int) cell.getNumericCellValue();
            } else {
                String s = getString(cell);
                if (s == null || s.isBlank()) return null;
                return Integer.parseInt(s.replace(",", "").trim());
            }
        } catch (Exception e) {
            return null;
        }
    }



 // ===================== 메인 페이지 =====================

    @GetMapping("main")
    public String mainPage() {
        log.info("mainPage 실행");
        return "user/mainPageForm";   // /WEB-INF/views/user/mainPageForm.jsp 이런 식으로 매핑되겠죠?
    }
    
    
    // ===================== TEST =====================

    
    @GetMapping("test")
    public String Test(Model model) {
        log.info("TEST 메서드 실행");
        // List<TestVO> tvo = testService.test();
        // model.addAttribute("tvo", tvo);
        return "user/testForm";
    }

    // ===================== 근무자 목록 =====================

    @GetMapping("worListPage") // 근무자 목록 리스트
    public String worList(PagingVO vo,
                          Model model,
                          @RequestParam(value = "nowPage", required = false) String nowPage,
                          @RequestParam(value = "cntPerPage", required = false) String cntPerPage) {

        log.info("worList 실행 완료");

        int total = workersService.countWor();

        if (nowPage == null && cntPerPage == null) {
            nowPage = "1";
            cntPerPage = "40";
        } else if (nowPage == null) {
            nowPage = "1";
        } else if (cntPerPage == null) {
            cntPerPage = "40";
        }

        vo = new PagingVO(total, Integer.parseInt(nowPage), Integer.parseInt(cntPerPage));
        ArrayList<WorkersVO> wvo = workersService.worList(vo);

        log.info("wvo : {}", wvo);
        log.info("vo : {}", vo);
        log.info("nowPage : {}", nowPage);
        log.info("total : {}", total);
        log.info("cntPerPage : {}", cntPerPage);

        model.addAttribute("paging", vo);
        model.addAttribute("wList", wvo);

        return "user/worListPageForm";
    }

    // ===================== 업체 목록 =====================

    @GetMapping("comListPage") // 업체 목록 리스트
    public String comList(PagingVO vo,
                          Model model,
                          @RequestParam(value = "nowPage", required = false) String nowPage,
                          @RequestParam(value = "cntPerPage", required = false) String cntPerPage) {

        log.info("ComList 실행 완료");

        int total = companysService.countCom();

        if (nowPage == null && cntPerPage == null) {
            nowPage = "1";
            cntPerPage = "40";
        } else if (nowPage == null) {
            nowPage = "1";
        } else if (cntPerPage == null) {
            cntPerPage = "40";
        }

        vo = new PagingVO(total, Integer.parseInt(nowPage), Integer.parseInt(cntPerPage));
        ArrayList<CompanysVO> cvo = companysService.comList(vo);

        log.info("cvo : {}", cvo);
        log.info("vo : {}", vo);
        log.info("nowPage : {}", nowPage);
        log.info("total : {}", total);
        log.info("cntPerPage : {}", cntPerPage);

        model.addAttribute("paging", vo);
        model.addAttribute("cList", cvo);

        return "user/comListPageForm";
    }

    // ===================== 작업일보 목록 =====================

    @GetMapping("dwListPage") // 작업일보 리스트
    public String dwList(PagingVO vo,
                         Model model,
                         @RequestParam(value = "nowPage", required = false) String nowPage,
                         @RequestParam(value = "cntPerPage", required = false) String cntPerPage) {

        log.info("dwList 실행 완료");

        int total = dwListService.countDw();

        if (nowPage == null && cntPerPage == null) {
            nowPage = "1";
            cntPerPage = "40";
        } else if (nowPage == null) {
            nowPage = "1";
        } else if (cntPerPage == null) {
            cntPerPage = "40";
        }

        vo = new PagingVO(total, Integer.parseInt(nowPage), Integer.parseInt(cntPerPage));
        ArrayList<DWListVO> dvo = dwListService.dwList(vo);

        log.info("dvo : {}", dvo);
        log.info("vo : {}", vo);
        log.info("nowPage : {}", nowPage);
        log.info("total : {}", total);
        log.info("cntPerPage : {}", cntPerPage);

        model.addAttribute("paging", vo);
        model.addAttribute("dList", dvo);

        return "user/dwListPageForm";
    }

    // ===================== 구인접수대장 =====================

    @GetMapping("offerListPage") // 구인접수대장
    public String offerList(PagingVO vo,
                            Model model,
                            @RequestParam(value = "nowPage", required = false) String nowPage,
                            @RequestParam(value = "cntPerPage", required = false) String cntPerPage) {

        log.info("offerList 실행 완료");

        int total = offerService.countOffer();

        if (nowPage == null && cntPerPage == null) {
            nowPage = "1";
            cntPerPage = "40";
        } else if (nowPage == null) {
            nowPage = "1";
        } else if (cntPerPage == null) {
            cntPerPage = "40";
        }

        log.info("total : {}", total);

        vo = new PagingVO(total, Integer.parseInt(nowPage), Integer.parseInt(cntPerPage));
        ArrayList<OfferVO> ovo = offerService.offerList(vo);

        log.info("ovo : {}", ovo);
        log.info("vo : {}", vo);
        log.info("nowPage : {}", nowPage);
        log.info("total : {}", total);
        log.info("cntPerPage : {}", cntPerPage);

        model.addAttribute("paging", vo);
        model.addAttribute("oList", ovo);

        return "user/offerAllListPageForm";
    }

    // 업체 검색
    @GetMapping("searchOffer")
    public String searchOffer(String com_name, Model model) {

        log.info("searchOffer 실행 완료");

        try {
            ArrayList<OfferVO> ovo = offerService.searchOffer(com_name);

            if (!ovo.isEmpty()) {
                OfferVO firstVO = ovo.get(0);
                String com_name2 = firstVO.getCom_name();
                String com_major = firstVO.getCom_major();
                String com_address = firstVO.getCom_address();
                String com_phone = firstVO.getCom_phone();

                model.addAttribute("oList", ovo);
                model.addAttribute("com_name", com_name2);
                model.addAttribute("com_major", com_major);
                model.addAttribute("com_address", com_address);
                model.addAttribute("com_phone", com_phone);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "user/offerListPageForm";
    }

    // ===================== 구직접수대장 =====================

    @GetMapping("searcherListPage") // 구직접수대장
    public String searcherList(PagingVO vo,
                               Model model,
                               @RequestParam(value = "nowPage", required = false) String nowPage,
                               @RequestParam(value = "cntPerPage", required = false) String cntPerPage) {

        log.info("searcherList 실행 완료");

        int total = searcherService.countSearcher();

        if (nowPage == null && cntPerPage == null) {
            nowPage = "1";
            cntPerPage = "40";
        } else if (nowPage == null) {
            nowPage = "1";
        } else if (cntPerPage == null) {
            cntPerPage = "40";
        }

        log.info("total : {}", total);

        vo = new PagingVO(total, Integer.parseInt(nowPage), Integer.parseInt(cntPerPage));
        ArrayList<SearcherVO> svo = searcherService.searcherList(vo);

        log.info("vo : {}", vo);
        log.info("nowPage : {}", nowPage);
        log.info("total : {}", total);
        log.info("cntPerPage : {}", cntPerPage);

        model.addAttribute("paging", vo);
        model.addAttribute("sList", svo);

        return "user/searcherAllListPageForm";
    }

    // 근무자 검색
    @GetMapping("searcherSearch")
    public String searcherSearch(@Param("wor_name") String wor_name,
                                 Model model) {

        log.info("searcherSearch 실행 완료");

        try {
            ArrayList<SearcherVO> svo = searcherService.searcherSearch(wor_name);

            if (!svo.isEmpty()) {
                SearcherVO firstVO = svo.get(0);
                String wor_name2 = firstVO.getWor_name();
                String wor_want = firstVO.getWor_want();
                String wor_address = firstVO.getWor_address();
                String wor_birth = firstVO.getWor_birth();
                String wor_phone = firstVO.getWor_phone();

                log.info("wor_phone : {}", wor_phone);

                model.addAttribute("sList", svo);
                model.addAttribute("wor_name", wor_name2);
                model.addAttribute("wor_want", wor_want);
                model.addAttribute("wor_birth", wor_birth);
                model.addAttribute("wor_phone", wor_phone);
                model.addAttribute("wor_address", wor_address);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "user/searcherListPageForm";
    }
}
