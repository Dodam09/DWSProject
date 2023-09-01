package com.dws.user.dw.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.dws.user.dw.util.PagingVO;
import com.dws.user.dw.vo.OfferVO;

@Mapper
public interface OfferDAO {
	public ArrayList<OfferVO> offerList(PagingVO vo);
	public int countOffer();
	public ArrayList<OfferVO> searchOffer(String com_name);
	public int countOfferSearch(String com_name);
	
}
