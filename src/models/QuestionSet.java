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

public class QuestionSet {

	private String _nameOfSet;
	private Map<String, String> _QAPairs = new HashMap<String, String>();
	private File _theSet;

	protected QuestionSet(String nameOfSet) {
		_nameOfSet = nameOfSet;
		_theSet = new File("QuestionSets/" + _nameOfSet + ".csv");
		loadList();
	}

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
						_QAPairs.put(entry[0], entry[1]);
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

	protected void addQAPair(String question, String answer) {
		_QAPairs.put(question, answer);
		updateLocalFile();
	}

	protected List<List<String>> generateRandomQuestionList(int numOfQuestions) {
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

	protected void delete(String key) {
		_QAPairs.remove(key);
		updateLocalFile();
	}

	protected void deleteLocalFile() {
		_theSet.delete();
	}

	protected boolean questionExist(String key) {
		String value = _QAPairs.get(key);
		if (value != null) {
			return true;
		}
		return false;
	}

	protected String getSetName() {
		return _nameOfSet;
	}

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
}
