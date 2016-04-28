package com.twitter.sentiment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;
import org.apache.pig.impl.util.UDFContext;

import com.wantedtech.common.xpresso.x;


public class compute extends EvalFunc<String>{

    @Override
    public String exec(Tuple inputTuple) throws IOException {

        if (inputTuple != null && inputTuple.size() > 0) {
        	
            String input = inputTuple.get(0).toString();
            System.out.println(">> "+input);
//          System.out.println(schema);
           
            int abs_pos=0, abs_neg=0, fuz_pos=0, fuz_neg =0;

            input = input.replaceAll("[^a-zA-Z0-9 ]","");
            String [] words = input.split(" ");
            
            ArrayList<String> w_list = new ArrayList<String>(Arrays.asList(words));
            
            for (int i = 0; i < w_list.size(); i++) {
                if (w_list.get(i).length() < 3 ) {
                	w_list.remove(i);
                }
            }
            words = w_list.toArray(new String[w_list.size()]);
            
            String output = "";
            FileSystem fs = FileSystem.get(UDFContext.getUDFContext().getJobConf());
            InputStream in = fs.open(new Path("hdfs://localhost:9000/user/ayokunle/words/positive.txt"));
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            
            //Abs Positive
            for(String word : words ){
            	String line;
                word = word.toLowerCase();
        		while ((line = br.readLine()) != null) {
        			line = line.toLowerCase();
        			if(line.equals(word)){
        				output += word + "|";
//        				output += line + "->" +word +"=" +fuzzyScore(line, word) + "|";
        				abs_pos++;
        				break;
        			}
//        			else if(x.String(line).similarity(word) >= 90){ //ClassNotFoundException
////        			else if(fuzzyScore(line, word) >= 9){ //too inaccurate
////        				output += word + "|";
//        				output += word + "->" +line + "|";
//        				fuz_pos++;
//        				break;
//        			}
        		}
            }
            
            if(output.length() != 0){
            	output = output.substring(0, output.length()-1) ;
            }
            output = output + "," + Integer.toString(abs_pos) +",";
            
            
            in = fs.open(new Path("hdfs://localhost:9000/user/ayokunle/words/negative.txt"));
            br = new BufferedReader(new InputStreamReader(in));
            
            //Abs Negative
            for(String word : words ){
            	String line;
                word = word.toLowerCase();
        		while ((line = br.readLine()) != null) {
        			line = line.toLowerCase();
        			if(line.equals(word)){
        				output += word + "|";
        				//output += line + "->" +word +"=" +fuzzyScore(line, word) + "|";
        				abs_neg++;
        				break;
        			}
//        			else if(x.String(line).similarity(word) >= 90){
////        			else if(fuzzyScore(line, word) >= 9){
//        				output += word + "->" +line + "|";
//        				fuz_neg++;
//        				break;
//        			}
        		}
            }
            
            if(output.charAt(output.length()-1) == '|' ){
            	output = output.substring(0, output.length()-1) ;
            }
            output = output + "," + Integer.toString(abs_neg);
            
            
//            if( output.equals("")){
//            	return output;
//            }else{
//            	return output.substring(0, output.length()-1) ;
//            }
            return output;
        }
        return null;
    }
    
    /**
     * <p>
     * Find the Fuzzy Score which indicates the similarity score between two
     * Strings.
     * </p>
     *
     * <pre>
     * score.fuzzyScore(null, null, null)                                    = IllegalArgumentException
     * score.fuzzyScore("", "", Locale.ENGLISH)                              = 0
     * score.fuzzyScore("Workshop", "b", Locale.ENGLISH)                     = 0
     * score.fuzzyScore("Room", "o", Locale.ENGLISH)                         = 1
     * score.fuzzyScore("Workshop", "w", Locale.ENGLISH)                     = 1
     * score.fuzzyScore("Workshop", "ws", Locale.ENGLISH)                    = 2
     * score.fuzzyScore("Workshop", "wo", Locale.ENGLISH)                    = 4
     * score.fuzzyScore("Apache Software Foundation", "asf", Locale.ENGLISH) = 3
     * </pre>
     *
     * @param term a full term that should be matched against, must not be null
     * @param query the query that will be matched against a term, must not be
     *            null
     * @return result score
     * @throws IllegalArgumentException if either String input {@code null} or
     *             Locale input {@code null}
     */
    public Integer fuzzyScore(CharSequence term, CharSequence query) {
        if (term == null || query == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }

        // fuzzy logic is case insensitive. We normalize the Strings to lower
        // case right from the start. Turning characters to lower case
        // via Character.toLowerCase(char) is unfortunately insufficient
        // as it does not accept a locale.
        final String termLowerCase = term.toString().toLowerCase(Locale.ENGLISH);
        final String queryLowerCase = query.toString().toLowerCase(Locale.ENGLISH);

        // the resulting score
        int score = 0;

        // the position in the term which will be scanned next for potential
        // query character matches
        int termIndex = 0;

        // index of the previously matched character in the term
        int previousMatchingCharacterIndex = Integer.MIN_VALUE;

        for (int queryIndex = 0; queryIndex < queryLowerCase.length(); queryIndex++) {
            final char queryChar = queryLowerCase.charAt(queryIndex);

            boolean termCharacterMatchFound = false;
            for (; termIndex < termLowerCase.length()
                    && !termCharacterMatchFound; termIndex++) {
                final char termChar = termLowerCase.charAt(termIndex);

                if (queryChar == termChar) {
                    // simple character matches result in one point
                    score++;

                    // subsequent character matches further improve
                    // the score.
                    if (previousMatchingCharacterIndex + 1 == termIndex) {
                        score += 2;
                    }

                    previousMatchingCharacterIndex = termIndex;

                    // we can leave the nested loop. Every character in the
                    // query can match at most one character in the term.
                    termCharacterMatchFound = true;
                }
            }
        }

        return score;
    }
}

