package testing;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MockMain {
	public static void main(String[] args) {
		Map<String, String> a = new HashMap<String, String>();
		a.put("1", "qw");
		a.put("2", "as");
		List<String> ls = new ArrayList<String>(a.keySet());
		for (int i=0; i<a.size(); i++) {
			System.out.println(ls.get(i));
		}
	}
}
