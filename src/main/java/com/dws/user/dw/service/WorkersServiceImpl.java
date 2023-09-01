package com.dws.user.dw.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dws.user.dw.dao.WorkersDAO;
import com.dws.user.dw.util.PagingVO;
import com.dws.user.dw.vo.WorkersVO;

import lombok.Setter;

@Service
public class WorkersServiceImpl implements WorkersService {
	
	@Setter(onMethod_ = @Autowired )
	private WorkersDAO workersdao;

	@Override
	public int countWor() {
		return workersdao.countWor();
	}

	@Override
	public ArrayList<WorkersVO> worList(PagingVO vo) {
		ArrayList<WorkersVO> wvo = workersdao.worList(vo);		
		return wvo;
	}
	
	
	
}
