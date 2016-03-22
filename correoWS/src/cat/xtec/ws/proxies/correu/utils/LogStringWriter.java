package cat.xtec.ws.proxies.correu.utils;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

public class LogStringWriter
        extends StringWriter {

    public static final int DEFAULT_TAB_SPACES = 4;
    public static final String INFO_PREFIX = "[INFO] ";
    public static final String WARN_PREFIX = "[WARNING] ";
    public static final String ERROR_PREFIX = "[ERROR] ";
    private static final String PROPERTY_SEPARATOR = ";";
    private int levelOfIndentation = 0;
    private SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public void println(String line) {
        println(line, this.levelOfIndentation, false, 4);
    }

    public void println(String line, int numberOfIndents) {
        println(line, numberOfIndents, false, 4);
    }

    public void println(String line, int numberOfIndents, boolean asSpaces) {
        println(line, numberOfIndents, asSpaces, 4);
    }

    public void println(String line, int numberOfIndents, boolean asSpaces, int numberOfSpaces) {
        if (line != null) {
            String data = getCurrentDateTime();
            String text = null;
            if (numberOfIndents > 0) {
                String prefix = "";
                for (int i = 0; i < numberOfIndents; i++) {
                    if (!asSpaces) {
                        prefix = prefix + '\t';
                    } else {
                        for (int j = 0; j < numberOfSpaces; j++) {
                            prefix = prefix + ' ';
                        }
                    }
                }
                text = prefix + data + line.replaceAll("\n", new StringBuffer("\n").append(prefix).toString());
            } else {
                text = line;
            }
            getBuffer().append(text);
        }
        getBuffer().append('\n');
    }

    private String getCurrentDateTime() {
        long initTime = System.currentTimeMillis();
        Date initDate = GregorianCalendar.getInstance().getTime();

        return this.df.format(initDate);
    }

    public void error(String line) {
        println("[ERROR] " + line);
    }

    public void info(String line) {
        println("[INFO] " + line);
    }

    public void warning(String line) {
        println("[WARNING] " + line);
    }

    public void printProperty(String propertyName, String value) {
        StringTokenizer tokens = new StringTokenizer(value, ";");
        String key = propertyName + " = ";
        if (tokens.hasMoreTokens()) {
            println(key + tokens.nextToken());
        }
        key = key.replaceAll(".", " ");
        while (tokens.hasMoreTokens()) {
            println(key + tokens.nextToken());
        }
    }

    public void printStackTrace(Exception ex) {
        println(ex.getClass().getName() + ": " + ex.getMessage());
        StackTraceElement[] stack = ex.getStackTrace();
        increaseIndentationLevel();
        for (int i = 0; i < stack.length; i++) {
            println(stack[i].toString());
        }
        decreaseIndentationLevel();
        if (ex.getCause() != null) {
            write("Caused by: ");
            printStackTrace(ex);
        }
    }

    public void addNewLine() {
        getBuffer().append('\n');
    }

    public void increaseIndentationLevel() {
        this.levelOfIndentation += 1;
    }

    public void decreaseIndentationLevel() {
        if (this.levelOfIndentation > 0) {
            this.levelOfIndentation -= 1;
        }
    }

    public int getLevelOfIndentation() {
        return this.levelOfIndentation;
    }
}
