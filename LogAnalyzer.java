import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class LogAnalyzer {
    private Queue<String> logQueue = new LinkedList<>();
    private Stack<String> errorStack = new Stack<>();
    private int infoCount = 0;
    private int warnCount = 0;
    private int errorCount = 0;
    private int memoryWarningCount = 0;

    public void readLogFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logQueue.offer(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading log file: " + e.getMessage());
        }
    }

    public void processLogs() {
        while (!logQueue.isEmpty()) {
            String logEntry = logQueue.poll();
            
            if (logEntry.contains("INFO")) {
                infoCount++;
            } else if (logEntry.contains("WARN")) {
                warnCount++;
                if (logEntry.contains("Memory")) {
                    memoryWarningCount++;
                }
            } else if (logEntry.contains("ERROR")) {
                errorCount++;
                errorStack.push(logEntry);
                if (errorStack.size() > 100) {
                    errorStack.remove(0);
                }
            }
        }
    }

    public void printAnalysis() {
        System.out.println("Log Level Counts:");
        System.out.println("INFO: " + infoCount);
        System.out.println("WARN: " + warnCount);
        System.out.println("ERROR: " + errorCount);
        
        System.out.println("\nMemory Warnings: " + memoryWarningCount);
        
        System.out.println("\nLast 100 Errors:");
        for (int i = errorStack.size() - 1; i >= Math.max(0, errorStack.size() - 100); i--) {
            System.out.println(errorStack.get(i));
        }
    }

    public static void main(String[] args) {
        LogAnalyzer analyzer = new LogAnalyzer();
        analyzer.readLogFile("log-data.csv");
        analyzer.processLogs();
        analyzer.printAnalysis();
    }
}
