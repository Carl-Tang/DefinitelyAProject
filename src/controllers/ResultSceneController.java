package controllers;

import javafx.fxml.FXML;

import java.io.File;
import java.net.MalformedURLException;

import com.jfoenix.controls.JFXButton;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import models.QuestionModel;
/**
 * This class is the controller for Result scene which shows the user correctness of the question he/she just answered with the functionality of:
 * offering a retry for the 1st time wrong answer, skip, go next question, information of what the user answered, detail of past questions, provide user to hear what they've said as well as hint of the correct word if they answered wrong the 2nd time.
 * 
 * @author Carl Tang & Wei Chen
 *
 */
public class ResultSceneController {

	// Labels
	@FXML
	private Label _correctness;
	@FXML
	private Label _usrAnsLabel;
	@FXML
	private Label _correctAnsLabel;

	// Containers
	@FXML
	private HBox _usrAnsBox;
	@FXML
	private HBox _correctAnsBox;

	// Buttons
	@FXML
	private JFXButton _replayBtn;
	@FXML
	private JFXButton _retryBtn;
	@FXML
	private JFXButton _nextBtn;
	@FXML
	private JFXButton _finishBtn;

	private FoundationBoardController _parentController;

	protected ResultSceneController(FoundationBoardController foundationBoardController) {
		_parentController = foundationBoardController;
	}

	/**
	 * Tell controller if the user has another chance to retry
	 * 
	 * @param canRetry
	 */
	protected void setCanRetry(boolean canRetry) {
		if (canRetry) {
			_retryBtn.setDisable(false);
		} else {
			_retryBtn.setDisable(true);
		}
	}

	/**
	 * Tell controller what the user said
	 * 
	 * @param userAnswer
	 */
	protected void setUserAnswer(String userAnswer) {
		if (userAnswer == null) {
			_usrAnsBox.setVisible(false);
		} else {
			if (!userAnswer.isEmpty()) {
				_usrAnsLabel.setText(userAnswer);
			} else {
				_usrAnsLabel.setText("No Answer");
			}
			_usrAnsBox.setVisible(true);
		}

	}

	/**
	 * Tell ResultSceneController that this question is the final question and
	 * disable the "next question" button.
	 * 
	 * @param isFinal
	 */
	protected void setFinal(boolean isFinal) {
		if (isFinal) {
			_nextBtn.setText("Final question");
			_nextBtn.setDisable(true);
		} else {
			_nextBtn.setText("Next question");
			_nextBtn.setDisable(false);
		}
	}

	/**
	 * Tell controller the correctness of the answer.
	 * 
	 * @param isCorrect
	 */
	protected void resultIsCorrect(boolean isCorrect) {
		if (!isCorrect) {
			_correctness.setText("Wrong");
			_correctness.setTextFill(Color.RED);
		} else {
			_correctness.setText("Correct");
			_correctness.setTextFill(Color.GREEN);
		}
	}

	/**
	 * Show the hint which is the correct answer (Maori word) for the question
	 * 
	 * @param correctWord
	 */
	protected void showCorrectAnswer(String correctWord) {
		if (correctWord != null && !correctWord.isEmpty()) {
			_correctAnsLabel.setText(correctWord);
			_correctAnsBox.setVisible(true);
		} else {
			_correctAnsBox.setVisible(false);
		}
	}

	/**
	 * Event handler for finishBtn on action. Asks FoundationBoard to finish the
	 * game or practise.
	 * 
	 * @param event
	 */
	@FXML
	private void finishBtnClicked(ActionEvent event) {
		_parentController.finish();
	}

	/**
	 * Event handler for nextBtn on action. Asks FoundationBoard to show next
	 * question.
	 * 
	 * @param event
	 */
	@FXML
	private void nextBtnClicked(ActionEvent event) {
		_parentController.showNextQuestion();
	}

	/**
	 * Event handler for replayBtn on action. Replay the recorded user speech.
	 * 
	 * @param event
	 */
	@FXML
	private void replayBtnClicked(ActionEvent event) {
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

	/**
	 * Event handler for retryBtn on action. Ask FoundationBoard to re show the
	 * question scene. Trail number is automatically incremented by the
	 * FoundationBoard.
	 * 
	 * @param event
	 */
	@FXML
	private void retryBtnClicked(ActionEvent event) {
		_parentController.showQuestionScene();
	}

}
