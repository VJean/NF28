package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Model {
	private final ObservableList<NF28Groupe> groups = FXCollections.observableArrayList();
	private final ObservableMap<String, String> validationErrors = FXCollections.observableHashMap();

	public ObservableList<NF28Groupe> getGroups() {
		return groups;
	}

//	public void setGroups(ObservableList<NF28Groupe> groups) {
//		this.groups = groups;
//	}

	public static ArrayList<NF28Groupe> getAvailableGroups() {
		return null;
	}

	public ObservableMap<String, String> getValidationErrors() {
		return validationErrors;
	}

	/**
	 * Permet de déterminer si un contact est valide, en contrôlant la valeur de ses champs.
	 * @return true si le contact est valide, false sinon (regarder le contenu de validationErrors dans ce cas)
	 */
	public boolean validateContact(NF28Contact c){
		boolean valid = true;
		validationErrors.clear();

		// nom ne doit pas être vide
		if (c.getNom() == null || c.getNom().isEmpty()) {
			valid = false;
			validationErrors.put("nom", "Le nom ne peut pas être vide");
		}

		// nom ne doit pas être vide
		if (c.getPrenom() == null || c.getPrenom().isEmpty()) {
			valid = false;
			validationErrors.put("prenom", "Le prénom ne peut pas être vide");
		}

		// voie ne doit pas être vide
		if (c.getAdresse().getVoie() == null || c.getAdresse().getVoie().isEmpty()) {
			valid = false;
			validationErrors.put("voie", "La voie ne peut pas être vide");
		}

		// codePostal ne doit pas être vide et est composé de chiffres
		if (c.getAdresse().getCodePostal() == null || c.getAdresse().getCodePostal().isEmpty()) {
			valid = false;
			validationErrors.put("cp", "Le Code Postal ne peut pas être vide");
		} else if (!Pattern.matches("\\d+",c.getAdresse().getCodePostal())) {
			valid = false;
			validationErrors.put("cp", "Un code postal est composé de chiffres");
		}

		// ville ne doit pas être vide
		if (c.getAdresse().getVille() == null || c.getAdresse().getVille().isEmpty()) {
			valid = false;
			validationErrors.put("ville", "La ville ne peut pas être vide");
		}

		// pays ne doit pas être vide
		if (c.getAdresse().getPays() == null || c.getAdresse().getPays().isEmpty()) {
			valid = false;
			validationErrors.put("pays", "Il faut choisir un pays");
		}

		// sexe ne doit pas être vide
		if (c.getSexe() == null || c.getSexe().isEmpty()) {
			valid = false;
			validationErrors.put("sexe", "Il faut renseigner un genre");
		}

		// date ne doit pas être vide et doit être dans le passé
		if (c.getDateNaissance() == null || c.getDateNaissance().isAfter(LocalDate.now())) {
			valid = false;
			validationErrors.put("date", "Indiquer une date de naissance (avant aujourd'hui)");
		}

		return valid;
	}

	public void openFile(File f) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));

			try {
				groups.setAll((NF28Groupe[]) ois.readObject());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveFile(File f) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
			oos.writeObject(groups.toArray(new NF28Groupe[groups.size()]));
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
