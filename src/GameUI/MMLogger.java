package GameUI;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MMLogger {

	private final Logger logger = Logger.getAnonymousLogger();
	private FileHandler fh = null;

	public MMLogger() {

		try {
			fh = new FileHandler("log.txt",true);


		} catch (Exception e) {
			e.printStackTrace();
		}

	//	fh.setFormatter(new SimpleFormatter());

		logger.addHandler(fh);
	}

	public void doLogging(String msg) {
		logger.setUseParentHandlers(false);

		logger.info(msg);

	}

}
