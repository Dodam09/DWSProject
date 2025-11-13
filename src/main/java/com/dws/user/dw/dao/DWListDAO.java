package com.dws.user.dw.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.dws.user.dw.util.PagingVO;
import com.dws.user.dw.vo.DWListVO;

@Mapper
public interface DWListDAO {
//	public ArrayList<DWListVO> dwList();
	
	// 게시물 총 갯수
	public int countDw();

	// 페이징 처리 게시글 조회
	public ArrayList<DWListVO> dwList(PagingVO vo);
	

    void insert(DWListVO vo);           // 작업일보 저장

}
