package com.database.model.bean;

public class PvnAward {
	private Integer id;
	private Long min;
	private Long max;
	private Long reward;
	private Boolean change;

	public PvnAward(Integer id, Long min, Long max, Long reward, Boolean change) {
		this.id = id;
		this.min = min;
		this.max = max;
		this.reward = reward;
		this.change = change;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getMin() {
		return min;
	}

	public void setMin(Long min) {
		this.min = min;
	}

	public Long getMax() {
		return max;
	}

	public void setMax(Long max) {
		this.max = max;
	}

	public Long getReward() {
		return reward;
	}

	public void setReward(Long reward) {
		this.reward = reward;
	}

	public Boolean getChange() {
		return change;
	}

	public void setChange(Boolean change) {
		this.change = change;
	}

}
