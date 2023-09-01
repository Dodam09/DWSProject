package com.dws.user.dw.service;

import java.util.ArrayList;

import com.dws.user.dw.util.PagingVO;
import com.dws.user.dw.vo.OfferVO;


public interface OfferService {
	
	public ArrayList<OfferVO> offerList(PagingVO vo);


	public int countOffer();


	public ArrayList<OfferVO> searchOffer(String com_name  );


	public int countOfferSearch(String com_name);

}
