package com.dws.user.dw.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.dws.user.dw.util.PagingVO;
import com.dws.user.dw.vo.SearcherVO;

@Mapper
public interface SearcherDAO {
	public ArrayList<SearcherVO> searcherList(PagingVO vo);
	public ArrayList<SearcherVO> searcherSearch(String wor_name);
	
	public int countSearcherSearch(String wor_name);
	public int countSearcher();
}
