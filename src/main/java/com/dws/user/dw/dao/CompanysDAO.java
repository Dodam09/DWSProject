package com.dws.user.dw.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.dws.user.dw.util.PagingVO;
import com.dws.user.dw.vo.CompanysVO;

@Mapper
public interface CompanysDAO {
	
	public ArrayList<CompanysVO> comList(PagingVO vo);

	public int countCom();
	

	    CompanysVO findByName(String name); // 회사명으로 회사 조회

	    void insert(CompanysVO vo);         // 회사 신규 등록


}
