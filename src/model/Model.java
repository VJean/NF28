package model;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class Model {
	private ObservableList<NF28Groupe> groups = FXCollections.observableArrayList();
	private ObservableMap<String, String> validationErrors = FXCollections.observableHashMap();

	public ObservableList<NF28Groupe> getGroups() {
		return groups;
	}

	public void setGroups(ObservableList<NF28Groupe> groups) {
		this.groups = groups;
	}

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

		return valid;
	}
}
