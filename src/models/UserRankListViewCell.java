package models;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

/**
 * This class is the customized list cell used in the list views of the
 * LeaderBoard.
 */
public class UserRankListViewCell extends ListCell<UserDataTuple> {

	@FXML
	private HBox _cell;

	@FXML
	private Label _rankLabel;

	@FXML
	private Label _nameLabel;

	@FXML
	private Label _scoreLabel;

	/**
	 * Constructor
	 */
	public UserRankListViewCell() {
		// load the view for the cell
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserRankListCell.fxml"));
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void updateItem(UserDataTuple item, boolean empty) {
		super.updateItem(item, empty);

		if (empty || item == null) {
			setText(null);
			setGraphic(null);
		} else {
			_rankLabel.setText(item.getRank() + "");
			_nameLabel.setText(item.getName());
			_scoreLabel.setText(item.getHighestScore() + "");

			setText(null);
			setGraphic(_cell);
		}
	}

}
