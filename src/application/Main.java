package application;

import java.io.IOException;
import java.io.InputStream;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXDialog.DialogTransition;

import controllers.FoundationBoardController;
import controllers.HelpController;
import controllers.HomeController;
import controllers.LeaderBoardController;
import controllers.PersonalPanelController;
import controllers.SettingsController;
import enums.Function;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import models.QuestionModel;
import models.UserModel;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;

/**
 * This Class is the entry point of the program. It handles the functionality of
 * initialize base components of the program as well as the exchange of scenes
 * in the program.
 * 
 * @author Carl Tang & Wei Chen
 *
 */
public class Main extends Application {

	// constants
	public static final int NUMBER_LOWER_BOUND = 1;
	public static final int NUMBER_UPPER_BOUND = 99;
	public static final String DEFAULT_QUESTION_SET_NAME = "Default";

	// stages
	private Stage _primaryStage;
	private Stage _helpStage;

	// scenes
	private Pane _homePage;
	private Pane _foundationBoard;
	private Pane _leaderBoard;
	private Pane _personalPanel;
	private Pane _settings;
	private Pane _helpScene;

	// controllers
	private HomeController _homePageController;
	private FoundationBoardController _foundationBoardController;
	private LeaderBoardController _leaderBoardController;
	private SettingsController _settingsController;
	private HelpController _helpSceneController;
	private PersonalPanelController _personalPanelController;

	/**
	 * Initialize some of classes that are needed of the program
	 */
	@Override
	public void start(Stage primaryStage) {
		_primaryStage = primaryStage;
		_primaryStage.setTitle("Taatai!");
		_primaryStage.setMinHeight(660);
		_primaryStage.setMinWidth(1000);

		// initialize models
		QuestionModel.getInstance();
		UserModel.getInstance();
		System.out.println("Models successfully loaded...");

		// initialize controllers
		_homePageController = new HomeController(this);
		_foundationBoardController = new FoundationBoardController(this);
		_leaderBoardController = new LeaderBoardController(this);
		_personalPanelController = new PersonalPanelController(this);
		_settingsController = new SettingsController(this);
		_helpSceneController = new HelpController();
		System.out.println("Controllers successfully initialized...");

		// load FXMLs
		_homePage = loadScene("Home.fxml", _homePageController);
		_foundationBoard = loadScene("FoundationBoard.fxml", _foundationBoardController);
		_leaderBoard = loadScene("LeaderBoard.fxml", _leaderBoardController);
		_personalPanel = loadScene("PersonalPanel.fxml", _personalPanelController);
		_settings = loadScene("Settings.fxml", _settingsController);
		_helpScene = loadScene("Help.fxml", _helpSceneController);
		System.out.println("Views successfully loaded...");
		System.out.println("All done... Home page opening");

		showHome();
	}

	/**
	 * Switch between scenes based on function passed in.
	 * 
	 * @param function
	 */
	public void switchScene(Function function) {
		switch (function) {
		case HOME:
			showScene(_primaryStage, _homePage);
			break;
		case PRACTISE:
		case MATH:
			_foundationBoardController.setFunction(function);
			Platform.runLater(() -> {
				showScene(_primaryStage, _foundationBoard);
			});
			break;
		case SCORE:
			showScene(_primaryStage, _leaderBoard);
			_leaderBoardController.initData();
			break;
		case SETTINGS:
			showScene(_primaryStage, _settings);
			_settingsController.initData();
			break;
		default:
			throw new RuntimeException(
					"Funtions other than HOME,PRACTISE,MATH,SCORE,or SETTINGS are not supported in this method.\n"
							+ "For showing help scene, use showHelp() instead.");
		}
	}

	/**
	 * Pops up a window that displays the help information.
	 * 
	 * @param f
	 */
	public void showHelp(Function f) {
		if (_helpStage != null) {
			_helpStage.show();
			_helpStage.toFront();
		} else {
			_helpStage = new Stage();
			_helpStage.setMinHeight(500);
			_helpStage.setMinWidth(790);
			_helpStage.setTitle("Help");
			showScene(_helpStage, _helpScene);
		}
	}

	/**
	 * Switch to Home page of the program.
	 */
	public void showHome() {
		switchScene(Function.HOME);
	}

	/**
	 * Switch to the personal statistic page of the program
	 * 
	 * @param userName
	 */
	public void showPersonalPanel(String userName) {
		showScene(_primaryStage, _personalPanel);
		_personalPanelController.showPersonalHistory(userName);
	}

	public static void main(String[] args) {
		System.out.println("Loading...");
		launch(args);
	}

	/**
	 * Get a FXMLLoader that loads the FXML file into a root pane and set the
	 * controller passes in as its controller.
	 * 
	 * @param fxml
	 * @param controller
	 * @return Scene loaded from the FXML file
	 */
	public static Pane loadScene(String fxml, Object controller) {
		// loading FXML from FXML loader
		FXMLLoader loader = new FXMLLoader();
		InputStream in = Main.class.getResourceAsStream("/views/" + fxml);
		loader.setBuilderFactory(new JavaFXBuilderFactory());
		loader.setLocation(Main.class.getResource("/views/" + fxml));
		loader.setController(controller);
		Pane root = null;
		try {
			root = (Pane) loader.load(in);
			HBox.setHgrow(root, Priority.ALWAYS);
			VBox.setVgrow(root, Priority.ALWAYS);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return root;
	}

	/**
	 * Make the root showing on the scene of the stage.
	 * 
	 * @throws RuntimeException
	 *             if the root is null If the root cannot be set, an error dialog
	 *             will be popped up.
	 * 
	 * @param stage
	 * @param root
	 */
	public static void showScene(Stage stage, Pane root) {
		try {
			if (root == null) {
				throw new RuntimeException("The root can not be null.");
			}

			Scene scene = stage.getScene();
			if (scene == null) {
				scene = new Scene(root);
				root.resize(scene.getWidth(), scene.getHeight());
				scene.getStylesheets().add(Main.class.getResource("/views/application.css").toExternalForm());
				stage.setScene(scene);
			} else {
				scene.setRoot(root);
				root.resize(scene.getWidth(), scene.getHeight());
			}
			stage.show();
		} catch (RuntimeException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error: Could Not Load Page!");
			alert.setHeaderText("Could not load the page!");
			alert.setContentText("Sorry! An error happened and the page cannot be loaded.");

			alert.showAndWait();
		}
	}

	/**
	 * Show a JFoenix material confirmation dialog on the given background
	 * stackPane.
	 * 
	 * @param title
	 * @param body
	 * @param okHandler
	 * @param cancelHandler
	 * @param background
	 */
	public static void showConfirmDialog(String title, String body, EventHandler<ActionEvent> okHandler,
			EventHandler<ActionEvent> cancelHandler, StackPane background) {

		Text[] heading = { new Text(title) };
		Text[] bodyNodes = { new Text(body) };
		JFXButton okBtn = new JFXButton("OK");
		JFXButton cancelBtn = new JFXButton("Cancel");
		if (okHandler != null) {
			okBtn.addEventHandler(ActionEvent.ACTION, okHandler);
		}
		if (cancelHandler != null) {
			cancelBtn.addEventHandler(ActionEvent.ACTION, cancelHandler);
		}
		Button[] actions = { okBtn, cancelBtn };

		Main.showCustomizableDialog(heading, bodyNodes, actions, background, false);
	}

	/**
	 * Show a JFoenix material error dialog on the given background stackPane.
	 * 
	 * @param title
	 * @param body
	 * @param okHandler
	 * @param background
	 */
	public static void showErrorDialog(String title, String body, EventHandler<ActionEvent> okHandler,
			StackPane background) {
		Main.showInfoDialog(title, body, okHandler, background);
	}

	/**
	 * Show a JFoenix material information dialog on the given background stackPane.
	 * 
	 * @param title
	 * @param body
	 * @param okHandler
	 * @param background
	 */
	public static void showInfoDialog(String title, String body, EventHandler<ActionEvent> okHandler,
			StackPane background) {
		Text[] heading = { new Text(title) };
		Text[] bodyNodes = { new Text(body) };
		JFXButton okBtn = new JFXButton("OK");
		if (okHandler != null) {
			okBtn.addEventHandler(ActionEvent.ACTION, okHandler);
		}
		Button[] actions = { okBtn };

		Main.showCustomizableDialog(heading, bodyNodes, actions, background, false);
	}

	/**
	 * Show a customizable JFoenix material dialog on the given background
	 * StackPane. You can customize event handlers on the actions. And if an action
	 * node is a button, a new mouse clicked event handler which closes the dialog
	 * will be added to the button.
	 * 
	 * @param heading
	 *            - an array of nodes to show on the heading
	 * @param body
	 *            - an array of nodes to show on the body
	 * @param actions
	 *            - an array of nodes set as actions
	 * @param background
	 * @param overlayClose
	 *            - to be passes into the setOverlayClose method of JFXDialog, to
	 *            determine either the dialog will be closed if clicked anywhere
	 *            else away from the dialog
	 */
	public static void showCustomizableDialog(Node[] heading, Node[] body, Node[] actions, StackPane background,
			boolean overlayClose) {
		JFXDialogLayout content = new JFXDialogLayout();
		content.setHeading(heading);
		content.setBody(body);
		content.setActions(actions);

		JFXDialog dialog = new JFXDialog(background, content, DialogTransition.CENTER);
		dialog.setOverlayClose(overlayClose);

		for (Node n : actions) {
			if (n instanceof Button) {
				n.addEventHandler(ActionEvent.ACTION, (e) -> {
					dialog.close();
				});
			}
		}

		dialog.show();

	}

}
