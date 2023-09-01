package com.dws.user.dw.service;

import java.util.ArrayList;

import com.dws.user.dw.util.PagingVO;
import com.dws.user.dw.vo.WorkersVO;

public interface WorkersService {
	
	public ArrayList<WorkersVO> worList(PagingVO vo);

	public int countWor();
	
}
