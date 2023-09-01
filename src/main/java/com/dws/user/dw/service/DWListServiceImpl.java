package com.dws.user.dw.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dws.user.dw.dao.DWListDAO;
import com.dws.user.dw.util.PagingVO;
import com.dws.user.dw.vo.DWListVO;

import lombok.Setter;

@Service
public class DWListServiceImpl implements DWListService {
	
	@Setter(onMethod_ = @Autowired )
	private DWListDAO dwListDao;
	
	
	@Override
	public int countDw() {
		return dwListDao.countDw();
	}

	@Override
	public ArrayList<DWListVO> dwList(PagingVO vo) {
		ArrayList<DWListVO> dvo = dwListDao.dwList(vo);		
		return dvo;
	}

}
