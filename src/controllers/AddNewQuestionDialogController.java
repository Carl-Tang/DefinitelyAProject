package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import application.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.QuestionModel;

public class AddNewQuestionDialogController implements Initializable {

	@FXML
	private JFXTextField questionTextField;
	@FXML
	private JFXTextField answerTextField;
	@FXML
	private Label answerFormatWarningLabel;
	@FXML
	private JFXButton confirmAdd;
	@FXML
	private VBox warningBox;

	private String _setName;

	private QuestionSetEditPanelController _parent;

	private Stage _stage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		answerFormatWarningLabel
				.setText("Answer must be a number between " + Main.NUMLOWERBOUND + " and " + Main.NUMUPPERBOUND);

		questionTextField.textProperty().addListener((observable, oldValue, newValue) -> {

			if (newValue.isEmpty()) {
				confirmAdd.setDisable(true);
			} else {
				ScriptEngineManager mgr = new ScriptEngineManager();
				ScriptEngine engine = mgr.getEngineByName("JavaScript");
				try {
					int answer = Integer.parseInt(engine.eval(newValue).toString());
					if (answer >= Main.NUMLOWERBOUND && answer <= Main.NUMUPPERBOUND) {
						answerTextField.setText(answer + "");
					} else {
						answerTextField.setText("");
						warningBox.setVisible(true);
						confirmAdd.setDisable(true);
					}
				} catch (ScriptException | NumberFormatException e) {
					answerTextField.setText("");
					warningBox.setVisible(true);
					confirmAdd.setDisable(true);
				}
			}
		});

		answerTextField.textProperty().addListener((observable, oldValue, newValue) -> {

			// check is the answer empty
			if (!newValue.isEmpty()) {
				warningBox.setVisible(false);
				confirmAdd.setDisable(false);
			} else {
				warningBox.setVisible(true);
				confirmAdd.setDisable(true);
			}
		});
	}

	public void initData(String setName, Stage newQuestionStage) {
		_setName = setName;
		_stage = newQuestionStage;
	}

	/**
	 * Add a key event handler to the scene to handle the shortcut. The shortcut is
	 * "Esc" for closing the this add new question stage.
	 */
	public void enableShortcut() {
		_stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.ESCAPE) {
					_stage.close();
				}
			}

		});
	}

	public void setParent(QuestionSetEditPanelController parent) {
		_parent = parent;
	}

	@FXML
	void addQuestion(ActionEvent event) {
		// TODO add multiple questions
		String question = questionTextField.getText();
		String answer = answerTextField.getText();
		QuestionModel.getInstance().addQuestionToQuestionSet(_setName, question, answer);
		_parent.loadQuestions();
	}

}
