package org.classes.objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

@AutoProperty
public class Customer {

	public Customer(){
		
	}
	
	@Null
	@Size(min = 1, max = 255)
	private String id;

	@NotNull
	@Size(min = 1, max = 255)
	private String skupina;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSkupina() {
		return skupina;
	}

	public void setSkupina(String skupina) {
		this.skupina = skupina;
	}

	
}