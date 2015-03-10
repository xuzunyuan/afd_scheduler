/**
 * Copyright (c)2015-? by www.afd.com. All rights reserved.
 * 
 */
package com.afd.schedule.service;

import java.util.Date;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Service;

import com.afd.common.spring.SpringContextUtil;
import com.afd.common.util.DateUtils;
import com.afd.service.product.IBrandShowService;
import com.afd.service.schedule.IBrandShowScheduleService;

/**
 * 注册特卖启停任务服务
 * 
 * @author xuzunyuan
 * @date 2015年3月10日
 */
@Service("brandShowScheduleService")
public class BrandShowScheduleService implements IBrandShowScheduleService {
	@Resource(name = "dbSchedule")
	Scheduler scheduler;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.afd.service.schedule.IBrandShowScheduleService#registerBrandShowStartJob
	 * (int, java.util.Date)
	 */
	@Override
	public int registerBrandShowStartJob(int brandShowId, Date startDate) {
		System.out.println("注册启动特卖任务 || brandShowId:" + brandShowId
				+ "|| startDate:"
				+ DateUtils.formatDate(startDate, DateUtils.DEFAULT_PATTERN));

		JobDetail job = JobBuilder
				.newJob(StartBrandShowJob.class)
				.withIdentity("brandShowStartJob" + brandShowId)
				.usingJobData("brandShowId", brandShowId)
				.usingJobData(
						"startDate",
						DateUtils.formatDate(startDate,
								DateUtils.DEFAULT_PATTERN)).build();

		SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder
				.newTrigger()
				.withIdentity("brandShowStartTrigger" + brandShowId)
				.startAt(startDate)
				.withSchedule(
						SimpleScheduleBuilder.simpleSchedule()
								.withMisfireHandlingInstructionFireNow())
				.build();

		try {
			scheduler.scheduleJob(job, trigger);

		} catch (SchedulerException e) {
			e.printStackTrace();
			return 0;
		}

		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.afd.service.schedule.IBrandShowScheduleService#registerBrandShowEndJob
	 * (int, java.util.Date)
	 */
	@Override
	public int registerBrandShowEndJob(int brandShowId, Date endDate) {
		System.out.println("注册停止特卖任务 || brandShowId:" + brandShowId
				+ "|| endDate:"
				+ DateUtils.formatDate(endDate, DateUtils.DEFAULT_PATTERN));

		JobDetail job = JobBuilder
				.newJob(EndBrandShowJob.class)
				.withIdentity("brandShowEndJob" + brandShowId)
				.usingJobData("brandShowId", brandShowId)
				.usingJobData(
						"endDate",
						DateUtils
								.formatDate(endDate, DateUtils.DEFAULT_PATTERN))
				.build();

		SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder
				.newTrigger()
				.withIdentity("brandShowEndTrigger" + brandShowId)
				.startAt(endDate)
				.withSchedule(
						SimpleScheduleBuilder.simpleSchedule()
								.withMisfireHandlingInstructionFireNow())
				.build();

		try {
			scheduler.scheduleJob(job, trigger);

		} catch (SchedulerException e) {
			e.printStackTrace();
			return 0;
		}

		return 1;
	}

	public static final class StartBrandShowJob implements Job {
		@Override
		public void execute(JobExecutionContext arg0)
				throws JobExecutionException {

			JobDataMap dataMap = arg0.getJobDetail().getJobDataMap();

			int brandShowId = dataMap.getInt("brandShowId");
			String endDate = dataMap.getString("endDate");

			System.out.println("停止特卖 || brandShowId:" + brandShowId
					+ "|| endDate:" + endDate + "  执行！");

			try {
				SpringContextUtil.getBean(IBrandShowService.class).endBrandSow(
						brandShowId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static final class EndBrandShowJob implements Job {
		@Override
		public void execute(JobExecutionContext arg0)
				throws JobExecutionException {

			JobDataMap dataMap = arg0.getJobDetail().getJobDataMap();

			int brandShowId = dataMap.getInt("brandShowId");
			String startDate = dataMap.getString("startDate");

			System.out.println("启动特卖 || brandShowId:" + brandShowId
					+ "|| startDate:" + startDate + "  执行！");

			try {
				SpringContextUtil.getBean(IBrandShowService.class)
						.startBrandShow(brandShowId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
