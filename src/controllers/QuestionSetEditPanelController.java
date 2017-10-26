package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import models.QuestionModel;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import application.Main;

public class QuestionSetEditPanelController implements Initializable {

	@FXML
	private StackPane background;

	// list view multiple selection to be implemented
	@FXML
	private JFXListView<?> questionSetList;

	@FXML
	private JFXButton doneBtn;

	@FXML
	private JFXButton addQuestionBtn;

	@FXML
	private JFXListView<String> questionList;

	@FXML
	private JFXButton deleteQuestionBtn;

	private Stage _editPanelStage;

	private Stage _newQuestionStage;

	private QuestionModel _questionModel;

	private List<String> _listOfQuestions;

	private String _currentSetName;

	private SettingsController _settingsController;

	protected QuestionSetEditPanelController(SettingsController settingsController, Stage editPanelStage) {
		_editPanelStage = editPanelStage;
		_settingsController = settingsController;
		// stop the stage from closing if the question set is still empty and show an
		// error panel
		_editPanelStage.setOnCloseRequest(e -> {
			if (_listOfQuestions == null || _listOfQuestions.isEmpty()) {
				e.consume();
				Main.showConfirmDialog("Empty Question Set!",
						"The question set should contain at least one question.\n"
								+ "You can choose OK to delete this empty set or Cancel to keep editing.",
						new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent event) {
								QuestionModel.getInstance().deleteLocalQuestionSet(_currentSetName);
								settingsController.updateSetList();
								_editPanelStage.close();
							}
						}, null, background);
			}
		});
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		deleteQuestionBtn.setDisable(true);
		questionList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null && !newValue.isEmpty()) {
				deleteQuestionBtn.setDisable(false);
			} else {
				deleteQuestionBtn.setDisable(true);
			}
		});
	}

	/**
	 * Add key event handler to the scene to handle keyboard shortcuts. The
	 * shortcuts are: "Delete" for deleting the selected question, "Enter" for done
	 * creation, and "Ctrl+N" to add a new question.
	 */
	protected void enableShortcut() {
		_editPanelStage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			final KeyCombination keyComb = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN);

			@Override
			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.DELETE) {
					deleteSelectedQuestion(null);
				} else if (e.getCode() == KeyCode.ENTER) {
					doneCreation(null);
				} else if (keyComb.match(e)) {
					addNewQuestion(null);
				}
			}

		});
	}

	protected void initData(String setName) {
		_questionModel = QuestionModel.getInstance();
		_currentSetName = setName;
		_listOfQuestions = new ArrayList<String>();
		loadQuestions();
	}

	protected void loadQuestions() {
		_listOfQuestions.clear();
		List<List<String>> rawData = _questionModel.getQuestionsFromSpecificSet(_currentSetName);
		for (int i = 0; i < rawData.size(); i++) {
			List<String> temp = rawData.get(i);
			String Combined = temp.get(0) + "=" + temp.get(1);
			_listOfQuestions.add(Combined);
		}
		questionList.getItems().setAll(_listOfQuestions);
	}

	@FXML
	private void addNewQuestion(ActionEvent event) {

		_newQuestionStage = new Stage();
		_newQuestionStage.setTitle("New question");
		AddNewQuestionDialogController aqdController = new AddNewQuestionDialogController();
		Pane root = Main.loadScene("AddNewQuestionDialog.fxml", aqdController);
		Main.showScene(_newQuestionStage, root);
		aqdController.initData(_currentSetName, _newQuestionStage);
		aqdController.enableShortcut();
		aqdController.setParent(this);

		loadQuestions();
	}

	@FXML
	private void doneCreation(ActionEvent event) {
		if (_listOfQuestions.isEmpty()) {
			Main.showErrorDialog("Error!", "The question set should contain at least one question.", null, background);
		} else {
			_settingsController.updateSetList();
			_editPanelStage.close();
		}
	}

	@FXML
	private void deleteSelectedQuestion(ActionEvent event) {
		String selectedQ = questionList.getSelectionModel().getSelectedItem();
		if (selectedQ != null && !selectedQ.isEmpty()) {
			String key = selectedQ.split("=")[0];
			_questionModel.deleteQuestionFromQuestionSet(_currentSetName, key);
		}
		loadQuestions();
	}
}
