package testing;

import org.junit.Test;

import enums.Mode;
import models.UserModel;

public class TestUserModel {

	@Test
	public void test() {
		UserModel model = UserModel.getInstance();
		model.appendRecord("Demo", Mode.ENDLESSMATH, 110);
	}

}
