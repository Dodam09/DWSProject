package com.dws.user.dw.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.dws.user.dw.util.PagingVO;
import com.dws.user.dw.vo.WorkersVO;


@Mapper
public interface WorkersDAO {
	
	public ArrayList<WorkersVO> worList(PagingVO vo);

	public int countWor();
	
	 WorkersVO findByName(String name);  // 이름으로 근무자 조회

    void insert(WorkersVO vo);          // 근무자 신규 등록
	
}
