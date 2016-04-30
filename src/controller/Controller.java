package controller;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Model;
import model.NF28Contact;
import model.NF28Groupe;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import model.NF28Country;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {
	@FXML
	VBox rootContainer;
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
	Button btnFormValidation;
	@FXML
	TreeView<Object> groupsView;
	@FXML
	VBox editingPanel;

    private Model model;
	private TreeItem<Object> currentGroupeItem;
    private NF28Contact currentContact;
	private File currentFile;

	private enum EditingState {
		IDLE, EDITING, ADDING
	}
	private EditingState state = EditingState.IDLE;

	private Stage getStage() {
		return (Stage) rootContainer.getScene().getWindow();
	}

    public Controller(){
    	model = new Model();
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("initializing controller");
		choicePays.setItems(FXCollections.observableArrayList(NF28Country.getCountries()));

		radioGroupSexe = new ToggleGroup();
		radioM.setToggleGroup(radioGroupSexe);
		radioF.setToggleGroup(radioGroupSexe);

		TreeItem<Object> root = new TreeItem<>("Fiche de contacts");
		groupsView.setRoot(root);
		groupsView.setCellFactory(param -> new TextFieldTreeCellImpl());
		groupsView.setEditable(true);

		setIdleState();

		groupsView.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> {
					if (newValue.getValue() instanceof NF28Groupe) {
						currentGroupeItem = newValue;
					} else if (newValue.getValue() instanceof NF28Contact) {
						currentGroupeItem = newValue.getParent();
						setEditingState();
						// fill editing panel with selected contact's properties
						NF28Contact c = (NF28Contact) newValue.getValue();
						fieldNom.setText(c.getNom());
						fieldPrenom.setText(c.getPrenom());
						fieldVoie.setText(c.getAdresse().getVoie());
						fieldVille.setText(c.getAdresse().getVille());
						fieldCP.setText(c.getAdresse().getCodePostal());
						choicePays.setValue(c.getAdresse().getPays());
						fieldDate.setValue(c.getDateNaissance());
						if (c.getSexe().equals("M"))
							radioGroupSexe.selectToggle(radioM);
						else
							radioGroupSexe.selectToggle(radioF);
					}
				}
		);

		listenToEditingPanel();

		ListChangeListener<NF28Contact> contactChange = change -> {
			change.next();
			if (change.wasRemoved()) {
				// TODO remove corresponding TreeItems
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
				// TODO remove corresponding TreeItems
			}
			else if (change.wasAdded()) { // add corresponding Groups TreeItems
				change.getAddedSubList().forEach(item -> {
					TreeItem<Object> g = new TreeItem<Object>(item, new ImageView("file:res/group.png"));
					this.groupsView.getRoot().getChildren().add(g);

					if (!item.getContacts().isEmpty()) {
						for (NF28Contact contact : item.getContacts()) {
							TreeItem<Object> c = new TreeItem<Object>(contact, new ImageView("file:res/contact.png"));
							g.getChildren().add(c);
						}
					}

					item.getContacts().addListener(contactChange);
				});
			}
		};
		
		model.getGroups().addListener(groupChange);

		// bind to model validation map
		listenToValidationErrors();

	}

	private void listenToEditingPanel() {
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
			setAddingState();
			this.reset();
		}
	}

	private void setAddingState() {
		editingPanel.setVisible(true);
		btnFormValidation.setText("Add contact");
		state = EditingState.ADDING;
	}

	private void setEditingState() {
		editingPanel.setVisible(true);
		btnFormValidation.setText("Save modifications");
		state = EditingState.EDITING;
	}

	private void setIdleState() {
		editingPanel.setVisible(false);
		state = EditingState.IDLE;
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

		if (state == EditingState.ADDING) {
			// on ajoute le contact au groupe selectionné, s'il y en a un.
			if (groupsView.getSelectionModel().selectedItemProperty().getValue().getValue() instanceof NF28Groupe){
				// valider le contact
				if (model.validateContact(currentContact)) {
					// ajouter le contact au groupe sélectionné
					((NF28Groupe) currentGroupeItem.getValue()).getContacts().add(new NF28Contact(currentContact));
					setIdleState();
				}
			} else {
				// avertir l'utilisateur qu'il faut sélectionner un groupe :
				Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez sélectionner le groupe dans lequel vous souhaitez enregistrer ce contact.");
				alert.showAndWait();
			}
		}
		else if (state == EditingState.EDITING) {

		}
	}

	public void openFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open database");
		//fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
		File f = fileChooser.showOpenDialog(getStage());

		if (f != null) {
			model.openFile(f);
			currentFile = f;
		}
	}

	public void saveFileAs() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save database");
		//fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
		File f = fileChooser.showSaveDialog(getStage());

		if (f != null) {
			model.saveFile(f);
			currentFile = f;
		}
	}

	private final class TextFieldTreeCellImpl extends TreeCell<Object> {
		 
        private TextField textField;
 
        public TextFieldTreeCellImpl() {
        }
 
        @Override
        public void startEdit() {
            super.startEdit();
            
            // on ne peut modifier que les items de groupe
            if(getTreeItem() == null || !(getTreeItem().getValue() instanceof NF28Groupe)){
            	return;
            }
            if (textField == null) {
                createTextField();
            }
            setText(null);
            setGraphic(textField);
            textField.selectAll();
        }
 
        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setText(getItem().toString());
            setGraphic(getTreeItem().getGraphic());
        }
 
        @Override
        public void updateItem(Object item, boolean empty) {
            super.updateItem(item, empty);

			if (empty && isSelected()) {
				updateSelected(false);
			}
 
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(getTreeItem().getGraphic());
                }
            }
        }
 
        private void createTextField() {
            textField = new TextField(getString());
            textField.setOnKeyReleased(new EventHandler<KeyEvent>() {
 
                @Override
                public void handle(KeyEvent t) {
                    if (t.getCode() == KeyCode.ENTER) {
						((NF28Groupe) getTreeItem().getValue()).setNom(textField.getText());
						commitEdit(getTreeItem().getValue());
                    } else if (t.getCode() == KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                }
            });
        }
 
        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }
}
