package testing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockMain {
	public static void main(String[] args) {
		Map<String, String> a = new HashMap<String, String>();
		a.put("1", "qw");
		a.put("2", "as");
		List<String> ls = new ArrayList<String>(a.keySet());
		a.remove("2");
		for (int i = 0; i < a.size(); i++) {
			System.out.println(ls.get(i));
			System.out.println(a.get(ls.get(i)));
		}
	}
}
