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
public class Order {

	public Order(){
		
	}
	@Null
	@Size(min = 1, max = 255)
	private String stDob;

	@NotNull
	@Size(min = 1, max = 255)
	private String datum;

	@NotNull
	@Size(min = 1, max = 255)
	private String stranka;

	@NotNull
	@Size(min = 1, max = 255)
	private String kupec;

	@NotNull
	@Size(min = 1, max = 255)
	private String material;

	@NotNull
	@Size(min = 1, max = 255)
	private String kolicina;

	@NotNull
	@Size(min = 1, max = 255)
	private String opomba;

	@NotNull
	@Size(min = 1, max = 255)
	private String narocil;

	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

	public String getStranka() {
		return stranka;
	}

	public void setStranka(String stranka) {
		this.stranka = stranka;
	}

	public String getKupec() {
		return kupec;
	}

	public void setKupec(String kupec) {
		this.kupec = kupec;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getKolicina() {
		return kolicina;
	}

	public void setKolicina(String kolicina) {
		this.kolicina = kolicina;
	}

	public String getOpomba() {
		return opomba;
	}

	public void setOpomba(String opomba) {
		this.opomba = opomba;
	}

	public String getStDob() {
		return stDob;
	}

	public void setStDob(String stDob) {
		this.stDob = stDob;
	}

	public String getNarocil() {
		return narocil;
	}

	public void setNarocil(String narocil) {
		this.narocil = narocil;
	}



	
}