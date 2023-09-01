package com.dws.user.dw.service;

import java.util.ArrayList;

import com.dws.user.dw.util.PagingVO;
import com.dws.user.dw.vo.DWListVO;

public interface DWListService {

//	public ArrayList<DWListVO> dwList();
	
	// 게시물 총 갯수
	public int countDw();

	// 페이징 처리 게시글 조회
	public ArrayList<DWListVO> dwList(PagingVO vo);
}
