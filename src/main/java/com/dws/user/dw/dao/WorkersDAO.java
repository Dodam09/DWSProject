package com.dws.user.dw.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.dws.user.dw.util.PagingVO;
import com.dws.user.dw.vo.WorkersVO;


@Mapper
public interface WorkersDAO {
	
	public ArrayList<WorkersVO> worList(PagingVO vo);

	public int countWor();
	
}
