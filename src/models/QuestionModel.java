package models;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

import application.BashProcess;
import application.Main;
import enums.Mode;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 * This class will handle local question list as well as question lists in the
 * program. The computed score for any single question therefore come from here
 * and should be stored in User Model for future reuse.
 * 
 * This class gethering input from local question list, user input (customized
 * question list), and correctness from recognizing system.
 * 
 * The class then should have following functionalities:
 * 
 * load local questionSets create new questionSets (warn user when name of
 * question set duplicate) delete existing questionSets add new question to
 * existing questionSet delete questions from existing questionSet (allow
 * multiple selection)
 * 
 * generate a random question list from a given question set and number of
 * questions are given store a user picked question list and give the
 * functionality of randomize it
 * 
 * the class should be able to iterating an generated list (also have a default
 * if no instructions given) during the iteration: current question is stored,
 * current answer is stored, current trial is stored, whether if the next
 * question existed is stored, the list of questions user done is stored
 * 
 * scoring system (to be detailed)
 * 
 */

// generate question, trial number, correct answer, score of game, isfinished,
// is the question,
public class QuestionModel {

	private static QuestionModel _modelInstance;

	// for preload question set
	private Map<String, QuestionSet> _sets;
	private List<String> _listOfSetNames;
	private List<List<String>> _preloadSortedQuestionSet;

	// for current question
	private String _currentQuestion;
	private String _currentAnswer;
	private double _pronounciationHardnessFactor;
	private String _recognizedWord;
	private String _correctWord;
	private boolean _correctness;

	// for practice
	private Integer _numberToPractise;
	// for question list
	private List<List<String>> _generatedQuestionList;

	// TODO I think below _toDoList can be a stack rather than a list, subject to
	// change later
	private List<List<String>> _toDoList; // this should be a copy of generated list in the begining of each game but
									// reduce its size as the game going
	private List<List<String>> _questionsDid;
	private Integer _lengthOfQuestionList;
	private boolean _isFinished;
	private Integer _numOfquestionsGotCorrect;
	private Integer _currentScore;

	// for mode
	private Mode _currentMode;

	/**
	 * Get the instance of the singleton class QuestionModel
	 * 
	 * @return the only instance of QuestionModel
	 */
	public static QuestionModel getInstance() {
		if (_modelInstance == null) {
			_modelInstance = new QuestionModel();
		}
		return _modelInstance;
	}

	/**
	 * Constructor
	 */
	private QuestionModel() {
		// load pre-made question as a list into the program
		_sets = new HashMap<String, QuestionSet>();
		_listOfSetNames = new ArrayList<String>();
		_listOfSetNames.add(Main.DEFAULT_QUESTION_SET_NAME);
		_preloadSortedQuestionSet = new ArrayList<List<String>>();

		_toDoList = new ArrayList<List<String>>();
		_questionsDid = new ArrayList<List<String>>();

		_generatedQuestionList = new ArrayList<List<String>>();

		_pronounciationHardnessFactor = 1;
		_numOfquestionsGotCorrect = 0;

		_currentScore = 0;

		loadLocalLists();
		Scanner s;
		InputStream in = Main.class.getResourceAsStream("/Default.csv");
		s = new Scanner(in);
		// s = new Scanner(new File("setABC.csv"));
		ArrayList<String> list = new ArrayList<String>();
		while (s.hasNext()) {
			list.add(s.next());
		}
		s.close();
		for (int i = 0; i < list.size(); i++) {
			String[] QAPair = list.get(i).split(",");
			List<String> QAPairl = new ArrayList<String>();
			QAPairl.add(QAPair[0]);
			QAPairl.add(QAPair[1]);
			_preloadSortedQuestionSet.add(QAPairl);
		}
		// give a default list of questions with medium hardness
		generateQuestionListFromPreload("medium", 10);
	}

	// load local question sets
	private void loadLocalLists() {
		File folder = new File("QuestionSets");

		File[] fileList = folder.listFiles();

		for (File file : fileList) {
			if (file.isFile()) {
				// read the sets' name
				String fileName = file.getName();
				String setName = fileName.substring(0, fileName.lastIndexOf("."));
				QuestionSet q = new QuestionSet(setName);
				// loadQuestions(q.getSetName(), q);
				_sets.put(q.getSetName(), new QuestionSet(setName));
				_listOfSetNames.add(setName);
			}
		}
	}

	// create new question set
	// public boolean createLocalQuestionSet(String setName) {
	public void createLocalQuestionSet(String setName) {
		if (isQuestionSetExist(setName)) {
			_sets.get(setName).deleteLocalFile();
			_sets.remove(setName);
			_sets.put(setName, new QuestionSet(setName));
		} else {
			_sets.put(setName, new QuestionSet(setName));
			_listOfSetNames.add(setName);
		}
	}

	// delete existing question set
	// TODO possibility of combining delete confirmation dialogs? How to handle with
	// different
	public void deleteLocalQuestionSet(String setName) {
		new BashProcess("./MagicStaff.sh", "delete", setName);
		_listOfSetNames.remove(setName);
	}

	// check if a questionSet is existed in sets
	private boolean isQuestionSetExist(String setName) {
		QuestionSet value = _sets.get(setName);
		if (value != null) {
			return true;
		}
		return false;
	}

	// add new question to existing question set
	public void addQuestionToQuestionSet(String setName, String question, String answer) {
		if (!isQuestionSetExist(setName)) {
			noSetFoundDialog();
		} else {
			_sets.get(setName).addQAPair(question, answer);
		}
	}

	// delete question from existing question set
	public void deleteQuestionFromQuestionSet(String setName, String question) {
		if (!isQuestionSetExist(setName)) {
			noSetFoundDialog();
		} else {
			if (_sets.get(setName).questionExist(question)) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("confirm delete");
				alert.setHeaderText("Look, a Confirmation Dialog");
				alert.setContentText("Are you ok with this?");

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					_sets.get(setName).delete(question);
				}
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Warning Dialog");
				alert.setHeaderText("No question found");
				alert.setContentText("Careful with the next step!");
				alert.showAndWait();
			}

		}
	}

	// no set found dialog
	private void noSetFoundDialog() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning Dialog");
		alert.setHeaderText("No set found");
		alert.setContentText("Careful with the next step!");
		alert.showAndWait();
	}

	public List<String> getListOfsets() {
		return _listOfSetNames;
	}

	public void setUserPickedList(List<List<String>> listGenerated) {
		_generatedQuestionList = listGenerated;
	}

	/**
	 * append question to a list when user want to pick up their own list of
	 * questions. Note: the field need to be cleared in certain stages at least
	 * before user want to rebuild a list
	 */
	public void addQuestionToListForUserDefine(String question, String answer) {
		List<String> pair = new ArrayList<String>();
		pair.add(question);
		pair.add(answer);
		_generatedQuestionList.add(pair);
	}

	/**
	 * Randomize the order of generated question list, necessarily for each run of
	 * game for user picked list
	 * 
	 * @param randomize
	 */
	public void randomizeQuestionListFromUserDefineWithSelfPick(boolean randomize) {
		if (_generatedQuestionList == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning Dialog");
			alert.setHeaderText("List is empty");
			alert.setContentText("Careful with the next step!");
			alert.showAndWait();
		} else {
			Collections.shuffle(_generatedQuestionList);
		}
	}

	public void setLengthOfQuestionList(int length) {
		_lengthOfQuestionList = length;
	}

	/**
	 * Set the mode of the math aid (pratice, normal math questions, or endless math
	 * questions)
	 * 
	 * @param mode
	 *            (PRACTISE, NORMALMATH, ENDLESSMATH)
	 */
	public void setMode(Mode mode) {
		_currentMode = mode;
	}

	public void initializePractise(Integer numToPractise) {
		_currentMode = Mode.PRACTISE;
		_numberToPractise = numToPractise;
	}

	// getListOfQuestions in a specific set
	public List<List<String>> getQuestionsFromSpecificSet(String setName) {
    
		if (setName == Main.DEFAULT_QUESTION_SET_NAME) {
			return _preloadSortedQuestionSet;
		}
    
		return _sets.get(setName).getQuestionsInSet();
	}

	// generate a random list of question with certain hardness given number of
	// questions, using the preloadset of questions
	public void generateQuestionListFromPreload(String hardness, int numOfQuestions) {
		Random r = new Random();
		int barrier = 100;
		_generatedQuestionList.clear();
		switch (hardness) {
		case "easy":
			for (int i = 0; i < numOfQuestions; i++) {
				int Result = r.nextInt(barrier) + barrier * 0;
				_generatedQuestionList.add(_preloadSortedQuestionSet.get(Result));
			}
			break;
		default:
			for (int i = 0; i < numOfQuestions; i++) {
				int Result = r.nextInt(barrier) + barrier * 1;
				_generatedQuestionList.add(_preloadSortedQuestionSet.get(Result));
			}
			break;
		case "hard":
			for (int i = 0; i < numOfQuestions; i++) {
				int Result = r.nextInt(barrier) + barrier * 2;
				_generatedQuestionList.add(_preloadSortedQuestionSet.get(Result));
			}
			break;
		}
	}

	// generate a random list of questions from selected question set given number
	// of questions, this function may or may not be called multiple times for each
	// run depends on the design choice
	public void generateQuestionListRandom(String setName) {
		if (_lengthOfQuestionList != null && setName.equals("Default")) {
			generateQuestionListFromPreload("medium", _lengthOfQuestionList);
		} else if (_lengthOfQuestionList != null) {
			_generatedQuestionList = _sets.get(setName).generateRandomQuestionList(_lengthOfQuestionList);
		} else {
			_generatedQuestionList = _sets.get(setName).generateRandomQuestionList(10);
		}
	}

	// start question list processing for gaming part (not practise part)
	public void triggerGameStart() {
		if (_generatedQuestionList == null) {
			System.err.println("there is no generated question list to start");
		} else {
			_toDoList = _generatedQuestionList;
		}
	}

	// return number of questions left
	public int numOfQuestionsLeft() {
		return _toDoList.size();
	}

	// retrieve a QA pair to use
	public void NextQA() {
		computeScore(_currentMode);
		switch (_currentMode) {
		case PRACTISE:
			if (_numberToPractise == null) {
				Random r = new Random();
				int result = r.nextInt(98) + 1;
				_currentQuestion = Integer.toString(result);
				_currentAnswer = Integer.toString(result);
			} else {
				_currentQuestion = _numberToPractise.toString();
				_currentAnswer = _numberToPractise.toString();
			}
			break;
		case NORMALMATH:
			List<String> currentQA = _toDoList.get(0);
			_currentQuestion = currentQA.get(0);
			_currentAnswer = currentQA.get(1);
			_questionsDid.add(currentQA);
			_toDoList = _toDoList.subList(1, _toDoList.size());

			break;
		case ENDLESSMATH:
			Random r = new Random();
			List<String> endlessQA = _preloadSortedQuestionSet.get(r.nextInt(299));
			_currentQuestion = endlessQA.get(0);
			_currentAnswer = endlessQA.get(1);
			_questionsDid.add(endlessQA);
		}
		// computeScore(_currentMode);

	}

	// check if all questions are done
	public boolean hasNext() {
		switch (_currentMode) {
		case NORMALMATH:
			return (!(_toDoList.isEmpty()));
		default:
			return true;
		}
	}

	// get current question
	public String currentQuestion() {
		return _currentQuestion;
	}

	// get current answer
	public String currentAnswer() {

		return _currentAnswer;
	}

	// return correct maori word
	public String correctWord() {
		return _correctWord;
	}

	// return user answered word
	public String answerOfUser() {
		return _recognizedWord;

	}

	// return correctness
	public boolean isUserCorrect() {
		return _correctness;
	}

	public boolean isFinished() {
		return _isFinished;
	}

	public void updateResult(String recognizedWord, String correctWord, boolean correctness) {
		// if (correctness) {
		// _numOfquestionsGotCorrect++;
		// }
		_recognizedWord = recognizedWord;
		_correctWord = correctWord;
		_correctness = correctness;
	}

	public void clear() {
		// _preloadSortedQuestionSet = new ArrayList<List<String>>();

		_toDoList = _generatedQuestionList;
		_questionsDid = new ArrayList<List<String>>();

		_pronounciationHardnessFactor = 1;
		_numOfquestionsGotCorrect = 0;

		_currentScore = 0;
		_numOfquestionsGotCorrect = 0;
		_currentScore = 0;
		_isFinished = false;

	}

	public int getScore() {
		return _currentScore;
	}

	private void computeScore(Mode mode) {
		if (_currentScore == null) {
			_currentScore = 0;
		} else {
			if (isUserCorrect()) {
				_numOfquestionsGotCorrect++;
				int score = _currentScore;
				switch (mode) {
				case PRACTISE:
					score = _currentScore + 1;
					break;
				case NORMALMATH:
					calculateHardnessFactor();
					// new%correctness = num of questions correct / total num of questions =
					// (old%correctness * total num of questions + 1)/total num of questions
					double percentageCorrect = (double) _numOfquestionsGotCorrect / _generatedQuestionList.size();
					score = (int) (percentageCorrect * 100 * _pronounciationHardnessFactor
							* (1 + _generatedQuestionList.size() / 100));
					break;
				case ENDLESSMATH:
					calculateHardnessFactor();
					score = (int) (_pronounciationHardnessFactor * _numOfquestionsGotCorrect * 10);
				}
				_currentScore = score;
			}
		}

	}

	private void calculateHardnessFactor() {
		double prevFactor = _pronounciationHardnessFactor;
		double currentQuesHardness;
		// TODO warning: may leads to wrong answer, subject to change later
		Integer currentAns = Integer.parseInt(_currentAnswer);
		if (currentAns >= 1 && currentAns <= 10) {
			currentQuesHardness = 1.0;
		} else if (Arrays.asList(20, 30, 40, 50, 60, 70, 80, 90).contains(currentAns)) {
			currentQuesHardness = 1.2;
		} else if (currentAns > 10 && currentAns < 20) {
			currentQuesHardness = 1.4;
		} else {
			currentQuesHardness = 1.6;
		}

		double hardnessFactor = ((prevFactor * (_questionsDid.size())) + currentQuesHardness)
				/ (_questionsDid.size() + 1);

		_pronounciationHardnessFactor = hardnessFactor;

	}
}