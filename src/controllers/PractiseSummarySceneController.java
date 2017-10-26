package controllers;

import java.util.Comparator;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;

public class PractiseSummarySceneController {

	@FXML
	private PieChart _pieChart;
	@FXML
	private Label _percentageLabel;
	@FXML
	private BarChart<String, Number> _barChart;

	/**
	 * Correct rate should be between 0 (all wrong) and 1 (all correct)
	 * 
	 * @param correctRate
	 */
	protected void setCorrectRate(double correctRate) {
		double percentCorrect = correctRate * 100;

		_percentageLabel.setText("" + (int) percentCorrect);

		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
				new PieChart.Data("Correct", percentCorrect), new PieChart.Data("Wrong", 100 - percentCorrect));
		_pieChart.setData(pieChartData);
		pieChartData.get(0).getNode().setStyle("-fx-pie-color: #00c853;");
		pieChartData.get(1).getNode().setStyle("-fx-pie-color: #d50000;");
	}

	/**
	 * Set the data to be shown on the bar chart.
	 * 
	 * @param wrongQuestions
	 *            which is a map with keys of the questions the user did wrong, and
	 *            values of the number of times the user got the question wrong
	 */
	@SuppressWarnings("unchecked")
	protected void setWrongAnswerChartData(Map<String, Integer> wrongQuestions) {
		// create a new series
		XYChart.Series<String, Number> series = new Series<String, Number>();
		// added values to the series
		for (String question : wrongQuestions.keySet()) {
			int timesGetWrong = wrongQuestions.get(question);
			series.getData().add(new Data<String, Number>(question, timesGetWrong));
		}

		// sort the series depending on the times user get the question wrong
		series.getData().sort(new Comparator<Data<String, Number>>() {

			@Override
			public int compare(Data<String, Number> o1, Data<String, Number> o2) {
				int d1 = o1.getYValue().intValue();
				int d2 = o2.getYValue().intValue();
				if (d1 == d2) {
					return 0;
				} else {
					return (d1 > d2 ? -1 : 1);
				}

			}

		});

		_barChart.getData().setAll(series);
	}

}
