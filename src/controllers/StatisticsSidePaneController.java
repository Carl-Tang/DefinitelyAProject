package controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;

public class StatisticsSidePaneController {

	@FXML
	private ListView<Node> _resultListView;

	/**
	 * Append the correctness of the current number to the list view of the
	 * statistics bar. Each time appendResult is called, there will be a new record
	 * added. Only call this method when the current question is finished.
	 * 
	 * @param number
	 * @param isCorrect
	 */
	protected void appendResult(String number, boolean isCorrect) {
		Label result = new Label();
		// calculate the question number of the new result to append
		int questionNumber = _resultListView.getItems().size() + 1;
		if (isCorrect) {
			result.setText("Question " + questionNumber + ":   \"" + number + "\", Correct");
			result.setTextFill(Color.GREEN);
		} else {
			result.setText("Question " + questionNumber + ":   \"" + number + "\", Wrong");
			result.setTextFill(Color.RED);
		}
		_resultListView.getItems().add(result);
	}

	/**
	 * 
	 * @return number of questions been recorded in the statistics bar
	 */
	protected int getNumOfRecords() {
		return _resultListView.getItems().size();
	}

	/**
	 * Clear all records in the statistics bar.
	 */
	protected void reset() {
		_resultListView.getItems().clear();
	}

}
