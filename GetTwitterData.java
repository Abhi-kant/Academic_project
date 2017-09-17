
package DB;

import Analayse.com.datumbox.opensource.classifiers.NaiveBayes;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


public class GetTwitterData {

    static ArrayList<String> lstFitness = new ArrayList();
    static ArrayList<String> lstSprituality = new ArrayList();
    static ArrayList<String> lstSavvy = new ArrayList();
    static ArrayList<String> lstStressed = new ArrayList();
    static ArrayList<String> lstFitnessL = new ArrayList();
    static ArrayList<String> lstSpritualityL = new ArrayList();
    static ArrayList<String> lstSavvyL = new ArrayList();
    static ArrayList<String> lstStressedL = new ArrayList();

    public static void main(String args[]) {
        authenticate("dengue");
    }

    public static void authenticate(String value) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("jNU5eXtnC07jtF1eK8I7MPg22")
                .setOAuthConsumerSecret(
                        "p2VlR9BNpHiCjTODkkPpcUbwEZqLDDuuCSpl1U5N7V4d8Dml1W")
                .setOAuthAccessToken(
                        "4562659459-9kZnpPr5MvZex1HBwuRqOZBGLneuWCsbDXVl03F")
                .setOAuthAccessTokenSecret(
                        "3QjGL0rL5ZAR2Pk54JGmFfkMcb6BuEzFwWMDJUNYSRrIy");
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        Query query = new Query(value);

        QueryResult result;
        try {
            result = twitter.search(query);
            Connect.openConnection();
            for (twitter4j.Status status : result.getTweets()) {
                System.out.println("@" + status.getUser().getScreenName()
                        + ":" + status.getText());
               // System.out.println("location=" + status.getGeoLocation() + "\n" + status.getPlace());

                //String tweet, String user, String location, String place,String created_at,String tweetid
                String location = "";
                try {
                    location = status.getUser().getLocation();
                } catch (Exception e) {
                }
                String country = "";
                try {
                    location = status.getPlace().getCountry();
                } catch (Exception e) {
                }
                Connect.saveTweets(status.getText(), status.getUser().getScreenName(), location, country, status.getCreatedAt().toLocaleString(), Long.toString(status.getId()));
            }
            Connect.closeConnection();
        } catch (TwitterException e) {
            
            e.printStackTrace();
        }
        

    }
 

    public static void classify(String text) {
        lstSprituality.clear();
        lstFitness.clear();
        lstSavvy.clear();
        lstStressed.clear();
      
        NaiveBayes nb=DB.NavieBayesClassifier.trainData();
            
                
        String sql = "select * from tbltweet where tweet like '%" + text + "%'";
        System.out.println("sql" + sql);
        try {
            Connect.openConnection();
            Connect.rs = Connect.stat.executeQuery(sql);
            while (Connect.rs.next()) {
                
                String output =DB.NavieBayesClassifier.analayseData(nb,Connect.rs.getString("tweet"));
             
                if (output.equals("fitness")) {
                    if(!lstFitness.contains(Connect.rs.getString("tweet"))){
                    lstFitness.add(Connect.rs.getString("tweet"));
                    lstFitnessL.add(Connect.rs.getString("location"));
                    output = "Fitness Driven";
                  
                    }
                       
                    
                } else if (output.equals("spiritually")) {
                    if(!lstSprituality.contains(Connect.rs.getString("tweet"))){
                    lstSprituality.add(Connect.rs.getString("tweet"));
                        lstSpritualityL.add(Connect.rs.getString("location"));
                    output = "Spiritually Driven";
                   
                    }
                       
                } else if (output.equals("stresseed")) {
                    if(!lstStressed.contains(Connect.rs.getString("tweet"))){
                    lstStressed.add(Connect.rs.getString("tweet"));
                        lstStressedL.add(Connect.rs.getString("location"));
                    output = "Stressed Driven";
                   
                    }
                       
                }
                else if (output.equals("savvy")) {
                    if(!lstSavvy.contains(Connect.rs.getString("tweet"))){
                    lstSavvy.add(Connect.rs.getString("tweet"));
                        lstSavvyL.add(Connect.rs.getString("location"));
                    output = "Savvy Driven";
                   
                    }
                       
                }

               
            }
            Connect.closeConnection();
          
        } catch (SQLException ex) {
            Logger.getLogger(GetTwitterData.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static ArrayList<String> getFitnessTweets() {
        return lstFitness;
    }
    public static ArrayList<String> getSprituality() {
        return lstSprituality;
    }

    public static ArrayList<String> getstressedTweet() {
        return lstStressed;
    }
    public static ArrayList<String> getSavvyTweet() {
        return lstSavvy;
    }
    public static ArrayList<String> getFitnessTweetsL() {
        return lstFitnessL;
    }
    public static ArrayList<String> getSpritualityL() {
        return lstSpritualityL;
    }

    public static ArrayList<String> getstressedTweetL() {
        return lstStressedL;
    }
    public static ArrayList<String> getSavvyTweetL() {
        return lstSavvyL;
    }

   
}
