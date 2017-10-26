package controllers;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class HelpController {

	@FXML
	private StackPane background;

	/**
	 * Event handler for button clicked. Show answer for "What is an Endless Mode
	 * Math Game?".
	 * 
	 * @param event
	 */
	@FXML
	private void helpEndlessMode(ActionEvent event) {
		String title = "What is an Endless Mode Math Game?";
		String body = "In an Endless Mode Math Game, you will be answering math questions \n"
				+ "randomly picked from the default question set*. The game will not finish \n"
				+ "until you clicked the 'Skip the rest and finish' button.\n\n" + "* See \"What is a question set?\"";
		Main.showInfoDialog(title, body, null, background);
	}

	/**
	 * Event handler for button clicked. Show answer for "How to set the maximum
	 * number of tries?".
	 * 
	 * @param event
	 */
	@FXML
	private void helpMaxTrailNum(ActionEvent event) {
		String title = "How to set the maximum number of tries?";
		String body = "1. Go to \"Settings\".\n"
				+ "2. Under \"Recording time and maximum retry number\" block, find \"Maximum number of tries\" setting.\n"
				+ "3. Enter a number greater than 0 in the text field.\n\n"
				+ "Note: The maximum number of tries will be automatically saved when you click the 'Home' button if the number \n"
				+ "is greater than 0.";
		Main.showInfoDialog(title, body, null, background);
	}

	/**
	 * Event handler for button clicked. Show answer for "What is a Normal Mode Math
	 * Game?".
	 * 
	 * @param event
	 */
	@FXML
	private void helpNormalMode(ActionEvent event) {
		String title = "What is a Normal Mode Math Game?";
		String body = "In an Normal Mode Math Game, you will be answering math questions randomly picked \n"
				+ "from the selected question list if there is one*.\n"
				+ "You can customize the size and the questions in the question list**. If not customized,\n"
				+ "the default number of questions in the question list will be 10, and the questions will\n"
				+ "be randomly selected from the default question set***.\n\n" + "* See \"What is a question list?\"\n"
				+ "** See \"How to customize the question list?\"\n" + "*** See \"What is a question set?\"";
		Main.showInfoDialog(title, body, null, background);
	}

	/**
	 * Event handler for button clicked. Show answer for "How to use Practise
	 * Mode?".
	 * 
	 * @param event
	 */
	@FXML
	private void helpPractiseMode(ActionEvent event) {
		String title = "How to use Practise Mode?";
		String body = "In Practise Mode, you can either choose to practise pronouncing a specific number in Maori \n"
				+ "or let the system randomly generate numbers for you to practise. \n"
				+ "If you want to choose a specific number to practise, enter the number you want to practise in the \n"
				+ "text field on the practise mode start page and click the 'Practise This Number' button to start practising.";
		Main.showInfoDialog(title, body, null, background);
	}

	/**
	 * Event handler for button clicked. Show answer for "What is a question list?".
	 * 
	 * @param event
	 */
	@FXML
	private void helpQuestionList(ActionEvent event) {
		String title = "What is a question list?";
		String body = "A question list is a list of math questions to be asked in a Normal Mode Math Game. \n"
				+ "Both the size of the question list and the questions inside the question list can be customized*.\n\n"
				+ "* See \"How to customize the question list?\" for more details";
		Main.showInfoDialog(title, body, null, background);
	}

	/**
	 * Event handler for button clicked. Show answer for "How to customize the
	 * question list?".
	 * 
	 * @param event
	 */
	@FXML
	private void helpQuestionListCustomize(ActionEvent event) {
		String title = "How to customize the question list?";
		String body = "To customize the question list, you need to first select a question set in settings. \n"
				+ "Then you can either set the length of the question list and let the system automatically \n "
				+ "generate a question list from the chosen question set for you, or pick your own questions \n "
				+ "list from the chosen question set. \n\n"
				+ "- To let the system automatically generate a question list, first go to \"settings\", under \n"
				+ "  the \"Choose a question set and generate your own question list\" block, select a question \n"
				+ "  set from the combo box in \"Question sets\" setting and find the \"Question list\" setting. \n"
				+ "  Enter the size of the question list in the text field and click the 'Auto Generate' button \n "
				+ "  to let the system auto-generate a question list. You can disable the auto-generating function \n"
				+ "  of the system by clicking the 'Auto Generate' button again. \n\n"
				+ "- To pick your own question list, go to \"Settings\", also first select a question set, and \n"
				+ "  click the 'Pick yourself' button in \"Customize question list\" setting. A new window will \n"
				+ "  be opened for you to pick questions from the selected question set. Click the '>>' button or \n"
				+ "  press RIGHT to add a question to the question list, or click the '<<' button or press LEFT to\n"
				+ "  remove a question from the question list. After finished editing the question list, click the\n"
				+ "  'Confirm' button or press ENTER or press CTRL+S and the question list will be saved to the system. \n\n"
				+ "Note: the customized question list will not be saved locally, therefore if you want to \n"
				+ "customize your own question list, you need to pick your own question list every time you \n"
				+ "close and restart the program.";
		Main.showInfoDialog(title, body, null, background);
	}

	/**
	 * Event handler for button clicked. Show answer for "What is a question set?".
	 * 
	 * @param event
	 */
	@FXML
	private void helpQuestionSet(ActionEvent event) {
		String title = "What is a question set?";
		String body = "A question set is a set of math questions that can be picked into a question list (see \n"
				+ "\"What is a question list?\" for help) or be randomly chosen to be asked in math games. \n"
				+ "The default question set is \"" + Main.DEFAULT_QUESTION_SET_NAME
				+ "\", this set contains 300 math questions of different \n"
				+ "hardness and is not editable/deletable.\n"
				+ "Apart from the default question set, you can edit/add/delete your own question sets*.\n\n"
				+ "* See \"How to edit/add/delete your own question sets?\" for more details.";
		Main.showInfoDialog(title, body, null, background);
	}

	/**
	 * Event handler for button clicked. Show answer for "How to edit/add/delete
	 * your own question sets?".
	 * 
	 * @param event
	 */
	@FXML
	private void helpQuestionSetsEditAddDelete(ActionEvent event) {
		String title = "How to edit/add/delete your own question sets?";
		String body = "The editing/adding/deleting question set functions are all in \"Settings\", under the \n"
				+ "\"Choose a question set and generate your own question list\" block.\n\n"
				+ "- To edit a question set, in \"Question sets\" setting, choose the question set you want to \n"
				+ "  edit in the combo box and click the 'Edit' button, a new window called \"Set editing page\"*\n"
				+ "  will be opened and you can edit the questions in this window.\n\n"
				+ "- To add a question set, in \"Question sets\" setting, click the 'Add' button, a dialog will \n"
				+ "  open to ask you for a name of the question set. Enter the name you like and click 'OK' to open\n"
				+ "  the \"Set editing page\" in a new window to edit your new question set.\n\n"
				+ "- To delete a question set, in \"Question sets\" setting, choose the question set you want to \n"
				+ "  delete in the combo box and click the 'delete' button to delete this question set.\n\n"
				+ "* See \"How to add/delete questions in a question set?\" for more details\n"
				+ "Note: the default question set cannot be added/edited/deleted.";
		Main.showInfoDialog(title, body, null, background);
	}

	/**
	 * Event handler for button clicked. Show answer for "How to add/delete
	 * questions in a question set?".
	 * 
	 * @param event
	 */
	@FXML
	private void helpEditAQuestionSet(ActionEvent event) {
		String title = "How to add/delete questions in a question set?";
		String body = "To add/delete questions in a question set, you need to open the questioon set in a \"Set\n"
				+ "editing page\" window. For how to open this window, please have a look at \"How to\n"
				+ "edit/add/delete your own question sets?\". In this window, you can do the following:\n\n"
				+ "- To add a question to the question set, pressed CTRL+N or click 'Add' button and enter the \n"
				+ "  math expression in the left text field on the dialog poped up. The answer for the question \n"
				+ "  will be automatically calculated and shown in the right text field. Then click 'Add' or press\n"
				+ "  ENTER to add the question to the question set. Press ESC to close the dialog.\n\n"
				+ "- To delete a question from the question set, select the question and click 'Delete' or press\n"
				+ "  DELETE to delete the selected question.\n\n"
				+ "- Finally, click the 'Done' button or press ENTER or press CTRL+S to finish editing this question set.\n\n"
				+ "Note: the addition and deletion of questions will take effect immediately and is automatically\n"
				+ "saved. Either clicking or not clicking the 'Done' button will have no control on the change\n"
				+ "of the question set. Be careful when adding and deleting questions.";
		Main.showInfoDialog(title, body, null, background);
	}

	/**
	 * Event handler for button clicked. Show answer for "How to set the recording
	 * time limit?".
	 * 
	 * @param event
	 */
	@FXML
	private void helpRecordingTime(ActionEvent event) {
		String title = "How to set the recording time limit?";
		String body = "1. Go to \"Settings\".\n"
				+ "2. Under \"Recording time and maximum retry number\" block, find \"Recording time\" setting.\n"
				+ "3. Choose a recording time from the combo box.\n\n"
				+ "Note: The recording time will be automatically saved when you click the 'Home' button.";
		Main.showInfoDialog(title, body, null, background);
	}

	/**
	 * Event handler for button clicked. Show answer for "How is my score calculated
	 * for math games?".
	 * 
	 * @param event
	 */
	@FXML
	private void helpScore(ActionEvent event) {
		String title = "How is my score calculated for math games?";
		String body = "Every question has a hardness factor, this factor is decided by the length of the Maori\n"
				+ "pronounciation for the answer of the question. A one-word answer has a hardness factor of 1.0,\n"
				+ "a two-word answer has a factor of 1.2, a three-word answer is 1.4, and four-word is 1.6.\n\n"
				+ "- For Normal Mode Math Games*, the average hardness factor is calculated from the questions\n"
				+ "  you have already done. The factor is then timed by the correctness rate of this game (e.g.\n"
				+ "  if you have done 4 questions out of 10, and you got 3 of them correct, then your correctness\n"
				+ "  rate is 0.3). The calculated number will then be timed by \"1 + the size of the question list /\n"
				+ "  100\". Then the calculated number will be timed 100 and this will be your current score in the game.\n\n"
				+ "- For Endless Mode Math Games**, the average hardness factor is calculated using the same procedure\n"
				+ "  as Normal Mode Math Games. The factor is then timed by the number of questions you have got correct\n"
				+ "  (e.g. if you have done 4 questions and got 3 of them correct, the factor will be timed by 3). Then\n"
				+ "  the calculated number will be timed by 10 and this will be your current score in the game.\n\n"
				+ "* See \"What is a Normal Mode Math Game?\"\n" + "** See \"What is an Endless Mode Math Game?\"";
		Main.showInfoDialog(title, body, null, background);
	}

}
