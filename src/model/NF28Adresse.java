package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Created by JeanV on 21/03/2016.
 */
public class NF28Adresse implements Externalizable {
    
	private StringProperty voie = new SimpleStringProperty();
	private StringProperty codePostal = new SimpleStringProperty();
	private StringProperty ville = new SimpleStringProperty();
	private StringProperty pays = new SimpleStringProperty();
	
	// voie
	public StringProperty voieProperty(){
		return voie;
	}
	public String getVoie() {
		return voie.get();
	}
	public void setVoie(String voie) {
		this.voie.setValue(voie);
	}

	// CP
	public StringProperty codePostalProperty() {
		return codePostal;
	}
	public String getCodePostal() {
		return codePostal.get();
	}
	public void setCodePostal(String codePostal) {
		this.codePostal.set(codePostal);
	}

	// ville
	public StringProperty villeProperty() {
		return ville;
	}
	public String getVille() {
		return ville.get();
	}
	public void setVille(String ville) {
		this.ville.setValue(ville);
	}

	// pays
	public StringProperty paysProperty() {
		return pays;
	}
	public String getPays() {
		return pays.get();
	}
	public void setPays(String pays) {
		this.pays.setValue(pays);
	}

	//default ctor
	public NF28Adresse(){
		
	}
	
    //copy constructor
    public NF28Adresse(NF28Adresse adresse) {
		this.voie.setValue(adresse.getVoie());
		this.codePostal.setValue(adresse.getCodePostal());
		this.pays.setValue(adresse.getPays());
		this.ville.setValue(adresse.getVille());
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(getVoie());
		out.writeUTF(getCodePostal());
		out.writeUTF(getVille());
		out.writeUTF(getPays());
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

	}
}
