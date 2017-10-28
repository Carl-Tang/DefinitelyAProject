package controllers;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import models.QuestionModel;

import java.util.ArrayList;
import java.util.List;

import com.jfoenix.controls.JFXButton;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import com.jfoenix.controls.JFXListView;

import application.Main;
/**
 * This class is the controller of a scene such that user can pick their own list of questions to play in the game from a given set of qustions which they can chose in the settings scene.
 * 
 * @author Carl Tang & Wei Chen
 *
 */
public class PickQuestionListSceneController {

	@FXML
	private StackPane _background;
	@FXML
	private JFXListView<String> _allQuestionsListView;
	@FXML
	private JFXButton _addBtn;
	@FXML
	private JFXButton _deleteBtn;
	@FXML
	private JFXListView<String> _userChoseListView;
	@FXML
	private JFXButton _confirmBtn;

	private QuestionModel _qm;

	private Stage _selfStage;

	private String _setName;
	private List<String> _allQuestions;
	private List<String> _listOfQuestions;

	private boolean _isDialogShowing;

	protected void initData(Stage stage, String setName) {
		_qm = QuestionModel.getInstance();
		_selfStage = stage;
		_setName = setName;
		_allQuestions = new ArrayList<String>();
		_listOfQuestions = new ArrayList<String>();
		loadQuestions();
		_allQuestionsListView.getSelectionModel().select(0);

		_isDialogShowing = false;
	}

	/**
	 * Load a list of questions from a given set
	 */
	protected void loadQuestions() {
		List<List<String>> rawData = _qm.getQuestionsFromSpecificSet(_setName);
		for (int i = 0; i < rawData.size(); i++) {
			List<String> temp = rawData.get(i);
			String Combined = temp.get(0) + "=" + temp.get(1);
			_allQuestions.add(Combined);
		}
		_allQuestionsListView.getItems().setAll(_allQuestions);
	}

	/**
	 * Add a key event handler to handle shortcuts pressed. Shortcuts include: RIGHT
	 * to add a question to the list, LEFT to remove the question, ENTER or CTRL+S
	 * to confirm the list.
	 */
	protected void enableShortcut() {
		_selfStage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			final KeyCombination CTRL_S = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);

			@Override
			public void handle(KeyEvent e) {
				if (!_isDialogShowing) {
					if (e.getCode() == KeyCode.RIGHT) {
						addBtnClicked(null);
					} else if (e.getCode() == KeyCode.LEFT) {
						deleteBtnClicked(null);
					} else if (e.getCode() == KeyCode.ENTER || CTRL_S.match(e)) {
						confirmBtnClicked(null);
					}
				}
			}

		});
	}

	/**
	 * Event Listener on JFXButton[#_addBtn].onAction. Add the selected question
	 * from the _allQuestionsListView to the _listOfQuestions and
	 * _userChoseListView.
	 * 
	 * @param event
	 */
	@FXML
	private void addBtnClicked(ActionEvent event) {
		// TODO add multiple questions
		String selectedQuestion = _allQuestionsListView.getSelectionModel().getSelectedItem();
		if (selectedQuestion != null) {
			_listOfQuestions.add(selectedQuestion);
			_userChoseListView.getItems().setAll(_listOfQuestions);
			_userChoseListView.getSelectionModel().select(selectedQuestion);
			Platform.runLater(() -> {
				_allQuestionsListView.requestFocus();
			});
		}
	}

	/**
	 * Event Listener on JFXButton[#_deleteBtn].onAction. Remove the selected
	 * question from the _userChoseListView and _listOfQuestions.
	 * 
	 * @param event
	 */
	@FXML
	private void deleteBtnClicked(ActionEvent event) {
		String selectedQ = _userChoseListView.getSelectionModel().getSelectedItem();
		if (selectedQ != null) {
			int i = _listOfQuestions.indexOf(selectedQ);
			_listOfQuestions.remove(selectedQ);
			_userChoseListView.getItems().setAll(_listOfQuestions);
			if (_userChoseListView.getItems().size() > i) {
				_userChoseListView.getSelectionModel().select(i);
			} else if (_userChoseListView.getItems().size() == i) {
				_userChoseListView.getSelectionModel().select(i - 1);
			}
			Platform.runLater(() -> {
				if (_userChoseListView.getItems().isEmpty()) {
					_allQuestionsListView.requestFocus();
				}else {
					_userChoseListView.requestFocus();
				}
			});
		}
	}

	/**
	 * Event Listener on JFXButton[#_confirmBtn].onAction. Confirm using the picked
	 * list, set the picked list in QuestionModel.
	 * 
	 * @param event
	 */
	@FXML
	private void confirmBtnClicked(ActionEvent event) {
		if (_userChoseListView.getItems().isEmpty()) {
			Main.showErrorDialog("Error!", "Please pick at least one question.", (e) -> {
				_isDialogShowing = false;
			}, _background);
			_isDialogShowing = true;

		} else {
			List<List<String>> listGenerated = new ArrayList<List<String>>();
			for (int i = 0; i < _listOfQuestions.size(); i++) {
				String question = _listOfQuestions.get(i).split("=")[0];
				String answer = _listOfQuestions.get(i).split("=")[1];
				List<String> QAPair = new ArrayList<String>();
				QAPair.add(question);
				QAPair.add(answer);
				listGenerated.add(QAPair);
			}
			_qm.setUserPickedList(listGenerated);
			_selfStage.close();
		}
	}

	/**
	 * Show available keyBoard shortcuts
	 * @param event
	 */
	@FXML
	private void showShortcuts(ActionEvent event) {
		String body = "Press					Do\n"
				    + "RIGHT					add the selected question to the list.\n"
				    + "LEFT						remove the selected question from the list.\n"
				    + "ENTER or CTRL+S			confirm use this question list.";
		Main.showInfoDialog("Shortcuts", body, (e) -> {
			_isDialogShowing = false;
		}, _background);
		_isDialogShowing = true;
	}
}
