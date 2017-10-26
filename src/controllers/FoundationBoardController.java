package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import application.BashProcess;
import application.Main;
import application.SpeechRecognizer;
import enums.Function;
import enums.Mode;

import com.jfoenix.controls.JFXDrawer;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import models.QuestionModel;
import models.UserModel;

public class FoundationBoardController implements Initializable {

	// Containers
	@FXML
	private StackPane _background;
	/**
	 * This pane is shown on the left side of the scene, which can have different
	 * scenes switching on it
	 */
	@FXML
	private HBox _mainPane;
	/**
	 * This pane is shown on the right side of the scene, which shows the statistics
	 */
	@FXML
	private JFXDrawer _statisticsBar;

	// Labels
	@FXML
	private Label _modeLabel;
	@FXML
	private Label _scoreLabel;
	@FXML
	private Label _numQLeftLabel;

	// Buttons
	@FXML
	private JFXButton _homeBtn;
	@FXML
	private HBox _infoBar;
	@FXML
	private Button _helpBtn;

	// models
	private QuestionModel _questionModel;
	private UserModel _userModel;

	// variables
	private Function _function;
	private String _userName;
	/**
	 * A map that has keys that are the answer to the questions that the user did
	 * wrong, and values of the number of times user got it wrong
	 */
	private Map<String, Integer> _wrongQuestions = new HashMap<String, Integer>();
	private Mode _mode;
	private int _trailNum;
	private int _maxTrailNum;

	private Main _main;

	// controllers
	private MathStartPageController _mathStartPageController;
	private PractiseStartPageController _practiseStartPageController;
	private QuestionSceneController _questionSceneController;
	private ResultSceneController _resultSceneController;
	private PractiseSummarySceneController _practiseSummarySceneController;
	private StatisticsSidePaneController _statistics;

	// sub panes
	private Pane _mathStartPage;
	private Pane _practiseStartPage;
	private Pane _questionScene;
	private Pane _resultScene;
	private Pane _practiseSummaryScene;
	private Pane _statisticsSidePane;

	public FoundationBoardController(Main main) {
		_main = main;
		_userModel = UserModel.getInstance();
		_questionModel = QuestionModel.getInstance();

		_trailNum = 0;

		// load local config file and get the maximum trail number
		_maxTrailNum = loadMaxTrailNum();

		// initialize controllers
		_practiseStartPageController = new PractiseStartPageController(this);
		_mathStartPageController = new MathStartPageController(this);
		_questionSceneController = new QuestionSceneController(this);
		_resultSceneController = new ResultSceneController(this);
		_practiseSummarySceneController = new PractiseSummarySceneController();
		_statistics = new StatisticsSidePaneController();

		// load sub panes
		_practiseStartPage = Main.loadScene("PractiseStartPage.fxml", _practiseStartPageController);
		_mathStartPage = Main.loadScene("MathStartPage.fxml", _mathStartPageController);
		_questionScene = Main.loadScene("QuestionScene.fxml", _questionSceneController);
		_resultScene = Main.loadScene("ResultScene.fxml", _resultSceneController);
		_practiseSummaryScene = Main.loadScene("PractiseSummaryScene.fxml", _practiseSummarySceneController);
		_statisticsSidePane = Main.loadScene("StatisticsSidePane.fxml", _statistics);
	}

	/**
	 * Initialize the controller
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		_infoBar.setVisible(false);

		// load statistics bar
		_statisticsBar.setSidePane(_statisticsSidePane);
	}

	/**
	 * Set the function of the board: Play or Practise
	 * 
	 * @param function
	 *            (either PRACTISE or MATH)
	 */
	public void setFunction(Function function) {
		_function = function;

		// renew max trail number
		_maxTrailNum = loadMaxTrailNum();

		_infoBar.setVisible(false);

		switch (function) {
		case PRACTISE:
			_modeLabel.setText("Practise");
			_mainPane.getChildren().setAll(_practiseStartPage);
			break;
		case MATH:
			_modeLabel.setText("Math Game");
			_mainPane.getChildren().setAll(_mathStartPage);
			break;
		default:
			throw new RuntimeException("Function can only be PRACTISE or MATH");
		}

	}

	/**
	 * Start practising. If the number parameter is a number, then practise this
	 * specific number, of the number parameter is null, then randomly generate
	 * numbers to practise
	 * 
	 * @param number
	 *            (either a number or null)
	 */
	protected void startPractise(Integer number) {
		_mode = Mode.PRACTISE;

		_questionModel.initializePractise(number);
		_questionModel.NextQA();

		_modeLabel.setText("Practise Maori Pronunciation");

		_infoBar.setVisible(true);
		_scoreLabel.setText("0");
		_numQLeftLabel.setText("Infinite");

		showQuestionScene();
	}

	/**
	 * Start math game.
	 * 
	 * @param gameMode
	 *            the gameMode (either Normal or Endless)
	 * @param playerName
	 */
	protected void startMathGame(Mode gameMode, String playerName) {

		_mode = gameMode;

		switch (gameMode) {
		case NORMALMATH:
			_mode = Mode.NORMALMATH;
			_questionModel.setMode(Mode.NORMALMATH);
			_questionModel.triggerGameStart();
			_questionModel.NextQA();

			_modeLabel.setText("Maths Game: Normal Mode");

			_infoBar.setVisible(true);
			_scoreLabel.setText("0");
			_numQLeftLabel.setText(_questionModel.numOfQuestionsLeft() + 1 + "");

			break;
		case ENDLESSMATH:
			_mode = Mode.ENDLESSMATH;
			_questionModel.setMode(Mode.ENDLESSMATH);
			_questionModel.triggerGameStart();
			_questionModel.NextQA();

			_modeLabel.setText("Maths Game: Endless Mode");

			_infoBar.setVisible(true);
			_scoreLabel.setText("0");
			_numQLeftLabel.setText("Infinite");

			break;
		default:
			throw new RuntimeException("Game mode can only be NORMALMATH or ENDLESSMATH");
		}

		_userName = playerName;

		showQuestionScene();
	}

	/**
	 * Show the QuestionScene on the main pane
	 */
	protected void showQuestionScene() {
		_mainPane.getChildren().setAll(_questionScene);
		// ask for current question and answer
		_questionSceneController.setQuestion(_questionModel.currentQuestion(), _questionModel.currentAnswer());
	}

	/**
	 * Inform the foundation board controller that the number of trails the user
	 * took increased by one
	 */
	protected void incrementTrial() {
		_trailNum++;
	}

	/**
	 * Show the ResultScene on the main pane
	 */
	protected void showResult() {
		Task<Void> check = new Task<Void>() {
			@Override
			public Void call() {
				SpeechRecognizer.checkCorrectness();
				return null;
			}
		};
		check.setOnSucceeded(rce -> {
			// ask question model for correctness of the current question
			boolean isCorrect = _questionModel.isUserCorrect();

			// set the required info of the result controller
			_resultSceneController.resultIsCorrect(isCorrect);

			// show the user's answer
			_resultSceneController.setUserAnswer(_questionModel.answerOfUser());

			if (!isCorrect) {
				// check if the user has a chance to retry
				_resultSceneController.setCanRetry(_trailNum < _maxTrailNum);
			} else {
				_resultSceneController.setCanRetry(false);
			}

			// if is in practise mode and the user's answer in incorrect, show the
			// correct answer in result scene
			if (_mode == Mode.PRACTISE && !_questionModel.isUserCorrect()) {
				_resultSceneController.showCorrectAnswer(_questionModel.correctWord());
			} else {
				_resultSceneController.showCorrectAnswer(null);
			}

			// check is the question the final one
			_resultSceneController.setFinal(!_questionModel.hasNext());

			// show the result scene
			_mainPane.getChildren().setAll(_resultScene);

		});
		new Thread(check).start();

	}

	/**
	 * Record the correctness of the current question and show next question on the
	 * question scene.
	 */
	protected void showNextQuestion() {

		// append the new result
		appendResult();

		// reset trail number
		_trailNum = 0;

		// ask question model to go to next question
		_questionModel.NextQA();

		// update score label and numQLeftLabel
		_scoreLabel.setText(_questionModel.getScore() + "");
		if (_mode == Mode.NORMALMATH) {
			_numQLeftLabel.setText(_questionModel.numOfQuestionsLeft() + 1 + "");
		}

		showQuestionScene();

	}

	/**
	 * Finish practising/math gaming and go back to home (if is under practise mode)
	 * or go to personal summary (if is under math mode).
	 */
	protected void finish() {

		String title = "Confirm Finish";
		String body;
		if (_mode == Mode.PRACTISE) {
			body = "Do you want to finish practising and show summary?";
		} else if (_mode == Mode.NORMALMATH) {
			body = "Do you want to skip the rest questions and save your result? "
					+ "(The rest of the questions will be marked wrong)";
		} else {
			body = "Do you want to skip the rest questions and save your current result?";
		}

		EventHandler<ActionEvent> okHandler = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// append the result
				appendResult();

				if (_mode == Mode.PRACTISE) {
					_mode = null;
					showPractiseSummary();
				} else {
					_userModel.appendRecord(_userName, _mode, _questionModel.getScore());
					// reset question model and statistics
					_statistics.reset();
					_questionModel.clear();
					_trailNum = 0;
					_mode = null;
					_main.showPersonalPanel(_userName);
				}
			}
		};

		Main.showConfirmDialog(title, body, okHandler, null, _background);

	}

	/**
	 * Load the maximum trial number from the local properties file
	 * 
	 * @return maximum trial number
	 */
	private int loadMaxTrailNum() {
		File configFile = new File("config.properties");
		// default number of chances to retry is 2
		String maxTrailNumber = "2";
		try {
			FileReader reader = new FileReader(configFile);
			Properties props = new Properties();
			props.load(reader);

			maxTrailNumber = props.getProperty("maxTrailNumber", "2");

			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Integer.parseInt(maxTrailNumber);
	}

	/**
	 * Append the new result to the statistics bar and delete the temperate recorded
	 * sound file. If in practise mode and user got the question wrong, make a
	 * record of this question in the _wrongQuestion map.
	 */
	private void appendResult() {
		// if in practise mode, add this wrong record to the wrong questions map
		if (_mode == Mode.PRACTISE) {
			String currentQuestion = _questionModel.currentQuestion();
			if (_wrongQuestions.get(currentQuestion) == null) {
				_wrongQuestions.put(currentQuestion, 1);
			} else {
				int numGetWrong = _wrongQuestions.get(currentQuestion);
				numGetWrong++;
				_wrongQuestions.put(currentQuestion, numGetWrong);
			}
		}

		// append the result to the statistics bar
		_statistics.appendResult(_questionModel.currentAnswer(), _questionModel.isUserCorrect());

		// remove previous recording
		new BashProcess("./MagicStaff.sh", "remove", _questionModel.currentAnswer());
	}

	/**
	 * Show the summary scene for the practise
	 */
	private void showPractiseSummary() {
		_mainPane.getChildren().setAll(_practiseSummaryScene);
		double correctRate = (double) _questionModel.getScore() / _statistics.getNumOfRecords();
		_practiseSummarySceneController.setCorrectRate(correctRate);
		_practiseSummarySceneController.setWrongAnswerChartData(_wrongQuestions);
	}

	/**
	 * Stop any processes running on this foundation board and switch back to the
	 * home page.
	 */
	@FXML
	private void backToHome() {

		// If user haven't began practising or gaming, or already finished practising,
		// _mode will be null. In this case, do not show confirmation dialog
		if (_mode == null) {
			// reset question model and statistics
			_statistics.reset();
			_questionModel.clear();
			_trailNum = 0;
			_main.showHome();
		} else {

			String title = "Return to home page";
			String body = "Do you want to leave the game and return to home page (score will not be saved)?";

			EventHandler<ActionEvent> okHandler = new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					// remove previous recording
					new BashProcess("./MagicStaff.sh", "remove", _questionModel.currentAnswer());

					// reset question model and statistics
					_statistics.reset();
					_questionModel.clear();
					_trailNum = 0;
					_mode = null;
					_main.showHome();
				}
			};

			Main.showConfirmDialog(title, body, okHandler, null, _background);

		}

	}

	/**
	 * Event handler for the "details" button, open/close the statistics bar drawer.
	 * 
	 * @param event
	 */
	@FXML
	private void showStatisticsBar(ActionEvent event) {
		if (_statisticsBar.isHidden()) {
			_statisticsBar.open();
		} else {
			_statisticsBar.close();
		}
	}

	/**
	 * Show the help information for practise or math questions depending on current
	 * function of the foundation board
	 */
	@FXML
	private void showHelp(ActionEvent event) {
		_main.showHelp(_function);
	}

}
