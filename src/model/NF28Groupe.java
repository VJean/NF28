package model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class NF28Groupe implements Externalizable {
	private StringProperty nom = new SimpleStringProperty();
	private ObservableList<NF28Contact> contacts = FXCollections.observableArrayList();
	
	public StringProperty nomProperty() {
		return nom;
	}
	public String getNom() {
		return nom.get();
	}
	public void setNom(String nom) {
		this.nom.setValue(nom);
	}
	
	public ObservableList<NF28Contact> getContacts() {
		return contacts;
	}
	public void setContacts(ObservableList<NF28Contact> contacts) {
		this.contacts = contacts;
	}

	public NF28Groupe(String nom){
		this.nom.setValue(nom);
	}
	
	public NF28Groupe(){
		// default name
		this.nom.setValue("Groupe");
	}
	
	@Override
	public String toString(){
		return this.getNom();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(getNom());
		out.writeObject(contacts.toArray(new NF28Contact[contacts.size()]));
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		setNom(in.readUTF());
		getContacts().setAll((NF28Contact[]) in.readObject());
	}
}
