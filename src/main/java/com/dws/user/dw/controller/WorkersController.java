package com.dws.user.dw.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
import com.dws.user.dw.vo.WorkersVO;
import com.dws.user.dw.vo.SearcherVO;
import com.dws.user.dw.vo.TestVO;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value="/user/*")
@Slf4j
public class WorkersController {
	
	@Setter(onMethod_ = @Autowired)
	private WorkersService workersService;
	
	@Setter(onMethod_ = @Autowired )
	private CompanysService companysService;
	
	@Setter(onMethod_ = @Autowired )
	private DWListService dwListService;
	
	@Setter(onMethod_ = @Autowired )
	private OfferService offerService;
	
	@Setter(onMethod_ = @Autowired )
	private SearcherService searcherService;
	
	@Setter(onMethod_ = @Autowired )
	private TestService testService;
	
	
	
	@GetMapping("test")
	public String Test(Model model) {
		log.info("TEST 메서드 실행");
		
		List<TestVO> tvo = testService.test(); 
		
		model.addAttribute("tvo",tvo);
		
		return "user/testForm";
	}
	
	@GetMapping("worListPage") //근무자 목록 리스트
	public String worList(PagingVO vo,Model model
			,@RequestParam(value="nowPage", required = false)String nowPage
			,@RequestParam(value="cntPerPage", required = false)String cntPerPage
			) {
		
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
		
		log.info("wvo :" + workersService.worList(vo));
		log.info("vo :" + vo );
		log.info("nowPage :"+ nowPage);
		log.info("total :"+ total);
		log.info("cntPerPage :" + cntPerPage);
		
		model.addAttribute("paging", vo);
		model.addAttribute("wList",wvo);
		
		
		
		

		return "user/worListPageForm";
	}
	

	
	@GetMapping("comListPage") //업체 목록 리스트
	public String comList(PagingVO vo,Model model
			,@RequestParam(value="nowPage", required = false)String nowPage
			,@RequestParam(value="cntPerPage", required = false)String cntPerPage
			) {
		
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
		
		log.info("cvo :" + companysService.comList(vo));
		log.info("vo :" + vo );
		log.info("nowPage :"+ nowPage);
		log.info("total :"+ total);
		log.info("cntPerPage :" + cntPerPage);
		
		model.addAttribute("paging", vo);
		model.addAttribute("cList",cvo);

		return "user/comListPageForm";
	}
	
	@GetMapping("dwListPage")//작업일보 리스트
	public String dwList(PagingVO vo, Model model
			, @RequestParam(value="nowPage", required=false)String nowPage
			, @RequestParam(value="cntPerPage", required=false)String cntPerPage) {
		
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
		
		log.info("dvo :" + dwListService.dwList(vo));
		log.info("vo :" + vo );
		log.info("nowPage :"+ nowPage);
		log.info("total :"+ total);
		log.info("cntPerPage :" + cntPerPage);
		
		model.addAttribute("paging", vo);
		model.addAttribute("dList",dvo);
		
		return "user/dwListPageForm";
	}
	
	
	
	
	
	
	
	
	@GetMapping("offerListPage") //구인접수대장
	public String offerList(PagingVO vo,Model model
			,@RequestParam(value="nowPage", required = false)String nowPage
			,@RequestParam(value="cntPerPage", required = false)String cntPerPage
			) {
		
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
		log.info("total :"+total);
		
		vo = new PagingVO(total, Integer.parseInt(nowPage), Integer.parseInt(cntPerPage));
		ArrayList<OfferVO> ovo = offerService.offerList(vo);
		
		log.info("ovo :" + offerService.offerList(vo));
		log.info("vo :" + vo );
		log.info("nowPage :"+ nowPage);
		log.info("total :"+ total);
		log.info("cntPerPage :" + cntPerPage);
		
		model.addAttribute("paging", vo);
		model.addAttribute("oList",ovo);

		return "user/offerAllListPageForm";
	}
	
	
	
	
	
	@GetMapping("searchOffer") //업체 검색
	public String searchOffer(String com_name,Model model) {
			
		log.info("searchOffer 실행 완료");
		
		try {
			ArrayList<OfferVO> ovo = offerService.searchOffer(com_name);
			
			if(!ovo.isEmpty()) {
			    OfferVO firstVO = ovo.get(0);
			    String com_name2 = firstVO.getCom_name();
			    String com_major = firstVO.getCom_major();
			    String com_address = firstVO.getCom_address();
			    String com_phone = firstVO.getCom_phone();
			   
			    
				
				model.addAttribute("oList",ovo);
				model.addAttribute("com_name",com_name2);
				model.addAttribute("com_major",com_major);
				model.addAttribute("com_address",com_address);
				model.addAttribute("com_phone",com_phone);
				
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return "user/offerListPageForm";
	}
	
	
	
	
	
	@GetMapping("searcherListPage") //구직접수대장
	public String searcherList(PagingVO vo,Model model
			,@RequestParam(value="nowPage", required = false)String nowPage
			,@RequestParam(value="cntPerPage", required = false)String cntPerPage
			) {
		
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
		log.info("total :"+total);
		
		vo = new PagingVO(total, Integer.parseInt(nowPage), Integer.parseInt(cntPerPage));
		ArrayList<SearcherVO> svo = searcherService.searcherList(vo);
		
		log.info("vo :" + vo );
		log.info("nowPage :"+ nowPage);
		log.info("total :"+ total);
		log.info("cntPerPage :" + cntPerPage);
		
		model.addAttribute("paging", vo);
		model.addAttribute("sList",svo);

		return "user/searcherAllListPageForm";
	}
	
		
		
	
	@GetMapping("searcherSearch") //근무자 검색
	public String searcherSearch(
			 @Param("wor_name") String wor_name,Model model) {
			
		log.info("searcherSearch 실행 완료");
		
		try {
			ArrayList<SearcherVO> svo = searcherService.searcherSearch(wor_name);
			
			if(!svo.isEmpty()) {
			    SearcherVO firstVO = svo.get(0);
			    String wor_name2 = firstVO.getWor_name();
			    String wor_want = firstVO.getWor_want();
			    String wor_address = firstVO.getWor_address();
			    String wor_birth = firstVO.getWor_birth();
			    String wor_phone = firstVO.getWor_phone();
			    log.info("wor_phone :"+wor_phone);
				
				model.addAttribute("sList",svo);
				model.addAttribute("wor_name",wor_name2);
				model.addAttribute("wor_want",wor_want);
				model.addAttribute("wor_birth",wor_birth);
				model.addAttribute("wor_phone",wor_phone);
				model.addAttribute("wor_address",wor_address);
				
				
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return "user/searcherListPageForm";
	}
	
}
