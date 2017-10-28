package models;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
/**
 * This class is a data structure of a question set which contains a bunch of attributes that will be useful and are designed for the convenience of data management for QuestionModel class.
 * Note: Each question us unique in the same set but not necessarily the answer.
 * 
 * @author Carl Tang & Wei Chen
 *
 */
public class QuestionSet {

	private String _nameOfSet;
	private Map<String, String> _QAPairs = new HashMap<String, String>();
	private File _theSet;

	protected QuestionSet(String nameOfSet) {
		_nameOfSet = nameOfSet;
		_theSet = new File("QuestionSets/" + _nameOfSet + ".csv");
		loadList();
	}

	/**
	 * load list of questions from a local csv file which is a set of questions with the file name be the name of the set.
	 */
	private void loadList() {
		if (!_theSet.exists()) {
			// create file if does not exist
			try {
				_theSet.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// read all questions of the user
			List<String> listOfQuestions;
			try {
				listOfQuestions = Files.readAllLines(Paths.get(_theSet.toURI()));

				if (!listOfQuestions.isEmpty()) {
					// initialize a new questions' list
					for (String question : listOfQuestions) {
						String[] entry = question.split(",");
						if (entry.length == 2 && !entry[0].isEmpty() && !entry[1].isEmpty()) {
							try {
								Integer.parseInt(entry[1]);
							} catch (NumberFormatException e) {
								continue;
							}
							_QAPairs.put(entry[0], entry[1]);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sync the changes in the hashmap to the local file
	 */
	private void updateLocalFile() {
		deleteLocalFile();
		loadList();
		for (Entry<String, String> entry : _QAPairs.entrySet()) {
			// append the record to the local file
			try {
				FileWriter fw = new FileWriter(_theSet, true);
				fw.append(entry.getKey() + "," + entry.getValue() + "\n");
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * add a question to the set
	 * @param question
	 * @param answer
	 */
	protected void addQAPair(String question, String answer) {
		_QAPairs.put(question, answer);
		updateLocalFile();
	}

	/**
	 * randomly generate a list of questions to use
	 * @param numOfQuestions
	 * @return
	 * @throws EmptyQuestionSetException
	 */
	protected List<List<String>> generateRandomQuestionList(int numOfQuestions) throws EmptyQuestionSetException {
		if (_QAPairs.isEmpty()) {
			throw new EmptyQuestionSetException(_nameOfSet);
		}
		List<List<String>> randomList = new ArrayList<List<String>>();
		for (int i = 0; i < numOfQuestions; i++) {
			String question = (String) _QAPairs.keySet().toArray()[new Random()
					.nextInt(_QAPairs.keySet().toArray().length)];
			String answer = _QAPairs.get(question);
			List<String> pair = new ArrayList<String>();
			pair.add(question);
			pair.add(answer);
			randomList.add(pair);
		}
		return randomList;
	}

	/**
	 * delete a given question and its answer as a pair
	 * @param key
	 */
	protected void delete(String key) {
		_QAPairs.remove(key);
		updateLocalFile();
	}

	/**
	 * delete the whole set in local. Inappropriate call is dangerous.
	 */
	protected void deleteLocalFile() {
		if (_theSet != null && _theSet.exists()) {
			_theSet.delete();
		}
	}

	/**
	 * check if a given name of question is already existed in the set
	 * @param key
	 * @return
	 */
	protected boolean questionExist(String key) {
		String value = _QAPairs.get(key);
		if (value != null) {
			return true;
		}
		return false;
	}

	/**
	 * return the name of the question set.
	 */
	protected String getSetName() {
		return _nameOfSet;
	}
	
	/**
	 * return a list of questions turned from the set of questions in this module
	 * @return
	 */
	protected List<List<String>> getQuestionsInSet() {

		List<List<String>> listForEdit = new ArrayList<List<String>>();
		for (Entry<String, String> entry : _QAPairs.entrySet()) {
			List<String> newEntry = new ArrayList<String>();
			newEntry.add(entry.getKey());
			newEntry.add(entry.getValue());
			listForEdit.add(newEntry);
		}
		return listForEdit;
	}

	/**
	 * Empty question set exception is defined
	 * @author Carl Tang & Wei Chen
	 *
	 */
	public class EmptyQuestionSetException extends Exception {

		private static final long serialVersionUID = 1L;
		private String _nameOfSet;

		public EmptyQuestionSetException(String nameOfSet) {
			_nameOfSet = nameOfSet;
		}

		@Override
		public String getMessage() {
			return "The question set " + _nameOfSet + " is empty.";
		}

	}
}
