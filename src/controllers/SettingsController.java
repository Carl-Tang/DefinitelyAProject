package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.QuestionModel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;

import application.Main;
import enums.Function;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;

import javafx.scene.control.ComboBox;

public class SettingsController implements Initializable {
	@FXML
	private StackPane background;
	@FXML
	private ComboBox<String> quesitonSetComboBox;
	@FXML
	private Button editQuestionSetBtn;
	@FXML
	private Button addNewSetBtn;
	@FXML
	private Button deleteSetBtn;
	@FXML
	private Label questionListLabel;
	@FXML
	private TextField numOfQuestionsTextFieldForRandom;
	@FXML
	private Button pickARandomListBtn;
	@FXML
	private Button pickYourselfBtn;
	@FXML
	private JFXButton homeBtn;
	@FXML
	private HBox customizeQListBox;
	@FXML
	private JFXComboBox<String> recordingTimeComboBox;
	@FXML
	private JFXTextField maxTrailNumTextField;
	@FXML
	private JFXButton helpBtn;

	private Stage _editPanelStage;

	private Stage _userPickingStage;
	private Main _main;
	private QuestionModel _questionModel;

	private Properties _props;

	public SettingsController(Main main) {
		_main = main;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		_questionModel = QuestionModel.getInstance();

		// add change listers to the numOfQuestionsTextFieldForRandom and
		// maxTrailNumTextField to make them only accept numbers
		numOfQuestionsTextFieldForRandom.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.isEmpty()) {
				// check are all characters in the user input digits
				boolean isNumber = true;
				for (char ch : newValue.toCharArray()) {
					if (!Character.isDigit(ch)) {
						isNumber = false;
					}
				}

				if (!isNumber) { // check is the input a number
					// unto typing
					numOfQuestionsTextFieldForRandom.setText(oldValue);
				}
			}
		});
		maxTrailNumTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.isEmpty()) {
				// check are all characters in the user input digits
				boolean isNumber = true;
				for (char ch : newValue.toCharArray()) {
					if (!Character.isDigit(ch)) {
						isNumber = false;
					}
				}

				if (!isNumber) { // check is the input a number
					// unto typing
					maxTrailNumTextField.setText(oldValue);
				}
			}
		});

		initData();
	}

	/**
	 * Read the config.properties and set the content of text fields and combo
	 * boxes.
	 */
	public void initData() {
		// update the question set combo box
		updateSetList();

		// set items in recording time combo box
		recordingTimeComboBox.getItems().setAll("2.0", "2.5", "3.0", "4.0", "5.0");

		_props = new Properties();
		try {
			_props.load(new FileInputStream("config.properties"));
			String setChosen = _props.getProperty("QSet", "Default");
			String listSize = _props.getProperty("listSize", "10");
			String recordingTime = _props.getProperty("recordingTime", "3");
			String maxTrailNumber = _props.getProperty("maxTrailNumber", "2");
			if (quesitonSetComboBox.getItems().contains(setChosen)) {
				quesitonSetComboBox.getSelectionModel().select(setChosen);
			}
			if (recordingTimeComboBox.getItems().contains(recordingTime)) {
				recordingTimeComboBox.getSelectionModel().select(recordingTime);
			}
			numOfQuestionsTextFieldForRandom.setText(listSize);
			maxTrailNumTextField.setText(maxTrailNumber);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Event Listener on Button[#addNewSetBtn].onAction
	@FXML
	public void addNewSet(ActionEvent event) {
		JFXDialogLayout content = new JFXDialogLayout();
		content.setHeading(new Text("Please enter a name for your set: "));
		TextField tf = new TextField();
		content.setBody(tf);
		JFXButton okBtn = new JFXButton("OK");
		JFXButton cancelBtn = new JFXButton("Cancel");
		content.setActions(okBtn, cancelBtn);
		JFXDialog jfxdialog = new JFXDialog(background, content, DialogTransition.CENTER);
		okBtn.setOnAction(e -> {
			if (tf.getText().equals("Default")) {
				Main.showErrorDialog("Error!", "The new set cannot use the name Default.", null, background);
			} else {
				EventHandler<ActionEvent> okHandler = new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						_questionModel.createLocalQuestionSet(tf.getText());
						openeditPanel(tf.getText());
						updateSetList();
						quesitonSetComboBox.getSelectionModel().select(tf.getText());
					}

				};

				if (quesitonSetComboBox.getItems().contains(tf.getText())) {
					// ask user for confirmation of overwriting the existed question set
					Main.showConfirmDialog("Confirm Overwrite",
							"Do you want to overwrite the question set " + tf.getText()
									+ "? The questions stored in the old set will be losted.",
							okHandler, null, background);
				} else {
					okHandler.handle(event);
				}

			}
			jfxdialog.close();
		});
		cancelBtn.setOnAction(e -> {
			jfxdialog.close();
		});

		jfxdialog.show();
		//////////////////////////////////////
	}

	// Event Listener on Button[#editQuestionSetBtn].onAction
	@FXML
	public void editAQuestionSet(ActionEvent event) {
		if (!quesitonSetComboBox.getSelectionModel().isEmpty()
				&& !quesitonSetComboBox.getValue().toString().equals("Default")) {
			String currentSet = quesitonSetComboBox.getValue().toString();
			System.out.println("Step 0 ... PATH start");
			openeditPanel(currentSet);
		} else {
			permissionDeniededDialog();
		}
	}

	// Event Listener on Button[#deleteSetBtn].onAction
	@FXML
	public void deleteSet(ActionEvent event) {
		if (!quesitonSetComboBox.getSelectionModel().isEmpty()
				&& !quesitonSetComboBox.getValue().toString().equals("Default")) {
			String setName = quesitonSetComboBox.getValue().toString();
			Main.showConfirmDialog("Confirmation Dialog", "Are you sure you want to delete this set?",
					new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							_questionModel.deleteLocalQuestionSet(setName);
							updateSetList();
						}
					}, null, background);

		} else {
			permissionDeniededDialog();
		}
	}

	private void permissionDeniededDialog() {
		Main.showErrorDialog("Information Dialog", "The set is not editable / empty", null, background);
	}

	// Event Listener on Button[#pickARandomListBtn].onAction
	@FXML
	public void pickRandomList(ActionEvent event) {
		String numOfQuestions = numOfQuestionsTextFieldForRandom.getText();
		if (numOfQuestions != null && !numOfQuestions.isEmpty() && Integer.parseInt(numOfQuestions) > 0) {
			// check is auto generate already activated by checking is the customizeQListBox
			// already disabled
			if (customizeQListBox.isDisabled()) {
				// if is activated, deactivate and use preload question list
				_questionModel.generateQuestionListFromPreload("median", 10);
				pickARandomListBtn.setStyle("-fx-background-color: white; -fx-text-fill: black;");
				customizeQListBox.setDisable(false);
			} else {
				// if is not activated, activate and ask question model to automatically pick
				// questions
				String setName = quesitonSetComboBox.getValue().toString();
				_questionModel.setLengthOfQuestionList(Integer.parseInt(numOfQuestions));
				_questionModel.generateQuestionListRandom(setName);
				pickARandomListBtn.setStyle("-fx-background-color: #424242; -fx-text-fill: #eeeeee;");
				customizeQListBox.setDisable(true);
			}

			// TODO underlying code actually support more functionality such as user do not
			// need to specify numberOfQuestions
		} else {
			// show error dialog
			Main.showErrorDialog("Error!", "The size of the question list can only be a positive interger.", null,
					background);
		}
	}

	// Event Listener on Button[#pickYourselfBtn].onAction
	@FXML
	public void pickCustomizedList(ActionEvent event) {
		try {
			String setName = quesitonSetComboBox.getValue().toString();
			_userPickingStage = new Stage();
			_userPickingStage.setMinHeight(400);
			_userPickingStage.setMinWidth(500);
			_userPickingStage.setTitle("List picking window");
			PickQuestionListSceneController pickSceneController = new PickQuestionListSceneController();
			Pane root = Main.loadScene("PickQuestionListScene.fxml", pickSceneController);
			Main.showScene(_userPickingStage, root);
			pickSceneController.initData(_userPickingStage, setName);
		} catch (NullPointerException e) {
			permissionDeniededDialog();
		}

	}

	// Event Listener on Button[#homeBtn].onAction
	@FXML
	public void backToHome(ActionEvent event) {
		_props = new Properties();
		try {
			_props.load(new FileInputStream("config.properties"));
			String setChosen = quesitonSetComboBox.getSelectionModel().getSelectedItem();
			String listSize = numOfQuestionsTextFieldForRandom.getText();
			String recordingTime = recordingTimeComboBox.getSelectionModel().getSelectedItem();
			String maxTrailNumber = maxTrailNumTextField.getText();
			if (setChosen != null && !setChosen.isEmpty()) {
				_props.setProperty("QSet", setChosen);
			}
			if (listSize != null && !listSize.isEmpty()) {
				_props.setProperty("listSize", listSize);
			}
			if (recordingTime != null && !recordingTime.isEmpty()) {
				_props.setProperty("recordingTime", recordingTime);
			}
			if (maxTrailNumber != null && !maxTrailNumber.isEmpty() && Integer.parseInt(maxTrailNumber) > 0) {
				_props.setProperty("maxTrailNumber", maxTrailNumber);
			}

			_props.store(new FileOutputStream("config.properties"), "System Settings");
		} catch (IOException e) {
			e.printStackTrace();
		}

		_main.showHome();
	}

	// Event Listener on Button[#helpBtn].onAction
	@FXML
	void showHelp(ActionEvent event) {
		_main.showHelp(Function.SETTINGS);
	}

	public void openeditPanel(String setName) {
		// TODO pass questionSetName to edit panel
		_editPanelStage = new Stage();
		_editPanelStage.setMinHeight(400);
		_editPanelStage.setMinWidth(400);
		_editPanelStage.setTitle("Set editing page");
		QuestionSetEditPanelController editPanelController = new QuestionSetEditPanelController(this, _editPanelStage);
		Pane root = Main.loadScene("QuestionSetEditPanel.fxml", editPanelController);
		Main.showScene(_editPanelStage, root);
		editPanelController.initData(setName);
	}

	public void updateSetList() {
		ObservableList<String> ol = FXCollections.observableArrayList(_questionModel.getListOfsets());
		quesitonSetComboBox.setItems(ol);

		System.out.println(_questionModel.getListOfsets().toString());
	}
}
