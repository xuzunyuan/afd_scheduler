package com.afd.schedule.order;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.afd.service.payment.IPaymentServices;

/**
 * 
 * 取消未支付订单（24小时）
 * @date 2014年10月15日
 */
public class CancelNoPayOrder {

	private static final Logger logger = LoggerFactory.getLogger(CancelNoPayOrder.class);

	@Autowired
	private IPaymentServices paymentServices;
	
	public void cancelOrder(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		logger.error("CancelNoPayOrder--------------startDate:"+df.format(new Date()));
		this.paymentServices.updatelNoPayOrder(1);
		logger.error("CancelNoPayOrder--------------endDate:"+df.format(new Date()));
		
	}
}
