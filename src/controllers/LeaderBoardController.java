package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import application.Main;
import enums.Function;
import enums.Mode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import models.UserDataTuple;
import models.UserModel;
import models.UserRankListViewCell;
/**
 * This is the controller for leader board which is the main score scene, it connects to personal panel scene.
 * LeaderBoard scene shows the rank of players with their name and a filter which provides the functionality of narrow the searching range of leader board, this can help the user locate him/herself fastly.
 * 
 * @author Carl Tang & Wei Chen
 *
 */
public class LeaderBoardController implements Initializable {

	@FXML
	private JFXButton _personalLogBtn;
	@FXML
	private JFXTextField _nameSearchField;
	@FXML
	private JFXListView<UserDataTuple> _normalModeListView;
	@FXML
	private JFXListView<UserDataTuple> _endlessModeListView;
	private Main _main;
	private String _selectedUser;
	private ObservableList<UserDataTuple> _normalRanking = FXCollections.observableArrayList();
	private ObservableList<UserDataTuple> _endlessRanking = FXCollections.observableArrayList();

	public LeaderBoardController(Main main) {
		_main = main;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// load the data for the leader board
		initData();

		// disable personal log button if there is no user been selected
		_personalLogBtn.setDisable(true);

		/*
		 * Set selected item listener to make the user's selection in a list view
		 * automatically reflect on the other list view
		 */
		_normalModeListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == null) {
				_selectedUser = null;
				_personalLogBtn.setDisable(true);
			} else {
				_selectedUser = newValue.getName();
				_personalLogBtn.setDisable(false);

				for (UserDataTuple item : _endlessModeListView.getItems()) {
					if (newValue.equals(item)) {
						_endlessModeListView.getSelectionModel().select(item);
						break;
					}
				}
			}

		});
		_endlessModeListView.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> {
					if (newValue == null) {
						_selectedUser = null;
						_personalLogBtn.setDisable(true);
					} else {
						_selectedUser = newValue.getName();
						_personalLogBtn.setDisable(false);

						for (UserDataTuple item : _normalModeListView.getItems()) {
							if (newValue.equals(item)) {
								_normalModeListView.getSelectionModel().select(item);
								break;
							}
						}

					}
				});

		/*
		 * Add change listener to the name search field to make the list views show only
		 * the users that match the search
		 */
		_nameSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.isEmpty()) {
				// reset the list view to contain all records
				_normalModeListView.setItems(_normalRanking);
				// _normalModeListView.setCellFactory(lv -> new UserRankListViewCell());
				_endlessModeListView.setItems(_endlessRanking);
			} else {
				// if user entered his/her name, show only the matched records in the list view
				ObservableList<UserDataTuple> nMatchedList = FXCollections.observableArrayList();
				// find and set matched records in normal mode list view
				for (UserDataTuple item : _normalRanking) {
					if (item.getName().toLowerCase().contains(newValue.toLowerCase())) {
						nMatchedList.add(item);
					}
				}
				_normalModeListView.setItems(nMatchedList);

				// find and set matched records in endless mode list view
				ObservableList<UserDataTuple> eMatchedList = FXCollections.observableArrayList();
				for (UserDataTuple item : _endlessRanking) {
					if (item.getName().toLowerCase().contains(newValue.toLowerCase())) {
						eMatchedList.add(item);
					}
				}
				_endlessModeListView.setItems(eMatchedList);
			}

		});

	}

	/**
	 * Get the rankings from the user model and load into the list view
	 */
	public void initData() {
		// load observable lists
		_normalRanking.setAll(UserModel.getInstance().getRankingList(Mode.NORMALMATH));
		_endlessRanking.setAll(UserModel.getInstance().getRankingList(Mode.ENDLESSMATH));

		// setup ListViews
		_normalModeListView.setItems(_normalRanking);
		_normalModeListView.setCellFactory(lv -> new UserRankListViewCell());
		_endlessModeListView.setItems(_endlessRanking);
		_endlessModeListView.setCellFactory(lv -> new UserRankListViewCell());
	}

	/**
	 * Return to home page.
	 * 
	 * @param event
	 */
	@FXML
	private void backToHome(ActionEvent event) {
		_main.showHome();
	}

	/**
	 * Show the personal panel for the selected user
	 * 
	 * @param event
	 */
	@FXML
	private void personalLogBtnClicked(ActionEvent event) {
		_main.showPersonalPanel(_selectedUser);
	}

	/**
	 * Show help stage.
	 * 
	 * @param event
	 */
	@FXML
	private void showHelp(ActionEvent event) {
		_main.showHelp(Function.SCORE);
	}

}
