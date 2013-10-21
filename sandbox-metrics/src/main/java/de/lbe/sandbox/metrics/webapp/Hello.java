package de.lbe.sandbox.metrics.webapp;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 */
@XmlRootElement
public class Hello {

	private String message;

	private long counter;

	private double meanRequestRate;

	private long duration;
	
	private long gauge;

	public long getGauge() {
		return this.gauge;
	}

	public void setGauge(long gauge) {
		this.gauge = gauge;
	}

	public long getDuration() {
		return this.duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public double getMeanRequestRate() {
		return this.meanRequestRate;
	}

	public void setMeanRequestRate(double meanRequestRate) {
		this.meanRequestRate = meanRequestRate;
	}

	public long getCounter() {
		return this.counter;
	}

	public void setCounter(long counter) {
		this.counter = counter;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
