REGISTER libs/piggybank.jar; 
REGISTER libs/json-simple.jar;
REGISTER libs/elephant-bird-core-4.13.jar;
REGISTER libs/elephant-bird-hadoop-compat-4.5.jar;
REGISTER libs/elephant-bird-pig-4.13.jar;
REGISTER UDF/compute.jar

--| tweet id | topic | tweet | word count | negative | positive | date/time | any_other_info... |
tweets = load  '$in_file' using com.twitter.elephantbird.pig.load.JsonLoader('-nestedLoad');
result = FOREACH tweets GENERATE
		$0#'id' as id,
		$0#'created_at' as date_time,
		SIZE(TOKENIZE($0#'text')) as wcount,
		com.twitter.sentiment.compute($0#'text') as sentiment;

describe result;
STORE result INTO '$out_file' USING PigStorage (',');