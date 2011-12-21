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
public class Subject {

	public Subject(){
		
	}
	
	@Null
	@Size(min = 1, max = 255)
	private String sifra;

	@NotNull
	@Size(min = 1, max = 255)
	private String naziv;

	@NotNull
	@Size(min = 1, max = 255)
	private String naslov;

	@NotNull
	@Size(min = 1, max = 255)
	private String posta;

	@NotNull
	@Size(min = 1, max = 255)
	private String kraj;

	@NotNull
	@Size(min = 1, max = 255)
	private String telefon;

	@NotNull
	@Size(min = 1, max = 255)
	private String kontOseba;

	@NotNull
	@Size(min = 1, max = 255)
	private String osnovna;

	@NotNull
	@Size(min = 1, max = 255)
	private String kol_osnovna;

	@NotNull
	@Size(min = 1, max = 255)
	private String kupec;

	@NotNull
	@Size(min = 1, max = 255)
	private String skupina;

	public String getSifra() {
		return sifra;
	}

	public void setSifra(String sifra) {
		this.sifra = sifra;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getNaslov() {
		return naslov;
	}

	public void setNaslov(String naslov) {
		this.naslov = naslov;
	}

	public String getPosta() {
		return posta;
	}

	public void setPosta(String posta) {
		this.posta = posta;
	}

	public String getKraj() {
		return kraj;
	}

	public void setKraj(String kraj) {
		this.kraj = kraj;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public String getKontOseba() {
		return kontOseba;
	}

	public void setKontOseba(String kontOseba) {
		this.kontOseba = kontOseba;
	}

	public String getOsnovna() {
		return osnovna;
	}

	public void setOsnovna(String osnovna) {
		this.osnovna = osnovna;
	}

	public String getKol_osnovna() {
		return kol_osnovna;
	}

	public void setKol_osnovna(String kol_osnovna) {
		this.kol_osnovna = kol_osnovna;
	}

	public String getKupec() {
		return kupec;
	}

	public void setKupec(String kupec) {
		this.kupec = kupec;
	}

	public String getSkupina() {
		return skupina;
	}

	public void setSkupina(String skupina) {
		this.skupina = skupina;
	}

	
	
}