package com.dws.user.dw.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dws.user.dw.dao.SearcherDAO;
import com.dws.user.dw.util.PagingVO;
import com.dws.user.dw.vo.SearcherVO;

import lombok.Setter;
@Service
public class SearcherServiceImpl implements SearcherService {
	
	@Setter(onMethod_ = @Autowired )
	private SearcherDAO searcherdao;
	
	@Override
	public int countSearcher() {
		return searcherdao.countSearcher();
	}
	@Override
	public int countSearcherSearch(String wor_name) {
		return searcherdao.countSearcherSearch(wor_name);
	}

	@Override
	public ArrayList<SearcherVO> searcherList(PagingVO vo) {
		ArrayList<SearcherVO> svo = searcherdao.searcherList(vo);
		return svo;
	}
	@Override
	public ArrayList<SearcherVO> searcherSearch(String wor_name) {
		ArrayList<SearcherVO> svo = searcherdao.searcherSearch(wor_name);
		return svo;
	}

}
