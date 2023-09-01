package com.dws.user.dw.service;

import java.util.ArrayList;

import com.dws.user.dw.util.PagingVO;
import com.dws.user.dw.vo.CompanysVO;


public interface CompanysService {
	
	public ArrayList<CompanysVO> comList(PagingVO vo);

	public int countCom();
}
