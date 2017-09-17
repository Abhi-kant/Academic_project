package DB;

import Analayse.com.datumbox.opensource.classifiers.NaiveBayes;
import Analayse.com.datumbox.opensource.dataobjects.NaiveBayesKnowledgeBase;
import static Analayse.com.datumbox.opensource.examples.NaiveBayesExample.readLines;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class NavieBayesClassifier {
    public static void main(String args[]){
  NaiveBayes nb=trainData();
        analayseData(nb, "The supernatural element in religion can only be the divine character of the moral law.");
    }
     public static String replaceWords(String text) {
        text = text.replaceAll("\\bI\\b", "").trim();
        text = text.replaceAll("\\ba\\b", "").trim();
        text = text.replaceAll("\\bthe\\b", "").trim();
        text = text.replaceAll("\\bis\\b", "").trim();
        text = text.replaceAll("\\bam\\b", "").trim();
        text = text.replaceAll("\\bare\\b", "").trim();
        text = text.replaceAll("\\bthey\\b", "").trim();
        text = text.replaceAll("\\bthose\\b", "").trim();
        text = text.replaceAll("\\bthis\\b", "").trim();
        text = text.replaceAll("\\bwas\\b", "").trim();
        text = text.replaceAll("\\bit\\b", "").trim();
        text = text.replaceAll("\\b@\\b", "").trim();
        text = text.replaceAll("\\s+", " ");
        return text;
    }
     public static NaiveBayes trainData(){
         try {
              Map<String, URL> trainingFiles = new HashMap<String, URL>();
        trainingFiles.put("fitness", NavieBayesClassifier.class.getResource("/datasets/fitness.txt"));
        trainingFiles.put("spiritually", NavieBayesClassifier.class.getResource("/datasets/spiritually.txt"));
        trainingFiles.put("stresseed", NavieBayesClassifier.class.getResource("/datasets/stressed.txt"));
        trainingFiles.put("savvy", NavieBayesClassifier.class.getResource("/datasets/savvy.txt"));
          //loading examples in memory
        Map<String, String[]> trainingExamples = new HashMap<String, String[]>();
        for(Map.Entry<String, URL> entry : trainingFiles.entrySet()) {
            trainingExamples.put(entry.getKey(), readLines(entry.getValue()));
        }
        
        
        NaiveBayes nb = new NaiveBayes();
        nb.setChisquareCriticalValue(1.67); 
        nb.train(trainingExamples);
        
        
        NaiveBayesKnowledgeBase knowledgeBase = nb.getKnowledgeBase();
        
        
       return nb = new NaiveBayes(knowledgeBase);
         } catch (Exception e) {
         }
      return null;
     }
     
    public static String analayseData(NaiveBayes nb,String input){
       input= replaceWords(input.toLowerCase());
       System.out.println("input removed "+input);
          String output = null;
     
        
    
        
         output = nb.predict(input);
         
          System.out.format("The Moode  \"%s\" was classified as \"%s\".%n", input, output);
       
    return output;
    }
}
