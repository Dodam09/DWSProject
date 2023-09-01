package com.dws.user.dw.vo;

import java.sql.Date;

import lombok.Data;

@Data
public class DWListVO {
	private int w_no; 
    private String wor_name; 
    private String com_name; 
    private int w_allo; 
    private int w_price; 
    private String w_price_how ;
    private String w_memo; 
    private Date w_day; 

}
