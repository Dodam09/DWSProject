package com.dws.user.dw.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.dws.user.dw.vo.TestVO;

@Mapper
public interface TestDAO {
	
	public List<TestVO> test();
}
