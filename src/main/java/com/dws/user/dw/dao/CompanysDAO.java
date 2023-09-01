package com.dws.user.dw.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.dws.user.dw.util.PagingVO;
import com.dws.user.dw.vo.CompanysVO;

@Mapper
public interface CompanysDAO {
	
	public ArrayList<CompanysVO> comList(PagingVO vo);

	public int countCom();

}
