public class AnalyzerRun {
    public static void main(String[] args) {
        // 1. Args
        AnalyzerArgs analyzerArgs = new AnalyzerArgs(args);

        // 2. Get result
        AnalyzerPage anayzer = new AnalyzerPage(analyzerArgs);
        anayzer.fireAnalyze();
    }
}
