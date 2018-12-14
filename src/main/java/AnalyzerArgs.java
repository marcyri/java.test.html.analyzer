public class AnalyzerArgs {
    private String origin_file_path = "./samples/sample-0-origin.html";
//    private String origin_file_path = "./samples/startbootstrap-freelancer-gh-pages-cut.html";
//    private String other_sample_file_path = "./samples/startbootstrap-freelancer-gh-pages-cut.html";
    private String other_sample_file_path = "./samples/sample-1-evil-gemini.html";
//    private String other_sample_file_path = "./samples/sample-2-container-and-clone.html";
//    private String other_sample_file_path = "./samples/sample-4-the-mash.html";
//    private String other_sample_file_path = "./samples/sample-3-the-escape.html";
    private String origin_element_id = "make-everything-ok-button";

    public AnalyzerArgs(String[] args) {
        if (args.length == 3) {
            this.origin_element_id = args[2];
        }
        if (args.length >= 2) {
            this.origin_file_path = args[0];
            this.other_sample_file_path = args[1];
        }
    }

    public String getOriginFilePath() {
        return origin_file_path;
    }

    public String getOtherSampleFilePath() {
        return other_sample_file_path;
    }

    public String getOriginElementId() {
        return origin_element_id;
    }
}
