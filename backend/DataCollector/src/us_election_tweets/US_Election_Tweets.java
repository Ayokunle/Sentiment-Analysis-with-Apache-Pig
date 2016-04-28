package us_election_tweets;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;

/**
 *
 * @author Ayokunle
 */
public class US_Election_Tweets {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        
        ConfigurationBuilder [] cb = new ConfigurationBuilder[1];
        cb[0] = new ConfigurationBuilder();
        
        cb[0].setOAuthConsumerKey("");
        cb[0].setOAuthConsumerSecret("");
        cb[0].setOAuthAccessToken("");
        cb[0].setOAuthAccessTokenSecret("");
        cb[0].setJSONStoreEnabled(true);
        
        Twitter twitter;  
        twitter = new TwitterFactory(cb[0].build()).getInstance();
        String q = "clinton";
        
        US_Election_Tweets ust = new US_Election_Tweets();
        try {
            //ust.query(twitter, 40000, "trump");
            //ust.query(twitter, 40000, "sanders");
            ust.query(twitter, 40000, "donald");
            //ust.query(twitter, 40000, "clinton");
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(US_Election_Tweets.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }
    
    
    int limit_check = 0;
    PreparedStatement preparedStmt;
    Connection conn = null;
    
    public long[] query(Twitter twitter, int num, String search ) throws SQLException, InterruptedException, TwitterException{
        
        String url = "jdbc:mysql://localhost:3306/"; 
        String dbName = "twiter?useServerPrepStmts=false&rewriteBatchedStatements=true";
        String driver = "com.mysql.jdbc.Driver"; 
        String userName = "root"; 
        String password = ""; 
        
        try { 
            Class.forName(driver).newInstance(); 
            conn = DriverManager.getConnection(url+dbName,userName,password); 
        } catch (Exception e) {
            e.printStackTrace(); 
        }

        File file = null;
        BufferedWriter output = null;
        
        int file_id = 1;
        long fileSizeInBytes =0, fileSizeInKB =0, fileSizeInMB= 0;
        
        int i = 0, duplicates = 0;      
        long [] ids = new long[num];
        
        long id =0;
        Query query = new Query(search);
        query.setLang("en");
        query.setSince("2016-01-01");
        query.setUntil("2016-04-09");
        
        //GeoLocation location  = new GeoLocation(-36.850867, 174.760928);
        //query.setGeoCode(location, 1000, Query.Unit.mi);
        
        long lastID = Long.MAX_VALUE;
        ArrayList<Status> tweets = new ArrayList<>();
        int numberOfTweets = num;
        

        limit_check = twitter.getRateLimitStatus().get("/application/rate_limit_status").getRemaining();
        
        try{
            file = new File("C:\\Users\\Ayokunle\\Documents\\College\\Tweets\\"+search + "_" + file_id + ".json");
            output = new BufferedWriter(new FileWriter(file));
        }catch(Exception e){
            e.printStackTrace();
        }
        
        int min_month  = 4;
        while (tweets.size () < numberOfTweets && min_month != 1){
            if (numberOfTweets - tweets.size() > 100){
                query.setCount(100);
            }else{ 
                query.setCount(numberOfTweets - tweets.size());
            }
            try {
                limit_check--;
                System.out.println("Limt  ---" +  limit_check);
                if(limit_check <= 0){
                    System.out.println("In query - Limit Check = " + limit_check);
                    System.out.println("Sleeping - /application/rate_limit_status");
                    Thread.sleep(60000 *1);
                    while(limit_check < 175){
                        try{
                        RateLimitStatus status = twitter.getRateLimitStatus().get("/application/rate_limit_status");
                        limit_check = status.getRemaining();
                        System.out.println("In query, after sleep - Limit Check = " + limit_check);
                        }catch(Exception e){
                            System.out.println("Rate limit not ready - Sleeping for 5 mins - /application/rate_limit_status");
                            Thread.sleep(60000*5);
                        }
                    }
                }
                
                limit_check--;
                System.out.println("Limit  ---" +  limit_check);
                RateLimitStatus status = twitter.getRateLimitStatus().get("/search/tweets");
                int query_s = status.getRemaining();
                System.out.println("/search/tweets remaining  ---" +  query_s);
                if(query_s <= 0){
                    System.out.println("Sleeping - /search/tweets");
                    Thread.sleep(60000 *15);
                    while(query_s < 175){
                        try{
                            status = twitter.getRateLimitStatus().get("/search/tweets");
                            query_s = status.getRemaining();
                        }catch(Exception e){
                            System.out.println("Rate limit not ready - Sleeping for 5 mins - /search/tweets");
                            Thread.sleep(60000*5);
                        }
                    }
                }
                
                if(status.getRemaining() == 0){
                    System.out.println("Error - /search/tweets");
                    System.exit(-1);
                }
                
                QueryResult result = twitter.search(query);
                tweets.addAll(result.getTweets());

                System.out.println("Gathered " + tweets.size() + " tweets");

                for (Status t: tweets){
                    System.out.println(t.getCreatedAt());
                    try{
                        String json = DataObjectFactory.getRawJSON(t);
                        output.append(json+"\n");
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    fileSizeInBytes = file.length();
                    fileSizeInKB = fileSizeInBytes / 1024;
                    fileSizeInMB = fileSizeInKB / 1024;
                    if (fileSizeInMB > 100) {
                        file_id++;
                        try{
                            file = new File("C:\\Users\\Ayokunle\\Documents\\College\\Tweets\\"+search + "_" + file_id + ".json");
                            output = new BufferedWriter(new FileWriter(file));
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }

                    //System.out.println("lang" + t.getLang());
                    //System.out.println("source" + t.getSource());
                    //System.out.println("created at" + t.getCreatedAt());
                    //System.out.println("geo-lat" + t.getGeoLocation().getLatitude());
                    //System.out.println("geo-long" + t.getGeoLocation().getLongitude());
                    //System.out.println("place- bounding type" + t.getPlace().getBoundingBoxType());
                    //System.out.println("place country" + t.getPlace().getCountry());
                    //System.out.println("place - fullname" + t.getPlace().getFullName());
                    //System.out.println("place - geo type" + t.getPlace().getGeometryType());
                    //System.out.println("place - name" + t.getPlace().getName());
                    //System.out.println("place - place type" + t.getPlace().getPlaceType());
                    //System.out.println("place - street add" + t.getPlace().getStreetAddress());
                    //System.out.println("place - geo cord?" + t.getPlace().getGeometryCoordinates());
                    //Thread.sleep(10000);
                    if(t.getId() < lastID) lastID = t.getId(); 
                    id = t.getUser().getId();

                    String query_sql = " insert into "+ search +" (tweet_id, tweet_text, created_at_date, "
                    + "created_at_time, location, geo_lat, geo_long, user_id, is_rt)"
                    + " values (?,?,?,?,?,?,?,?,?)";
                            
                    // create the mysql insert preparedstatement
                    PreparedStatement preparedStmt = conn.prepareStatement(query_sql);
                    preparedStmt.setLong (1, t.getId());
                    preparedStmt.setString  (2, t.getText());
                    java.util.Date utilStartDate = t.getCreatedAt();
                    java.sql.Date sqlStartDate = new java.sql.Date(utilStartDate.getTime());
                    preparedStmt.setString  (3, sqlStartDate.toString());
                    Timestamp ts = new Timestamp(t.getCreatedAt().getTime());
                    preparedStmt.setString (4, ts.toString());
                    try{
                        preparedStmt.setString (5, t.getPlace().getCountry()); 
                    }catch(Exception e){
                        preparedStmt.setString (5, "-"); 
                    }
                    try{
                        preparedStmt.setDouble  (6, t.getGeoLocation().getLatitude());
                        preparedStmt.setDouble   (7,t.getGeoLocation().getLongitude());
                    }catch(Exception e){
                        preparedStmt.setDouble  (6, 0);
                        preparedStmt.setDouble   (7, 0);
                    }
                    preparedStmt.setLong   (8, t.getUser().getId());
                    preparedStmt.setBoolean (9, t.isRetweet());
                    
                    try{
                        // execute the preparedstatement
                        preparedStmt.execute();
                        //ids[i] = t.getUser().getId();
                        i++;
                        
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                        duplicates++;
                    }
                }
                tweets.clear();
            }catch (TwitterException te) {
                System.out.println("Couldn't connect: " + te);
            } 
            query.setMaxId(lastID-1);
        }
        System.out.println("i = " + i);
        System.out.println("duplicates = " + duplicates);
        return ids;
    }
}
