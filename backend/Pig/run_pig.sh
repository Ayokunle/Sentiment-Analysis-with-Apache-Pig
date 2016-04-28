#!/bin/bash

mkdir result
hdfs dfs -mkdir hdfs://localhost:9000/user/ayokunle/result/

mkdir result/cruz
for i in `seq 1 10`;
do
	num=$(ls -l tweets/cruz_$i | wc -l)
	for j in `seq 1 $num`;
	do
		pig -f process_tweets.pig -param "in_file=tweets/cruz_$i/file_$j.json" -param "out_file=result/cruz/cruz-$i$j" &&
		hdfs dfs -get hdfs://localhost:9000/user/ayokunle/result/cruz/cruz-$i$j result/cruz
	done
done

mkdir result/trump
for i in `seq 1 10`;
do
	num=$(ls -l tweets/trump_$i | wc -l)
	for j in `seq 1 $num`;
	do
		pig -f process_tweets.pig -param "in_file=tweets/trump_$i/file_$j.json" -param "out_file=result/trump/trump-$i$j" &&
		hdfs dfs -get hdfs://localhost:9000/user/ayokunle/result/trump/trump-$i$j result/trump
	done
done

mkdir result/sanders
for i in `seq 2 10`;
do
	num=$(ls -l tweets/Sanders_$i | wc -l)
	for j in `seq 1 $num`;
	do
		pig -f process_tweets.pig -param "in_file=tweets/Sanders_$i/file_$j.json" -param "out_file=result/sanders/sanders-$i$j" &&
		hdfs dfs -get hdfs://localhost:9000/user/ayokunle/result/sanders/sanders-$i$j result/sanders
	done
done

mkdir result/clinton
for i in `seq 1 10`;
do
	num=$(ls -l tweets/clinton_$i | wc -l)
	for j in `seq 1 $num`;
	do
		pig -x mapreduce -f process_tweets.pig -param "in_file=tweets/clinton_$i/file_$j.json" -param "out_file=result/clinton/clinton-$i$j" &&
		hdfs dfs -get hdfs://localhost:9000/user/ayokunle/result/clinton/clinton-$i$j result/clinton
	done
done   