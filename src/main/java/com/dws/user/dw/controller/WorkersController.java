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
    
    
 // ===================== 작업일보 엑셀 업로드 =====================

    @PostMapping("uploadDwExcel")
    public String uploadDwExcel(@RequestParam("file") MultipartFile file,
                                Model model) {
        log.info("uploadDwExcel 실행 - 파일명: {}", file.getOriginalFilename());

        List<DWListVO> excelList = new ArrayList<>();

        if (file.isEmpty()) {
            model.addAttribute("message", "파일이 비어 있습니다.");
            return "user/dwExcelPreviewForm";
        }

        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트

            // 엑셀 구조 상, 앞부분에 제목/주소/전화 등이 있어서
            // "실제 데이터가 있는 행만" 골라서 처리하는 방식으로 갑니다.
            for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Cell dateCell = row.getCell(0);
                Cell nameCell = row.getCell(2);
                Cell companyCell = row.getCell(3);

                // 날짜, 이름, 업체가 모두 비어있으면 데이터 아님 → skip
                if (isEmptyCell(dateCell) && isEmptyCell(nameCell) && isEmptyCell(companyCell)) {
                    continue;
                }

                DWListVO vo = new DWListVO();

                // 1) 날짜
                if (dateCell != null && dateCell.getCellType() != CellType.BLANK) {
                    if (DateUtil.isCellDateFormatted(dateCell)) {
                        LocalDate date = dateCell.getLocalDateTimeCellValue().toLocalDate();
                        // vo.setWorkDate(date);  // DWListVO 필드 이름에 맞게 수정
                    } else {
                        // 날짜가 텍스트로 들어온 경우 대비
                        String dateStr = getString(dateCell);
                        // vo.setWorkDateStr(dateStr);
                    }
                }

                // 2) 요일
                vo.setDw_week(getString(row.getCell(1))); // 예: 수, 목

                // 3) 이름
                vo.setDw_name(getString(row.getCell(2)));

                // 4) 업체명
                vo.setDw_company(getString(row.getCell(3)));

                // 5) 일수 (숫자)
                vo.setDw_days(getInt(row.getCell(4)));

                // 6) 직종 (전공/조공)
                vo.setDw_position(getString(row.getCell(5)));

                // 7) 일당 (숫자)
                vo.setDw_pay(getInt(row.getCell(6)));

                // 8) 입금 방식 (개인직수, 사무실입금 등)
                vo.setDw_payType(getString(row.getCell(7)));

                // 9) 비고 ("/완료", "/2/10완료" 등)
                vo.setDw_memo(getString(row.getCell(8)));

                excelList.add(vo);
            }

        } catch (Exception e) {
            log.error("엑셀 파싱 중 오류", e);
            model.addAttribute("message", "엑셀 파일을 읽는 중 오류가 발생했습니다: " + e.getMessage());
            return "user/dwExcelPreviewForm";
        }

        log.info("엑셀에서 읽은 건수: {}", excelList.size());

        model.addAttribute("excelList", excelList);
        return "user/dwExcelPreviewForm";  // 미리보기용 JSP
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
