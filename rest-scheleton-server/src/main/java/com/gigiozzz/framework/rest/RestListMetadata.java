package com.gigiozzz.framework.rest;


public class RestListMetadata {

	private Integer limit;
	private Integer offset;
	private Long total;

	public RestListMetadata(Integer limit,Integer offset,Long total){
		this.limit=limit;
		this.offset=offset;
		this.total=total;
	}
	
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	
	

}