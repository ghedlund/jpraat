package ca.hedlund.jpraat.binding.sys;

public class TestSendPraat {

	private final static String TEST_SCRIPT = """
		writeInfoLine: \"Hello World\"
	""";

	public static void main(String[] args) throws Exception {
		System.out.println(ProcessHandle.current().pid());
		String retVal = SendPraat.sendpraat(null, "Praat", 0L, TEST_SCRIPT);
		System.out.println(retVal);
	}

}
