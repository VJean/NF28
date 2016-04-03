package model;

import java.time.LocalDate;
import java.util.Date;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class NF28Contact {
	// ref to parent NF28Groupe ?
		
	private StringProperty nom = new SimpleStringProperty();
	private StringProperty prenom = new SimpleStringProperty();
	private StringProperty sexe = new SimpleStringProperty();
	private NF28Adresse adresse = new NF28Adresse();
	private LocalDate dateNaissance = LocalDate.MAX;

	public StringProperty nomProperty() {
		return nom;
	}
	public String getNom(){
		return nom.get();
	}
	public void setNom(String nom) {
		this.nom.setValue(nom);
	}

	public StringProperty prenomProperty() {
		return prenom;
	}
	public String getPrenom() {
		return prenom.get();
	}
	public void setPrenom(String prenom) {
		this.prenom.setValue(prenom);
	}

	public StringProperty sexeProperty() {
		return sexe;
	}
	public String getSexe() {
		return sexe.get();
	}
	public void setSexe(String sexe) {
		this.sexe.setValue(sexe);
	}

	public NF28Adresse getAdresse() {
		return adresse;
	}
	public void setAdresse(NF28Adresse adresse) {
		this.adresse = adresse;
	}

	public LocalDate getDateNaissance() {
		return dateNaissance;
	}
	public void setDateNaissance(LocalDate dateNaissance) {
		this.dateNaissance = dateNaissance;
	}


	public NF28Contact(){
	}

	public NF28Contact(NF28Contact c){
		this.nom.setValue(c.getNom());
		this.prenom.setValue(c.getPrenom());
		this.sexe.setValue(c.getSexe());
		this.adresse = new NF28Adresse(c.getAdresse());
		this.dateNaissance = LocalDate.of(c.getDateNaissance().getYear(),c.getDateNaissance().getMonth(),c.getDateNaissance().getDayOfMonth());
	}

}
