# java.test.html.analyzer
find element in target file from original file 

# For run:
cd java.test.html.analyzer/build/libs/samples/  
java -cp "java.test.html.analyzer-0.1.0.jar;*" AnalyzerRun [origin_file_path] [other_sample_file_path] [origin_element_id]  

# For check:
java.test.html.analyzer/build/libs/samples/  
java -cp "java.test.html.analyzer-0.1.0.jar;*" AnalyzerRun - *with default args "./samples/sample-0-origin.html" "./samples/sample-1-evil-gemini.html" "make-everything-ok-button"  
java -cp "java.test.html.analyzer-0.1.0.jar;*" AnalyzerRun "./samples/sample-0-origin.html" "./samples/sample-2-container-and-clone.html"  
java -cp "java.test.html.analyzer-0.1.0.jar;*" AnalyzerRun "./samples/sample-0-origin.html" "./samples/sample-3-the-escape.html"  
java -cp "java.test.html.analyzer-0.1.0.jar;*" AnalyzerRun "./samples/sample-0-origin.html" "./samples/sample-4-the-mash.html"  
java -cp "java.test.html.analyzer-0.1.0.jar;*" AnalyzerRun "./samples/sample-0-origin.html" "./samples/startbootstrap-freelancer-gh-pages-cut.html"  
java -cp "java.test.html.analyzer-0.1.0.jar;*" AnalyzerRun "./samples/startbootstrap-freelancer-gh-pages-cut_origin.html" "./samples/startbootstrap-freelancer-gh-pages-cut.html" "sendMessageButton"  
