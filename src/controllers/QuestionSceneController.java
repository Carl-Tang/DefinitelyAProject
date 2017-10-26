package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import com.jfoenix.controls.JFXProgressBar;

import application.BashProcess;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;

public class QuestionSceneController {
	@FXML
	private Label _question;
	@FXML
	private JFXProgressBar _progressBar;
	@FXML
	private Button _recordBtn;
	private FoundationBoardController _parentController;
	/**
	 * Answer for current question showing
	 */
	private String _answer;

	protected QuestionSceneController(FoundationBoardController foundationBoardController) {
		_parentController = foundationBoardController;
	}

	/**
	 * Event Listener on Button[#_recordBtn].onAction. Start recording user's
	 * speech.
	 * 
	 * @param event
	 */
	@FXML
	public void recordBtnClicked(ActionEvent event) {

		_parentController.incrementTrial();
		_recordBtn.setText("Recording...");
		_recordBtn.setDisable(true);
		Timer timer = new Timer();

		// set up a timer to update the progress indicator every 10ms, until reached
		// 100% after the recording time (get from config) finished
		timer.scheduleAtFixedRate(new TimerTask() {

			int i = 0;
			double total = getRecordingTime() * 100.0;

			@Override
			public void run() {
				_progressBar.setProgress(i / total);
				i++;
				if (i == (int) total) {
					_progressBar.setProgress(100);
					timer.cancel();
				}
			}

		}, 0, 10);

		Task<Void> record = new Task<Void>() {
			@Override
			public Void call() throws IOException {
				String args = _answer + " " + getRecordingTime();
				new BashProcess("./MagicStaff.sh", "record", args);

				return null;
			}
		};
		record.setOnSucceeded(e -> {
			_recordBtn.setText("Checking...");
			_parentController.showResult();
			timer.cancel();
		});

		Thread th = new Thread(record);
		th.setDaemon(true);
		th.start();

	}

	/**
	 * Set the question to show on screen
	 * 
	 * @param question
	 */
	protected void setQuestion(String question, String answer) {
		_question.setText(question);
		_answer = answer;
		_progressBar.setProgress(0);
		_recordBtn.setText("Record");
		_recordBtn.setDisable(false);
	}

	/**
	 * Get the recording time from config file.
	 *
	 * @return
	 */
	private double getRecordingTime() {
		// read local config file
		File configFile = new File("config.properties");

		// default recording time is 3 seconds
		String recordingTime = "3.0";

		try {
			FileReader reader = new FileReader(configFile);
			Properties props = new Properties();
			props.load(reader);

			recordingTime = props.getProperty("recordingTime");
			if (recordingTime == null) {
				recordingTime = "3.0";
			}

			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return Double.parseDouble(recordingTime);
	}
}
