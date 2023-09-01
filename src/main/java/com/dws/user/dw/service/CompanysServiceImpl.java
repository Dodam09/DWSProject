package com.dws.user.dw.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dws.user.dw.dao.CompanysDAO;
import com.dws.user.dw.util.PagingVO;
import com.dws.user.dw.vo.CompanysVO;

import lombok.Setter;

@Service
public class CompanysServiceImpl implements CompanysService {

	@Setter(onMethod_ = @Autowired )
	private CompanysDAO companysdao;
	
	@Override
	public int countCom() {
		return companysdao.countCom();
	}

	@Override
	public ArrayList<CompanysVO> comList(PagingVO vo) {
		ArrayList<CompanysVO> cvo = companysdao.comList(vo);		
		return cvo;
	}

}
