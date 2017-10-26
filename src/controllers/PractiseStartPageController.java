package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class PractiseStartPageController implements Initializable {

	@FXML
	private TextField _numberTextField;

	@FXML
	private Button _specificPractiseButton;

	@FXML
	private Button _autoGenerateButton;

	@FXML
	private Label _tipMessage;

	private FoundationBoardController _parentController;

	protected PractiseStartPageController(FoundationBoardController foundationBoardController) {
		// TODO Auto-generated constructor stub
		_parentController = foundationBoardController;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		// disable the specificPractiseButton as initially the user input is empty
		_specificPractiseButton.setDisable(true);
		// set text in the button and tip message according to the number lower bound
		// and upper bound
		_tipMessage.setText("Please enter a number from " + Main.NUMBER_LOWER_BOUND + " to " + Main.NUMBER_UPPER_BOUND);
		_autoGenerateButton
				.setText("Auto Generate (" + Main.NUMBER_LOWER_BOUND + " - " + Main.NUMBER_UPPER_BOUND + ")");

		/*
		 * Add a text change listener to the user name text field. When the user input
		 * is changed, check is the new character the user typed a non-alphanumeric
		 * character. If the character is non-alphanumeric, delete the new character and
		 * highlight the tip message by setting the text color to be red. Otherwise
		 * change the message back to its default style.
		 */
		_numberTextField.textProperty().addListener((observable, oldValue, newValue) -> {

			// boolean value representing that should the specificPractiseButton be disabled
			boolean toBeDisabled = true;

			if (!newValue.isEmpty()) {

				// check are all characters in the user input digits
				boolean isNumber = true;
				for (char ch : newValue.toCharArray()) {
					if (!Character.isDigit(ch)) {
						isNumber = false;
					}
				}

				if (!isNumber) { // check is the input a number
					// unto typing
					_numberTextField.setText(oldValue);
					_tipMessage.setTextFill(Color.RED);
					// toBeDisabled = true;
				} else if (Integer.parseInt(newValue) < Main.NUMBER_LOWER_BOUND
						|| Integer.parseInt(newValue) > Main.NUMBER_UPPER_BOUND) { // check is the input in
					_numberTextField.setText(oldValue); // the correct range
					_tipMessage.setTextFill(Color.RED);

					if (!oldValue.isEmpty()) {
						toBeDisabled = false;
					}

				} else { // user input is valid
					_tipMessage.setTextFill(Color.BLACK);
					toBeDisabled = false;
				}

			} else { // if user input is empty
				_tipMessage.setTextFill(Color.BLACK);
				toBeDisabled = true;
			}

			// disable the specificPractiseButton if needed
			_specificPractiseButton.setDisable(toBeDisabled);
		});
	}

	/**
	 * Event handler for specificPractiseButton on action. Asks
	 * FoundationBoardController to start a practise use the number user entered in
	 * the text field.
	 * 
	 * @param event
	 */
	@FXML
	private void specificPractiseButtonClicked(ActionEvent event) {
		_parentController.startPractise(Integer.parseInt(_numberTextField.getText()));
	}

	/**
	 * Event handler for autoGenerateButton on action. Asks
	 * FoundationBoardController to start a practise use auto generated numbers.
	 * 
	 * @param event
	 */
	@FXML
	private void autoGenerateButtonClicked(ActionEvent event) {
		_parentController.startPractise(null);
	}

}
