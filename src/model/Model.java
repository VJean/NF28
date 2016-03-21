package model;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model {
	private ObservableList<NF28Groupe> groups = FXCollections.observableArrayList();
	
	public ObservableList<NF28Groupe> getGroups() {
		return groups;
	}

	public void setGroups(ObservableList<NF28Groupe> groups) {
		this.groups = groups;
	}

	public static ArrayList<NF28Groupe> getAvailableGroups() {
		return null;
	}
}
