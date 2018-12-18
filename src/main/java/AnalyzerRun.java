import org.apache.log4j.BasicConfigurator;

public class AnalyzerRun {
    public static void main(String[] args) {
        BasicConfigurator.configure();

        // 1. Args
        AnalyzerArgs analyzerArgs = new AnalyzerArgs(args);

        // 2. Get result
        AnalyzerPage anayzer = new AnalyzerPage(analyzerArgs);
        anayzer.fireAnalyze();
    }
}
