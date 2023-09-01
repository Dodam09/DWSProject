package com.dws.user.dw.service;

import java.util.ArrayList;

import com.dws.user.dw.util.PagingVO;
import com.dws.user.dw.vo.SearcherVO;

public interface SearcherService {
	public ArrayList<SearcherVO> searcherList(PagingVO vo);

	public int countSearcher();
	public int countSearcherSearch(String wor_name);
	public ArrayList<SearcherVO> searcherSearch(String wor_name);
}
