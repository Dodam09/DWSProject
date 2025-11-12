package com.dws.user.dw.service;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

import org.springframework.stereotype.Service;



import com.dws.user.dw.util.PagingVO;
import com.dws.user.dw.vo.WorkersVO;

@Service

public interface WorkersService {
	
	public ArrayList<WorkersVO> worList(PagingVO vo);

	public int countWor();
	
}
