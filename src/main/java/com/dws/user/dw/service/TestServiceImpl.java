package com.dws.user.dw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dws.user.dw.dao.TestDAO;
import com.dws.user.dw.vo.TestVO;

import lombok.Setter;

@Service
public class TestServiceImpl implements TestService {
	
	@Setter(onMethod_ = @Autowired )
	private TestDAO testDao;
	
	@Override
	public List<TestVO> test() {
		List<TestVO> tvo = testDao.test();
		return tvo;
	}

}
