package controller;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import model.Model;
import model.NF28Contact;
import model.NF28Groupe;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import model.NF28Country;
import sun.reflect.generics.tree.Tree;


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
	TreeItem<Object> currentGroupeItem;
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
		editingPanel.setVisible(false);

		groupsView.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> {
					if (newValue.getValue() instanceof NF28Groupe)
						currentGroupeItem = newValue;
					else if (newValue.getValue() instanceof NF28Contact)
						currentGroupeItem = newValue.getParent();
				}
		);

		fieldNom.textProperty().addListener(
				(obs,oldval,newval) -> this.currentContact.setNom(newval)
		);
		fieldPrenom.textProperty().addListener(
				(obs,oldval,newval) -> this.currentContact.setPrenom(newval)
		);
		fieldVoie.textProperty().addListener(
				(observable, oldValue, newValue) -> this.currentContact.getAdresse().setVoie(newValue)
		);
		fieldVille.textProperty().addListener(
				(observable, oldValue, newValue) -> this.currentContact.getAdresse().setVille(newValue)
		);
		fieldCP.textProperty().addListener(
				(observable, oldValue, newValue) -> this.currentContact.getAdresse().setCodePostal(newValue)
		);
		choicePays.getSelectionModel().selectedItemProperty().addListener(
				(obs,oldval,newval) -> this.currentContact.getAdresse().setPays(newval)
		);
		radioGroupSexe.selectedToggleProperty().addListener(
				(obs,oldval,newval) -> this.currentContact.setSexe(newval.toString())
		);
		fieldDate.valueProperty().addListener(
				(observable, oldValue, newValue) -> this.currentContact.setDateNaissance(newValue)
		);
				
		ListChangeListener<NF28Contact> contactChange = change -> {
			change.next();
			if (change.wasRemoved()) {
				// remove corresponding TreeItems
			}
			else if (change.wasAdded()) { // add corresponding Contact TreeItems
				change.getAddedSubList().forEach(item -> {
					// considérer le groupe selectionné actuellement, ou bien le père du contact selectionné actuellement
					TreeItem<Object> c = new TreeItem<Object>(item, new ImageView("file:res/contact.png"));
					this.currentGroupeItem.getChildren().add(c);
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
					TreeItem<Object> g = new TreeItem<Object>(item, new ImageView("file:res/group.png"));
					this.groupsView.getRoot().getChildren().add(g);
					item.getContacts().addListener(contactChange);
				});
			}
		};
		
		model.getGroups().addListener(groupChange);

		// bind to model validation map
		listenToValidationErrors();

	}

	private void listenToValidationErrors() {
		MapChangeListener<String,String> listener;

		listener = changed -> {
			if(changed.wasAdded()) {
				switch (changed.getKey().toString()) {
					case "nom":
						fieldNom.setStyle("-fx-border-color: red");
						fieldNom.setTooltip(new Tooltip(changed.getValueAdded()));
						break;
					case "prenom":
						fieldPrenom.setStyle("-fx-border-color: red");
						fieldPrenom.setTooltip(new Tooltip(changed.getValueAdded()));
						break;
					case "voie":
						fieldVoie.setStyle("-fx-border-color: red");
						fieldVoie.setTooltip(new Tooltip(changed.getValueAdded()));
						break;
					case "cp":
						fieldCP.setStyle("-fx-border-color: red");
						fieldCP.setTooltip(new Tooltip(changed.getValueAdded()));
						break;
					case "ville":
						fieldVille.setStyle("-fx-border-color: red");
						fieldVille.setTooltip(new Tooltip(changed.getValueAdded()));
						break;
					case "pays":
						choicePays.setStyle("-fx-border-color: red");
						choicePays.setTooltip(new Tooltip(changed.getValueAdded()));
						break;
					case "sexe":
						radioF.setStyle("-fx-border-color: red");
						radioF.setTooltip(new Tooltip(changed.getValueAdded()));
						radioM.setStyle("-fx-border-color: red");
						radioM.setTooltip(new Tooltip(changed.getValueAdded()));
						break;
					case "date":
						fieldDate.setStyle("-fx-border-color: red");
						fieldDate.setTooltip(new Tooltip(changed.getValueAdded()));
						break;
				}
				System.out.println("!!  " + changed.getKey() + ":\t" + changed.getValueAdded());
			} else if(changed.wasRemoved()) {
				switch (changed.getKey().toString()) {
					case "nom":
						fieldNom.setStyle("-fx-border-color: none");
						fieldNom.setTooltip(new Tooltip(""));
						break;
					case "prenom":
						fieldPrenom.setStyle("-fx-border-color: none");
						fieldPrenom.setTooltip(new Tooltip(""));
						break;
					case "voie":
						fieldVoie.setStyle("-fx-border-color: none");
						fieldVoie.setTooltip(new Tooltip(""));
						break;
					case "cp":
						fieldCP.setStyle("-fx-border-color: none");
						fieldCP.setTooltip(new Tooltip(""));
						break;
					case "ville":
						fieldVille.setStyle("-fx-border-color: none");
						fieldVille.setTooltip(new Tooltip(""));
						break;
					case "pays":
						choicePays.setStyle("-fx-border-color: none");
						choicePays.setTooltip(new Tooltip(""));
						break;
					case "sexe":
						radioF.setStyle("-fx-border-color: none");
						radioF.setTooltip(new Tooltip(""));
						radioM.setStyle("-fx-border-color: none");
						radioM.setTooltip(new Tooltip(""));
						break;
					case "date":
						fieldDate.setStyle("-fx-border-color: none");
						fieldDate.setTooltip(new Tooltip(""));
						break;
				}
			}
		};
		model.getValidationErrors().addListener(listener);
	}

	public void addTreeItem() {
		TreeItem<Object> treeItem = groupsView.getSelectionModel().selectedItemProperty().getValue();
		if (treeItem == null)
			return;
		
		// l'item sélectionné est la racine
		if (treeItem.getValue() instanceof String){
			NF28Groupe newGroupe = new NF28Groupe();
			model.getGroups().add(newGroupe);
		} else { // l'item sélectionné est un contact ou un groupe

			currentContact = new NF28Contact();
			editingPanel.setVisible(true);
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
		if (groupsView.getSelectionModel().selectedItemProperty().getValue().getValue() instanceof NF28Groupe){
			// valider le contact
			if (model.validateContact(currentContact)) {
				// ajouter le contact au groupe sélectionné
				((NF28Groupe) currentGroupeItem.getValue()).getContacts().add(new NF28Contact(currentContact));
				// cacher le panel d'édition
				editingPanel.setVisible(false);
			}
		} else {
			// avertir l'utilisateur qu'il faut sélectionner un groupe :
			Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez sélectionner le groupe dans lequel vous souhaitez enregistrer ce contact.");
			alert.showAndWait();
		}
	}
}
