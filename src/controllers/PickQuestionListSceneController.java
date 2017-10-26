package controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import models.QuestionModel;

import java.util.ArrayList;
import java.util.List;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;

import com.jfoenix.controls.JFXListView;

import application.Main;

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

	protected void initData(Stage stage, String setName) {
		_qm = QuestionModel.getInstance();
		_selfStage = stage;
		_setName = setName;
		_allQuestions = new ArrayList<String>();
		_listOfQuestions = new ArrayList<String>();
		loadQuestions();
	}

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
			_listOfQuestions.remove(selectedQ);
			_userChoseListView.getItems().setAll(_listOfQuestions);
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
			Main.showErrorDialog("Error!", "Please pick at least one question.", null, _background);
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
}
