class AnalyzerArgs {
    private String origin_file_path = "./samples/sample-0-origin.html";
    private String other_sample_file_path = "./samples/sample-1-evil-gemini.html";
    private String origin_element_id = "make-everything-ok-button";

    AnalyzerArgs(String[] args) {
        if (args.length == 3) {
            this.origin_element_id = args[2];
        }
        if (args.length >= 2) {
            this.origin_file_path = args[0];
            this.other_sample_file_path = args[1];
        }
    }

    String getOriginFilePath() {
        return origin_file_path;
    }

    String getOtherSampleFilePath() {
        return other_sample_file_path;
    }

    String getOriginElementId() {
        return origin_element_id;
    }
}
