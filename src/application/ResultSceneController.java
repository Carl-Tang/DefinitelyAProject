package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.media.AudioClip;

public class ResultSceneController {
	@FXML
	private Label _correctness;
	@FXML
	private Label _answerLabel;
	@FXML
	private Button _replayBtn;
	@FXML
	private Button _retryBtn;
	@FXML
	private Button _nextBtn;
	@FXML
	private Button _finishBtn;
	private FoundationBoardController _parentController;

	// Event Listener on Button[#_replayBtn].onAction
	@FXML
	public void replayBtnClicked(ActionEvent event) {
		Task<Void> replay = new Task<Void>() {
			@Override
			public Void call() {
				System.gc();

				try {
					AudioClip replay = new AudioClip(
							new File(QuestionModel.getInstance().currentAnswer() + ".wav").toURI().toURL().toString());
					replay.play();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				return null;
			}
		};
		new Thread(replay).start();
	}

	// Event Listener on Button[#_retryBtn].onAction
	@FXML
	public void retryBtnClicked(ActionEvent event) {
		_parentController.showQuestionScene();
	}

	// Event Listener on Button[#_nextBtn].onAction
	@FXML
	public void nextBtnClicked(ActionEvent event) {
		_parentController.showNextQuestion();
	}

	// Event Listener on Button[#_finishBtn].onAction
	@FXML
	public void finishBtnClicked(ActionEvent event) {

		_parentController.finish();

	}

	public void setParent(FoundationBoardController controller) {
		_parentController = controller;
	}

	/**
	 * Tell controller the correctness of the answer.
	 * 
	 * @param isCorrect
	 */
	public void resultIsCorrect(boolean isCorrect) {
		if (!isCorrect) {
			if (_parentController.canRetry()) {
				_retryBtn.setVisible(true);
			} else {
				_retryBtn.setVisible(false);
			}
			_correctness.setText("Wrong");
		} else {
			_retryBtn.setVisible(false);
			_correctness.setText("Correct");
		}
	}

	/**
	 * Tell controller what the user said
	 * 
	 * @param userAnswer
	 */
	public void setUserAnswer(String userAnswer) {
		_answerLabel.setText(userAnswer);
	}

	/**
	 * Tell ResultSceneController that this question is the final question and
	 * disable the "next question" button.
	 * 
	 * @param isFinal
	 */
	public void setFinal(boolean isFinal) {
		if (isFinal) {
			_nextBtn.setText("Final question");
			_nextBtn.setDisable(true);
		} else {
			_nextBtn.setText("Next question");
			_nextBtn.setDisable(false);
		}

	}
}
