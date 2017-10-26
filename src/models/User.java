package models;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import enums.Mode;

public class User {

	private final String _name;
	private final int NORMAL = 1;
	private final int ENDLESS = 2;
	private File _localFile;
	private Map<Integer, List<Integer>> _results;
	private Mode _latestGameMode;
	private Integer _latestGameScore;

	/**
	 * Construct a new user instance using the name of the user.
	 * 
	 * @param name
	 */
	protected User(String name) {
		_name = name;
		_results = new HashMap<Integer, List<Integer>>();

		// load or create local file which stores user records
		try {
			_localFile = new File("usrRecords/" + name + ".csv");

			if (!_localFile.exists()) {
				try {
					_localFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				Collection<String> recordList;
				recordList = Files.readAllLines(Paths.get(_localFile.toURI()));

				if (!recordList.isEmpty()) {
					// initialize a new user
					for (String record : recordList) {
						String[] entry = record.split(",");
						if (entry[0].equals("Normal")) {
							this.addToResults(NORMAL, Integer.parseInt(entry[1]));
							_latestGameMode = Mode.NORMALMATH;
						} else if (entry[0].equals("Endless")) {
							this.addToResults(ENDLESS, Integer.parseInt(entry[1]));
							_latestGameMode = Mode.NORMALMATH;
						}
						_latestGameScore = Integer.parseInt(entry[1]);

					}
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * Add a new record to the user.
	 * 
	 * @param gameMode
	 * @param score
	 */
	protected void appendNewRecord(Mode gameMode, int score) {
		int mode;
		switch (gameMode) {
		case NORMALMATH:
			mode = NORMAL;
			break;
		case ENDLESSMATH:
			mode = ENDLESS;
			break;
		default:
			throw new RuntimeException("Game mode can only be NORMALMATH or ENDLESSMATH");
		}

		// add the record to field _results
		addToResults(mode, score);

		// append the record to the local file
		try {
			FileWriter fw = new FileWriter(_localFile, true);
			if (mode == NORMAL) {
				fw.append("Normal" + "," + score + "\n");
			} else if (mode == ENDLESS) {
				fw.append("Endless" + "," + score + "\n");
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		_latestGameMode = gameMode;
		_latestGameScore = score;
	}

	private void addToResults(int mode, int score) {
		if (_results.get(mode) == null) {
			List<Integer> resultList = new ArrayList<Integer>();
			resultList.add(score);
			_results.put(mode, resultList);
		} else {
			_results.get(mode).add(score);
		}
	}

	/**
	 * 
	 * @return the name of the user
	 */
	protected String getName() {
		return _name;
	}

	/**
	 * @return The game mode of the latest game.
	 */
	protected String reportLatestGameMode() {
		if (_latestGameMode == null) {
			return "No Record";
		} else {
			switch (_latestGameMode) {
			case NORMALMATH:
				return "Normal Mode";
			case ENDLESSMATH:
				return "Endless Mode";
			default:
				return "Practise";
			}
		}
	}

	/**
	 * 
	 * @return The score of the latest game
	 */
	protected String reportLatestGameScore() {
		if (_latestGameScore == null) {
			return "No Record";
		} else {
			return _latestGameScore.toString();
		}
	}

	/**
	 * 
	 * @param gameMode
	 * @return An int array of the score history of the game mode, an array of size
	 *         1 which only contains a 0 will be returned if there is no record
	 */
	protected int[] getHistory(Mode gameMode) {
		List<Integer> resultList;
		// get the score history of the specific game mode
		switch (gameMode) {
		case NORMALMATH:
			resultList = _results.get(NORMAL);
			break;
		case ENDLESSMATH:
			resultList = _results.get(ENDLESS);
			break;
		default:
			throw new RuntimeException("Mode can only be NORMALMATH or ENDLESSMATH");
		}

		int[] history;
		if (resultList != null) {
			// if the history is not empty, turn it into an int array and return
			history = new int[resultList.size()];
			for (int i = 0; i < resultList.size(); i++) {
				history[i] = resultList.get(i);
			}
			return history;
		} else {
			// otherwise return an array with size one, where the value is 0
			history = new int[1];
			history[0] = 0;
			return history;
		}
	}

	/**
	 * Search through the score history of normal mode or endless mode games and
	 * return the highest score. Performance is not optimized as the length of the
	 * score history will be small.
	 * 
	 * @return personal highest score of the normal mode or endless mode games, 0
	 *         will be returned if the user has no record in this game mode
	 */
	protected Integer getPersonalBest(Mode gameMode) {
		List<Integer> resultList;
		switch (gameMode) {
		case NORMALMATH:
			resultList = _results.get(NORMAL);
			if (resultList == null) {
				return 0;
			}
			return findMax(resultList);
		case ENDLESSMATH:
			resultList = _results.get(ENDLESS);
			if (resultList == null) {
				return 0;
			}
			return findMax(resultList);
		default:
			throw new RuntimeException("Mode can only be NORMALMATH or ENDLESSMATH");
		}

	}

	private int findMax(List<Integer> resultList) {
		int max = resultList.get(0);
		for (int i = 1; i < resultList.size(); i++) {
			if (resultList.get(i) > max) {
				max = resultList.get(i);
			}
		}
		return max;
	}

}