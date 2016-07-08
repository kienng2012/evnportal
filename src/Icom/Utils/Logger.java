package Icom.Utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;


/**
 *	Logging class for the pools.
 */
public class Logger {
    // The log file to log to
    private PrintWriter logWriter = null;
    private boolean logVerbose = false;
    private boolean logInfo = false;
    private boolean logWarn = false;
    private boolean logError = false;
    private boolean logCrisis = false;
    private int logDayOfMonth = -1;
    private String origLogName = null;
    private String emailEvents = null;

    public void setLogWriter(String log) throws IOException {
        if (origLogName == null) {
            origLogName = log;
        }

        Calendar cal = Calendar.getInstance();
        int localLogDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        boolean makeNewLog = false;

        if (logDayOfMonth == -1 && logWriter == null) {
            makeNewLog = true; // indicates first start up
        }

        if (origLogName.indexOf("${") > -1 && localLogDayOfMonth != logDayOfMonth) {
            String dateFormat = origLogName.substring(origLogName.indexOf("${") + 2, origLogName.indexOf("}"));
            String logPrefix = origLogName.substring(0, origLogName.indexOf("${"));
            String logSuffix = origLogName.substring(origLogName.indexOf("}") + 1, origLogName.length());
            java.text.SimpleDateFormat sdf2 = new java.text.SimpleDateFormat(dateFormat);
            java.util.Date tmp = new java.util.Date();
            String formattedDate = sdf2.format(tmp);
            log = (logPrefix + formattedDate + logSuffix);
            logDayOfMonth = localLogDayOfMonth;
            makeNewLog = true;
        }

        if (makeNewLog) {

            if (logWriter != null) {
                logWriter.close();
            }
            logWriter = new PrintWriter(new FileOutputStream(log, true), true);
        }
    }

    public void setLogLevel(String level) {
        if (level != null && level.length() > 0) {
            String[] levels = level.split(",");
            for (int i = 0; i < levels.length; i++) {
                if (levels[i].equalsIgnoreCase("verbose")) {
                    logVerbose = true;
                }
                else if (levels[i].equalsIgnoreCase("info")) {
                    logInfo = true;
                }
                else if (levels[i].equalsIgnoreCase("warn")) {
                    logWarn = true;
                }
                else if (levels[i].equalsIgnoreCase("error")) {
                    logError = true;
                }
                else if (levels[i].equalsIgnoreCase("crisis")) {
                    logCrisis = true;
                }
            }
        }
    }

    /**
     *	Print a stack trace from an exception to the logs
     */
    public void printStackTrace(Throwable t) {
        if (logWriter != null) {
            t.printStackTrace(logWriter);
            
        }
        else {
            t.printStackTrace(System.err);
        }

    }

    /**
     *	Close the logger's file handle.
     */
    public void close() {
        if (logWriter != null) {
            logWriter.close();
        }

    }

    /**
     *	Log verbose messages
     */
    public void verbose(String data) {
        if (logVerbose) {
            log("VERBOSE", data);
        }

    }

    /**
     *	Log an info message
     */
    public void info(byte[] data) {
        if (logInfo) {
            log("INFO", new String(data));
        }
    }

    /**
     *	Log an info message
     */
    public void info(String data) {
        if (logInfo) {
            log("INFO", data);
            System.out.println("INFO: " + data);
        }

    }

    /**
     *	Log an warn message
     */
    public void warn(String data) {
        if (logWarn) {
            log("WARN", data);
        }

    }

    /**
     *	Log an error message
     */
    public void error(String data) {
        if (logError) {
            System.out.println(data);
            log("ERROR", data);
        }

    }

    public void print(String data) {
        
    }

    public void email(String eventType, String message) {

        return;

    }

    /**
     *	Log an fatal message and email the world !
     */
    public void crisis(String message) {
    	
        if (logCrisis) {
            //System.out.println(message);
            log("CRISIS", message);
        }

        if (emailEvents == null) {
            return;
        }
        email("CRISIS", message);
    }

    /**
     *	Log an info message
     */
    private void log(String level, String data) {
        try {
            this.setLogWriter(this.origLogName); // alter log name if we need to (if date changes && they want date logging)
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }

        Calendar now = Calendar.getInstance();
        String nowString = now.get(Calendar.DAY_OF_MONTH) + "/" +
            (now.get(Calendar.MONTH) + 1) + "/" +
            now.get(Calendar.YEAR) + " " +
            now.get(Calendar.HOUR_OF_DAY) + ":" +
            now.get(Calendar.MINUTE) + ":" +
            now.get(Calendar.SECOND);

        String message = nowString + " : " + level + ": " + data;

        if (logWriter == null) {
            System.out.println(message);
        }
        else {
        	System.out.println(message);
            logWriter.println(message);
        }
    }

    /**
     *	Print a blank new line / line break to the log
     */
    public void linebreak() {
        if (logWriter == null) {
            System.out.println("\n");
        }
        else {
            logWriter.println("\n");
        }
    }

    public String getLogLevel() {
        // TODO Auto-generated method stub
        return null;
    }

    public int getLogDayOfMonth() {
        return logDayOfMonth;
    }

    public void setLogDayOfMonth(int logDayOfMonth) {
        this.logDayOfMonth = logDayOfMonth;
    }
}
