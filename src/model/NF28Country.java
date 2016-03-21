package model;

import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class NF28Country {
	private StringProperty nom;

	public StringProperty nomProperty() {
		return nom;
	}
	public String getNom(){
		return nom.get();
	}
	public void setNom(String nom) {
		this.nom.setValue(nom);
	}

	public static List<String> getCountries(){
		List<String> countryNames = new LinkedList<>();
		String[] locales = Locale.getISOCountries();
		
		countryNames = Arrays.stream(locales).map(countryCode -> (new Locale("", countryCode)
							.getDisplayCountry()))
							.collect(Collectors.toList());

		return countryNames;
	}
}
