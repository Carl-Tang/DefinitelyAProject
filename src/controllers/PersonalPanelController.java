package controllers;

import application.Main;
import enums.Function;
import enums.Mode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import models.UserModel;

public class PersonalPanelController {

	// Labels
	@FXML
	private Label _name;
	@FXML
	private Label _latestScore;
	@FXML
	private Label _latestMode;
	@FXML
	private Label _normalModeRank;
	@FXML
	private Label _normalModeScore;
	@FXML
	private Label _endlessModeRank;
	@FXML
	private Label _endlessModeScore;

	// charts
	@FXML
	private AreaChart<String, Number> _normalModeChart;
	@FXML
	private AreaChart<String, Number> _endlessModeChart;

	private Main _main;

	public PersonalPanelController(Main main) {
		_main = main;
	}

	/**
	 * Show the history of the user.
	 * 
	 * @param userName
	 */
	public void showPersonalHistory(String userName) {
		UserModel model = UserModel.getInstance();
		// clear previews chart data
		_normalModeChart.getData().clear();
		_endlessModeChart.getData().clear();

		// show name
		_name.setText(userName);

		// show latest score and rank
		_latestScore.setText(model.getLatestGameScore(userName));
		_latestMode.setText(model.getLatestGameMode(userName));

		// show data of normal mode
		_normalModeRank.setText(model.getRank(Mode.NORMALMATH, userName)); // rank
		_normalModeScore.setText(model.getPersonalBest(Mode.NORMALMATH, userName)); // score

		// add history to the chart
		int[] history = model.getPersonalHistory(Mode.NORMALMATH, userName);
		XYChart.Series<String, Number> series = new Series<String, Number>();
		for (int i = 0; i < history.length; i++) {
			series.getData().add(new Data<String, Number>("" + i, history[i]));
		}
		_normalModeChart.getData().add(series);

		// show data of endless mode
		_endlessModeRank.setText(model.getRank(Mode.ENDLESSMATH, userName)); // rank
		_endlessModeScore.setText(model.getPersonalBest(Mode.ENDLESSMATH, userName)); // score

		// add history to the chart
		history = model.getPersonalHistory(Mode.ENDLESSMATH, userName);
		series = new Series<String, Number>();
		for (int i = 0; i < history.length; i++) {
			series.getData().add(new Data<String, Number>("" + i, history[i]));
		}
		_endlessModeChart.getData().add(series);

	}

	/**
	 * Event handler for home button on action. Return to the home page.
	 * 
	 * @param event
	 */
	@FXML
	private void backToHome(ActionEvent event) {
		_main.showHome();
	}

	/**
	 * Event handler for show leader board button on action. Show the leader board.
	 * 
	 * @param event
	 */
	@FXML
	private void showLeaderBoard(ActionEvent event) {
		_main.switchScene(Function.SCORE);
	}

	/**
	 * Event handler for help button on action. Show the help stage.
	 * 
	 * @param event
	 */
	@FXML
	private void showHelp(ActionEvent event) {
		_main.showHelp(Function.SCORE);
	}

}
