import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class EventLogger {
    private Logger logger;
    private FileHandler fh;

    public EventLogger() {
        try {
            File file = new File("logs.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            fh = new FileHandler(file.getName());
            logger = Logger.getLogger("Event Logger");
            logger.addHandler(fh);
            logger.setLevel(Level.INFO);
        }
        catch(Exception e){
            System.out.println(e.getStackTrace());
        }
    }

    public void logInfo(String message){
        logger.info(message);
    }
}
