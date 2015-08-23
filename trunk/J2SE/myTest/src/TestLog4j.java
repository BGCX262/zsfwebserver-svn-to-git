import java.util.logging.Logger;

import org.apache.log4j.PropertyConfigurator;


public class TestLog4j {
	
	static Logger logger = Logger.getLogger(TestLog4j.class.getName());
	
    public static void main(String[] args) {
      PropertyConfigurator.configure(args[0]);
      logger.info("Entering application.");
      /*Bar bar = new Bar();
      bar.doIt();*/
      logger.info("Exiting application.");
    }


}
