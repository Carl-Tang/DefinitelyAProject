package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
/**
 * This class will handle local question list as well as question lists in the program.
 * The computed score for any single question therefore come from here and should be stored 
 * in User Model for future reuse.
 * 
 * This class gethering input from local question list, user input (customized question list),
 * and correctness from recognizing system.
 */

//generate question, trial number, correct answer, score of game, isfinished?, is the question,
public class QuestionModel {

	private static QuestionModel _modelInstance;
	
	private List<String> _preloadQAPairs;
	
	private List<String> _currentQuestionList;
	
	//includes the question just done (for calculating score)
	private List<String> _questionsDid;
	
	private int _numOfquestionsGotCorrect;
	
	
	private String _currentQuestion;
	
	private String _currentAnswer;
	
	private int _currentTrial;

	private int _lengthOfQuestionList;
	
	private boolean _isFinished;
	
	private boolean _isPractise;
	
	private int _currentScore;
	
	private Map<String, String> _maoriCache;
	
	private double _pronounciationHardnessFactor;
	
	public boolean isFinished() {
		return _isFinished;
	}
	
	/**
	 * Constructor
	 */
	private QuestionModel() {
		//load premade question as a list into the program
		_maoriCache = new HashMap();
		_maoriCache.put("expected", null);
		_maoriCache.put("actual", null);
		_pronounciationHardnessFactor = 0;
		_numOfquestionsGotCorrect = 0;
		Scanner s;
		try {
			s = new Scanner(new File("filepath"));
			ArrayList<String> list = new ArrayList<String>();
			while (s.hasNext()){
			    list.add(s.next());
			}
			s.close();
			_preloadQAPairs = list;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

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

	public void generateQuestionListFromPreload(String hardness) {
		
	}
	
	public void generateQuestionListFromUserDefine(String listName) {
		
	}
	
	//return true on success, return false on the last QAPairs in the list
	public boolean goNext() {
		return false;
		
	}
	
	public void generateAPremadeListD(String hardness) {
		switch (hardness){
		case "easy":
			_currentQuestionList = getEasyList();
		case "medium":
			_currentQuestionList = getMediumList();
		case "hard":
			_currentQuestionList = getHardList();
		}
	}
	
	private List<String> getEasyList(){
		
		
		return _currentQuestionList;
		
	}
	
	private List<String> getMediumList(){
		
		
		return _currentQuestionList;
		
	}
	
	private List<String> getHardList(){
		return _currentQuestionList;
		
	}
	/**
	 * Get the current question
	 * 
	 * @return current question
	 */
	public String currentQuestion() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Get the current answer
	 * 
	 * @return current answer
	 */
	public String currentAnswer() {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
	}
	
	public void updateResult(String maoriWord, boolean correctness){
		
	}
	
	private void computeScore(Mode mode)	{
		int score = 0;
		switch(mode) {
		case PRACTISE:
			score = _currentScore + 1;
		case NORMALMATH:
			calculateHardnessFactor();
			// new%correctness = num of questions correct / total num of questions =
			// (old%correctness * total num of questions + 1)/total num of questions
			double percentageCorrect = (double)_numOfquestionsGotCorrect / _currentQuestionList.size();
			score = (int) (percentageCorrect * 100 * _pronounciationHardnessFactor * (1 + _currentQuestionList.size() / 100));
		case ENDLESSMATH:
			
		}
		_currentScore = score;
	}

	private void calculateHardnessFactor() {
		double prevFactor = _pronounciationHardnessFactor;
		double currentQuesHardness;
		//TODO warning: may leads to wrong answer, change later
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

		double hardnessFactor = ((prevFactor * (_questionsDid.size() - 1)) + currentQuesHardness) / _questionsDid.size();

		_pronounciationHardnessFactor = hardnessFactor;

	}
}