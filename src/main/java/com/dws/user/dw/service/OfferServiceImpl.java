package com.dws.user.dw.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dws.user.dw.dao.OfferDAO;
import com.dws.user.dw.util.PagingVO;
import com.dws.user.dw.vo.OfferVO;

import lombok.Setter;


@Service
public class OfferServiceImpl implements OfferService {

	@Setter(onMethod_ = @Autowired )
	private OfferDAO offerDao;
	
	@Override
	public int countOffer() {
		return offerDao.countOffer();
	}

	@Override
	public ArrayList<OfferVO> offerList(PagingVO vo) {
		ArrayList<OfferVO> ovo = offerDao.offerList(vo);		
		return ovo;
	}
	
	@Override
	public ArrayList<OfferVO> searchOffer(String com_name ){
		ArrayList<OfferVO> ovo = offerDao.searchOffer(com_name);
		return ovo;
	}
	
	public int countOfferSearch(String com_name) {
		return offerDao.countOfferSearch(com_name);
	}

}
