package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import enums.Mode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
/**
 * This class is the controller of starting page of math mode game in the program.
 * MathStartPage provides the functionality of taking user's name and choosing the game mode between normal and endless.
 * 
 * @author Carl Tang & Wei Chen
 *
 */
public class MathStartPageController implements Initializable {

	@FXML
	private Button _normalModeButton;

	@FXML
	private Button _endlessModeButton;

	@FXML
	private TextField _userNameTF;

	@FXML
	private Label _tipMessage;

	private FoundationBoardController _parentController;

	public MathStartPageController(FoundationBoardController foundationBoardController) {
		_parentController = foundationBoardController;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		/*
		 * Add a text change listener to the user name text field. When the user input
		 * is changed, check is the new character the user typed a non-alphanumeric
		 * character. If the character is non-alphanumeric, delete the new character and
		 * highlight the tip message by setting the text color to be red. Otherwise
		 * change the message back to its default style.
		 */
		_userNameTF.textProperty().addListener((observable, oldValue, newValue) -> {

			if (!newValue.isEmpty()) {

				// check if the user input only contains alphanumeric characters or underscores
				boolean isValid = true;
				for (char ch : newValue.toCharArray()) {
					if (!Character.isDigit(ch) && !Character.isLetter(ch) && ch != '_') {
						isValid = false;
					}
				}

				if (!isValid) {
					// unto typing
					_userNameTF.setText(oldValue);
					_tipMessage.setTextFill(Color.RED);
				} else {
					_tipMessage.setTextFill(Color.BLACK);
				}
			} else { // if user input is empty
				_tipMessage.setTextFill(Color.BLACK);
			}

		});
	}

	/**
	 * Event handler for _normalModeButton on action. Ask foundation board to start
	 * a normal mode math game
	 * 
	 * @param event
	 */
	@FXML
	private void normalModeButtonClicked(ActionEvent event) {
		if (_userNameTF.getText().isEmpty()) {
			// if the user didn't enter his/her name, use "Guest Player" as the name and
			// start the game. As space cannot exist in user's input, there is no need to
			// worry about that user might type this name as his/her name.
			_parentController.startMathGame(Mode.NORMALMATH, "Guest Player");
		} else {
			_parentController.startMathGame(Mode.NORMALMATH, _userNameTF.getText());
		}
	}

	/**
	 * Event handler for _endlessModeButton on action. Ask foundation board to start
	 * a endless mode math game
	 * 
	 * @param event
	 */
	@FXML
	private void endlessModeButtonClicked(ActionEvent event) {
		if (_userNameTF.getText().isEmpty()) {
			_parentController.startMathGame(Mode.ENDLESSMATH, "Guest Player");
		} else {
			_parentController.startMathGame(Mode.ENDLESSMATH, _userNameTF.getText());
		}
	}

}
