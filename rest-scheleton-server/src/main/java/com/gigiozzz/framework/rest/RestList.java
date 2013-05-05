package com.gigiozzz.framework.rest;

import java.util.List;

public class RestList<T> {

	private List<T> elements;
	private RestListMetadata metadata;

	public List<T> getElements() {
		return elements;
	}
	public void setElements(List<T> elements) {
		this.elements = elements;
	}
	public RestListMetadata getMetadata() {
		return metadata;
	}
	public void setMetadata(RestListMetadata metadata) {
		this.metadata = metadata;
	}
		

}