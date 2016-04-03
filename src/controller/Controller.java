package controller;

import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import model.Model;
import model.NF28Contact;
import model.NF28Groupe;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.NF28Country;


public class Controller {
	@FXML
	TextField fieldNom;
	@FXML
	TextField fieldPrenom;
	@FXML
	TextField fieldVoie;
	@FXML
	TextField fieldCP;
	@FXML
	TextField fieldVille;
	@FXML
	ChoiceBox<String> choicePays;
	@FXML
	RadioButton radioF;
	@FXML
	RadioButton radioM;
	ToggleGroup radioGroupSexe;
	@FXML
	DatePicker fieldDate;
	@FXML
	TreeView<Object> groupsView;
	@FXML
	VBox editingPanel;
	
	
    Model model;
    NF28Contact currentContact;

    public Controller(){
    	model = new Model();
    }
    
	public void initialize(){
		System.out.println("initializing controller");
		choicePays.setItems(FXCollections.observableArrayList(NF28Country.getCountries()));

		radioGroupSexe = new ToggleGroup();
		radioM.setToggleGroup(radioGroupSexe);
		radioF.setToggleGroup(radioGroupSexe);

		TreeItem<Object> root = new TreeItem<Object>("Fiche de contacts");
		groupsView.setRoot(root);
		editingPanel.visibleProperty().set(false);


		fieldNom.textProperty().addListener(
				(obs,oldval,newval) -> { this.currentContact.setNom(newval); }
		);
		fieldPrenom.textProperty().addListener(
				(obs,oldval,newval) -> { this.currentContact.setPrenom(newval); }
		);
		radioGroupSexe.selectedToggleProperty().addListener(
				(obs,oldval,newval) -> { this.currentContact.setSexe(newval.toString()); }
		);
		choicePays.getSelectionModel().selectedItemProperty().addListener(
				(obs,oldval,newval) -> { this.currentContact.setCountry(newval); }
		);
		
				
		ListChangeListener<NF28Contact> contactChange = change -> {
			change.next();
			if (change.wasRemoved()) {
				// remove corresponding TreeItems
			}
			else if (change.wasAdded()) { // add corresponding Contact TreeItems
				change.getAddedSubList().forEach(item -> {
					// considérer le groupe selectionné actuellement, ou bien le père du contact selectionné actuellement
				});
			}
		};
		
		ListChangeListener<NF28Groupe> groupChange = change -> {
			change.next();
			if (change.wasRemoved()) {
				// remove corresponding TreeItems
			}
			else if (change.wasAdded()) { // add corresponding Groups TreeItems
				change.getAddedSubList().forEach(item -> {
					TreeItem<Object> g = new TreeItem<Object>(item);
					this.groupsView.getRoot().getChildren().add(g);
					item.getContacts().addListener(contactChange);
				});
			}
		};
		
		model.getGroups().addListener(groupChange);

	}

	public void addTreeItem() {
		if (groupsView.getSelectionModel().selectedItemProperty().getValue() == null)
			return;
		
		// l'item selecionné est la racine
		if (groupsView.getSelectionModel().selectedItemProperty().getValue().getValue().getClass() == String.class){
			NF28Groupe newGroupe = new NF28Groupe();
			model.getGroups().add(newGroupe);
		} else { // l'item selectionné est un contact ou un groupe
			currentContact = new NF28Contact();
			editingPanel.visibleProperty().set(true);
			this.reset();
		}
	}
	
	private void reset() {
		fieldNom.setText("");
		fieldPrenom.setText("");
		fieldCP.setText("");
		fieldDate.setValue(null);
		fieldVille.setText("");
		fieldVoie.setText("");
		radioGroupSexe.selectToggle(radioF);
		choicePays.setItems(FXCollections.observableArrayList(NF28Country.getCountries()));
	}

	public void removeTreeItem() {
		
	}
	
	public void saveContact(){
		if (groupsView.getSelectionModel().selectedItemProperty().getValue() == null)
			return;

		// on ajoute le contact au groupe selectionné, s'il y en a un.
		if (groupsView.getSelectionModel().selectedItemProperty().getValue().getValue().getClass() == NF28Groupe.class){
			// référencer le groupe sélectionné
			NF28Groupe currentGroupe = (NF28Groupe) groupsView.getSelectionModel().selectedItemProperty().getValue().getValue();

			// valider le contact
			if (model.validateContact(currentContact)) {
				currentGroupe.getContacts().add(new NF28Contact(currentContact));
			}
		} else {
			// avertir l'utilisateur qu'il faut sélectionner un groupe :
			Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez sélectionner le groupe dans lequel vous souhaitez enregistrer ce contact.");
			alert.showAndWait();
		}
	}
}
