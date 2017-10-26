package testing;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.Test;

public class TestThatDrives {

	@Test
	public void demo() {
		// This is a mock test
	}

	@Test
	public void test1() {
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		String foo = "40+2";
		try {
			// String a = engine.eval(foo).toString();
			System.out.println(engine.eval(foo));
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}
}
