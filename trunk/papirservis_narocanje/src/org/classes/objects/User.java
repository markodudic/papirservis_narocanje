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
public class User {

	public User(){
		
	}
	
	@Null
	@Size(min = 1, max = 255)
	private String id;

	@NotNull
	@Size(min = 1, max = 255)
	private String name;

	@NotNull
	@Size(min = 1, max = 255)
	private String surname;

	@NotNull
	@Size(min = 1, max = 255)
	private String status;

	@Null
	@Size(min = 1, max = 255)
	private String enota;
	
	@Null
	@Size(min = 1, max = 255)
	private String narocila;

	@Null
	@Size(min = 1, max = 255)
	private String sif_kupca;

	public String getEnota() {
		return enota;
	}

	public void setEnota(String enota) {
		this.enota = enota;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSif_kupca() {
		return sif_kupca;
	}

	public void setSif_kupca(String sif_kupca) {
		this.sif_kupca = sif_kupca;
	}

	public String getNarocila() {
		return narocila;
	}

	public void setNarocila(String narocila) {
		this.narocila = narocila;
	}
	

}