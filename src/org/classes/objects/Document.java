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
public class Document {

	public Document(){
		
	}
	
	@NotNull
	@Size(min = 1, max = 255)
	private String dobavitelj;

	@NotNull
	@Size(min = 1, max = 255)
	private String naslov;

	@NotNull
	@Size(min = 1, max = 255)
	private String narocilo;
	
	@NotNull
	@Size(min = 1, max = 255)
	private String evl;

	@NotNull
	@Size(min = 1, max = 25)
	private String datum_narocila;
	
	@NotNull
	@Size(min = 1, max = 25)
	private String datum_izvedbe;

	@NotNull
	@Size(min = 1, max = 255)
	private String vrsta_odpadka;

	@NotNull
	@Size(min = 1, max = 255)
	private String klasifikacijska_stevilka;
	
	@NotNull
	private int kolicina;
	
	@NotNull
	@Size(min = 1, max = 255)
	private String placnik;
	
	@NotNull
	@Size(min = 1, max = 255)
	private String kontakt;
	
	@NotNull
	@Size(min = 1, max = 255)
	private String opomba;

	@NotNull
	@Size(min = 1, max = 2)
	private String status;

	@NotNull
	@Size(min = 1, max = 255)
	private String vozilo;

	
	public String getDobavitelj() {
		return dobavitelj;
	}

	public void setDobavitelj(String dobavitelj) {
		this.dobavitelj = dobavitelj;
	}

	public String getNaslov() {
		return naslov;
	}

	public void setNaslov(String naslov) {
		this.naslov = naslov;
	}

	public String getNarocilo() {
		return narocilo;
	}

	public void setNarocilo(String narocilo) {
		this.narocilo = narocilo;
	}

	public String getDatum_narocila() {
		return datum_narocila;
	}

	public void setDatum_narocila(String datum_narocila) {
		this.datum_narocila = datum_narocila;
	}

	public String getDatum_izvedbe() {
		return datum_izvedbe;
	}

	public void setDatum_izvedbe(String datum_izvedbe) {
		this.datum_izvedbe = datum_izvedbe;
	}

	public String getVrsta_odpadka() {
		return vrsta_odpadka;
	}

	public void setVrsta_odpadka(String vrsta_odpadka) {
		this.vrsta_odpadka = vrsta_odpadka;
	}

	public String getKlasifikacijska_stevilka() {
		return klasifikacijska_stevilka;
	}

	public void setKlasifikacijska_stevilka(String klasifikacijska_stevilka) {
		this.klasifikacijska_stevilka = klasifikacijska_stevilka;
	}

	public int getKolicina() {
		return kolicina;
	}

	public void setKolicina(int kolicina) {
		this.kolicina = kolicina;
	}

	public String getPlacnik() {
		return placnik;
	}

	public void setPlacnik(String placnik) {
		this.placnik = placnik;
	}

	public String getKontakt() {
		return kontakt;
	}

	public void setKontakt(String kontakt) {
		this.kontakt = kontakt;
	}

	public String getOpomba() {
		return opomba;
	}

	public void setOpomba(String opomba) {
		this.opomba = opomba;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVozilo() {
		return vozilo;
	}

	public void setVozilo(String vozilo) {
		this.vozilo = vozilo;
	}

	public String getEvl() {
		return evl;
	}

	public void setEvl(String evl) {
		this.evl = evl;
	}

	
	

}