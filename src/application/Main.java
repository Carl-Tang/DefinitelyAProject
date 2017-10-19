package application;

import java.io.IOException;
import java.io.InputStream;

import controllers.FoundationBoardController;
import controllers.HelpController;
import controllers.HomeController;
import controllers.LeaderBoardController;
import controllers.PersonalPanelController;
import controllers.SettingsController;
import enums.Function;
import javafx.application.Application;
import javafx.stage.Stage;
import models.QuestionModel;
import models.UserModel;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;

/**
 * Note: Casting used in creating new instance of controller, inappropriate use
 * may lead to a failure.
 * 
 * @author Eric Chen
 *
 */
public class Main extends Application {

	// constants
	public static final int NUMLOWERBOUND = 1;
	public static final int NUMUPPERBOUND = 99;

	// stages
	private Stage _primaryStage;
	private Stage _helpStage;

	// scenes
	private Scene _homePage;
	private Scene _foundationBoard;
	private Scene _leaderBoard;
	private Scene _settings;
	private Scene _helpScene;

	// controllers
	private HomeController _homePageController;
	private FoundationBoardController _foundationBoardController;
	private LeaderBoardController _leaderBoardController;
	private SettingsController _settingsController;
	private HelpController _helpSceneController;

	@Override
	public void start(Stage primaryStage) {
		_primaryStage = primaryStage;

		// initialize models
		QuestionModel.getInstance();
		UserModel.getInstance();

		// initialize scenes
		_homePage = loadScene("Home.fxml");
		_foundationBoard = loadScene("FoundationBoard.fxml");
		_leaderBoard = loadScene("LeaderBoard.fxml");
		_settings = loadScene("Settings.fxml");
		_helpScene = loadScene("Help.fxml");

		// load controllers
		_homePageController = (HomeController) _homePage.getUserData();
		_foundationBoardController = (FoundationBoardController) _foundationBoard.getUserData();
		_leaderBoardController = (LeaderBoardController) _leaderBoard.getUserData();
		_settingsController = (SettingsController) _settings.getUserData();
		_helpSceneController = (HelpController) _helpScene.getUserData();

		// make links between controllers and the main class
		_homePageController.setParent(this);
		_foundationBoardController.setParent(this);
		_leaderBoardController.setParent(this);
		_settingsController.setParent(this);

		showHome();
	}

	public void Help(Function f) {
		if (_helpStage != null) {
			_helpStage.show();
			_helpStage.toFront();
		} else {
			_helpStage = new Stage();
			_helpSceneController.initData(f);
			showScene(_helpStage, _helpScene);
		}
	}

	public void switchScene(Function function) {
		switch (function) {
		case PRACTISE:
		case MATH:
			showFoundationBoard(function);
			break;
		case SCORE:
			Scene score = loadScene("LeaderBoard.fxml");
			LeaderBoardController controller = (LeaderBoardController) score.getUserData();
			controller.setParent(this);
			showScene(_primaryStage, score);
			break;
		case SETTINGS:
			Scene settings = loadScene("Settings.fxml");
			SettingsController settingsController = (SettingsController) settings.getUserData();
			settingsController.setParent(this);
			showScene(_primaryStage, settings);
			break;
		default:
			break;
		}
	}

	public void showFoundationBoard(Function function) {
		Scene foundationBoard = loadScene("FoundationBoard.fxml");
		FoundationBoardController fdtController = (FoundationBoardController) foundationBoard.getUserData();
		fdtController.setParent(this);
		fdtController.setFunction(function);
		showScene(_primaryStage, foundationBoard);
	}

	/**
	 * Get a FXMLLoader that loads the fxml file into a scene and set the default
	 * style sheet application.css as its style sheet. The scene has its controller
	 * be set as its user data and controller be retrieved by calling getUserData()
	 * on the scene. Null will be returned if the scene cannot be loaded.
	 * 
	 * @param fxml
	 * @return Scene loaded from the fxml file
	 */
	public Scene loadScene(String fxml) {
		// TODO
		// loading fxml from FXML loader
		FXMLLoader loader = new FXMLLoader();
		InputStream in = Main.class.getResourceAsStream("/views/" + fxml);
		loader.setBuilderFactory(new JavaFXBuilderFactory());
		loader.setLocation(Main.class.getResource("/views/" + fxml));
		Scene scene = null;
		try {
			Pane content = (Pane) loader.load(in);
			scene = new Scene(content);
			scene.getStylesheets().add(getClass().getResource("/views/application.css").toExternalForm());
			scene.setUserData(loader.getController());
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return scene;
	}

	/**
	 * Make the scene showing on the stage. If the scene is null or the scene cannot
	 * be set, an error dialog will be popped up.
	 * 
	 * @param stage
	 * @param scene
	 */
	public void showScene(Stage stage, Scene scene) {
		try {
			if (scene == null) {
				throw new RuntimeException();
			}
			stage.setScene(scene);
			stage.sizeToScene();
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

	public void showHome() {
		showScene(_primaryStage, _homePage);
	}

	public void showPersonalPanel(String userName) {
		// TODO Auto-generated method stub
		Scene personal = loadScene("PersonalPanel.fxml");
		PersonalPanelController controller = (PersonalPanelController) personal.getUserData();
		controller.setParent(this);
		controller.showPersonalHistory(userName);
		showScene(_primaryStage, personal);
	}

	public static void main(String[] args) {
		System.out.println("Loading...");
		launch(args);
	}

}
