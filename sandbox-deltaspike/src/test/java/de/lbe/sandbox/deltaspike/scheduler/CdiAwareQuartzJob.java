package de.lbe.sandbox.deltaspike.scheduler;

import javax.inject.Inject;

import org.apache.deltaspike.scheduler.api.Scheduled;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author lbeuster
 */
@Scheduled(cronExpression = "* * * * * ?")
public class CdiAwareQuartzJob implements Job {

	@Inject
	private ScheduledService service;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		service.service();
	}
}