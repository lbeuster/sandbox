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
public class ConstructorInjectionQuartzJob implements Job {

	private final ScheduledService service;

	@Inject
	public ConstructorInjectionQuartzJob(ScheduledService service) {
		this.service = service;
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("CDI-aware");
		service.service();
	}
}