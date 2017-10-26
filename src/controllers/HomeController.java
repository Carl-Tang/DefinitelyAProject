package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import enums.Function;
import javafx.event.ActionEvent;

public class HomeController implements Initializable {
	// buttons
	@FXML
	private Button _practiseBtn;
	@FXML
	private Button _playBtn;
	@FXML
	private Button _scoreBtn;
	@FXML
	private Button _settingsBtn;
	@FXML
	private Button _helpBtn;

	// icon image views
	@FXML
	private ImageView _practiseIcon;
	@FXML
	private ImageView _playIcon;
	@FXML
	private ImageView _scoreIcon;
	@FXML
	private ImageView _settingsIcon;
	@FXML
	private ImageView _helpIcon;

	private Main _main;

	public HomeController(Main main) {
		_main = main;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// set the icons in the buttons
		_practiseIcon.setImage(new Image(Main.class.getResourceAsStream("/icons/icons8-Reading-96.png")));
		_playIcon.setImage(new Image(Main.class.getResourceAsStream("/icons/icons8-Math-96.png")));
		_scoreIcon.setImage(new Image(Main.class.getResourceAsStream("/icons/icons8-Leaderboard-96.png")));
		_settingsIcon.setImage(new Image(Main.class.getResourceAsStream("/icons/icons8-Settings-96.png")));
		_helpIcon.setImage(new Image(Main.class.getResourceAsStream("/icons/icons8-Question Mark-96.png")));
	}

	/**
	 * Event Listener on Button[#_practiseBtn].onAction
	 * 
	 * @param event
	 */
	@FXML
	private void goPractise(ActionEvent event) {
		_main.switchScene(Function.PRACTISE);
	}

	/**
	 * Event Listener on Button[#_playBtn].onAction
	 * 
	 * @param event
	 */
	@FXML
	private void goPlay(ActionEvent event) {
		_main.switchScene(Function.MATH);
	}

	/**
	 * Event Listener on Button[#_scoreBtn].onAction
	 * 
	 * @param event
	 */
	@FXML
	private void showScore(ActionEvent event) {
		_main.switchScene(Function.SCORE);
	}

	/**
	 * Event Listener on Button[#_settingsBtn].onAction
	 * 
	 * @param event
	 */
	@FXML
	private void goSettings(ActionEvent event) {
		_main.switchScene(Function.SETTINGS);
	}

	/**
	 * Event Listener on Button[#_helpBtn].onAction
	 * 
	 * @param event
	 */
	@FXML
	private void showHelp(ActionEvent event) {
		_main.showHelp(Function.HELP);
	}
}
