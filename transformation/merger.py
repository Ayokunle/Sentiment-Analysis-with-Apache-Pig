import os
import os.path
import re
import csv
from datetime import datetime
import time
import pandas as pd


filepaths = []

for dirpath, dirnames, filenames in os.walk("./trump"):
    for filename in [f for f in filenames if f.endswith("00000")]:
        filepaths.append(os.path.join(dirpath, filename))

tup = ()
list = []

tweet_count = 0
total_w = 0
total_p = 0
total_n = 0
with open('trump_nwords.csv', 'w') as nfile, open('trump_pwords.csv', 'w') as pfile, open('trump.csv','w') as completefile, open('trump_timeseries.csv','w') as time_series:
    for fname in filepaths:
        with open(fname) as infile:
            for line in infile:
                tweet_count = tweet_count + 1
            	words = line.split(",")
            	total_w = total_w +int(words[2])

                date_t = datetime.strptime(words[1],"%a %b %d %H:%M:%S +0000 %Y")

                if int(words[6]) != 0 and int(words[6]) != 0:
                    nfile.write(re.sub('[^a-zA-Z0-9]', ' ', words[5]) + " ")
                    total_n = total_n + int(words[6])
                    pfile.write(re.sub('[^a-zA-Z0-9]', ' ', words[3]) + " ")
                    total_p = total_p + int(words[4])
                    completefile.write(line)
                    time_series.write(
                        str(date_t.day)+"/"+str(date_t.month)+ ","
                        +str(date_t.hour) + ","
                        +words[4] +","
                        +words[6]
                    ) 
                    break

            	if int(words[6]) != 0:
                    completefile.write(line)
                    nfile.write(re.sub('[^a-zA-Z0-9]', ' ', words[5]) + " ")
                    total_n = total_n + int(words[6])
                    time_series.write(
                        str(date_t.day)+"/"+str(date_t.month)+ ","
                        +str(date_t.hour) + ","
                        +words[4] +","
                        +words[6]
                    )

            	if int(words[4]) != 0:
                    completefile.write(line)
                    pfile.write(re.sub('[^a-zA-Z0-9]', ' ', words[3]) + " ")
                    total_p = total_p + int(words[4])
                    time_series.write(
                        str(date_t.day)+"/"+str(date_t.month)+ ","
                        +str(date_t.hour) + ","
                        +words[4] +","
                        +words[6]
                    )


df = pd.read_csv('trump_timeseries.csv')
df.columns = ['date', 'hour', 'neg', 'pos']
df = df.sort_values(['date', 'hour'], ascending=[True, True])

df = df.groupby(['date','hour']).sum()
df.to_csv('trump_timeseries.csv', sep=',')
print df

tup = tup + ("trump",)
tup = tup + (tweet_count,)
tup = tup + (total_w,)
tup = tup + (total_p,)
tup = tup + (total_n,)
list.append(tup)
print list


filepaths = []

for dirpath, dirnames, filenames in os.walk("./cruz"):
    for filename in [f for f in filenames if f.endswith("00000")]:
        filepaths.append(os.path.join(dirpath, filename))

tup = ()

tweet_count = 0
total_w = 0
total_p = 0
total_n = 0
with open('cruz_nwords.csv', 'w') as nfile, open('cruz_pwords.csv', 'w') as pfile, open('cruz.csv','w') as completefile, open('cruz_timeseries.csv','w') as time_series:
    for fname in filepaths:
        with open(fname) as infile:
            for line in infile:
                tweet_count = tweet_count + 1
                words = line.split(",")
                total_w = total_w +int(words[2])

                date_t = datetime.strptime(words[1],"%a %b %d %H:%M:%S +0000 %Y")

                if int(words[6]) != 0 and int(words[6]) != 0:
                    nfile.write(re.sub('[^a-zA-Z0-9]', ' ', words[5]) + " ")
                    total_n = total_n + int(words[6])
                    pfile.write(re.sub('[^a-zA-Z0-9]', ' ', words[3]) + " ")
                    total_p = total_p + int(words[4])
                    completefile.write(line)
                    time_series.write(
                        str(date_t.day)+"/"+str(date_t.month)+ ","
                        +str(date_t.hour) + ","
                        +words[4] +","
                        +words[6]
                    ) 
                    break

                if int(words[6]) != 0:
                    completefile.write(line)
                    nfile.write(re.sub('[^a-zA-Z0-9]', ' ', words[5]) + " ")
                    total_n = total_n + int(words[6])
                    time_series.write(
                        str(date_t.day)+"/"+str(date_t.month)+ ","
                        +str(date_t.hour) + ","
                        +words[4] +","
                        +words[6]
                    )

                if int(words[4]) != 0:
                    completefile.write(line)
                    pfile.write(re.sub('[^a-zA-Z0-9]', ' ', words[3]) + " ")
                    total_p = total_p + int(words[4])
                    time_series.write(
                        str(date_t.day)+"/"+str(date_t.month)+ ","
                        +str(date_t.hour) + ","
                        +words[4] +","
                        +words[6]
                    )


df = pd.read_csv('cruz_timeseries.csv')
df.columns = ['date', 'hour', 'neg', 'pos']
df = df.sort_values(['date', 'hour'], ascending=[True, True])

df = df.groupby(['date','hour']).sum()
df.to_csv('cruz_timeseries.csv', sep=',')
print df

tup = tup + ("cruz",)
tup = tup + (tweet_count,)
tup = tup + (total_w,)
tup = tup + (total_p,)
tup = tup + (total_n,)
list.append(tup)
print list


filepaths = []

for dirpath, dirnames, filenames in os.walk("./sanders"):
    for filename in [f for f in filenames if f.endswith("00000")]:
        filepaths.append(os.path.join(dirpath, filename))

tup = ()

tweet_count = 0
total_w = 0
total_p = 0
total_n = 0
with open('sanders_nwords.csv', 'w') as nfile, open('sanders_pwords.csv', 'w') as pfile, open('sanders.csv','w') as completefile, open('sanders_timeseries.csv','w') as time_series:
    for fname in filepaths:
        with open(fname) as infile:
            for line in infile:
                tweet_count = tweet_count + 1
                words = line.split(",")
                total_w = total_w +int(words[2])

                date_t = datetime.strptime(words[1],"%a %b %d %H:%M:%S +0000 %Y")

                if int(words[6]) != 0 and int(words[6]) != 0:
                    nfile.write(re.sub('[^a-zA-Z0-9]', ' ', words[5]) + " ")
                    total_n = total_n + int(words[6])
                    pfile.write(re.sub('[^a-zA-Z0-9]', ' ', words[3]) + " ")
                    total_p = total_p + int(words[4])
                    completefile.write(line)
                    time_series.write(
                        str(date_t.day)+"/"+str(date_t.month)+ ","
                        +str(date_t.hour) + ","
                        +words[4] +","
                        +words[6]
                    ) 
                    break

                if int(words[6]) != 0:
                    completefile.write(line)
                    nfile.write(re.sub('[^a-zA-Z0-9]', ' ', words[5]) + " ")
                    total_n = total_n + int(words[6])
                    time_series.write(
                        str(date_t.day)+"/"+str(date_t.month)+ ","
                        +str(date_t.hour) + ","
                        +words[4] +","
                        +words[6]
                    )

                if int(words[4]) != 0:
                    completefile.write(line)
                    pfile.write(re.sub('[^a-zA-Z0-9]', ' ', words[3]) + " ")
                    total_p = total_p + int(words[4])
                    time_series.write(
                        str(date_t.day)+"/"+str(date_t.month)+ ","
                        +str(date_t.hour) + ","
                        +words[4] +","
                        +words[6]
                    )


df = pd.read_csv('sanders_timeseries.csv')
df.columns = ['date', 'hour', 'neg', 'pos']
df = df.sort_values(['date', 'hour'], ascending=[True, True])

df = df.groupby(['date','hour']).sum()
df.to_csv('sanders_timeseries.csv', sep=',')
print df

tup = tup + ("sanders",)
tup = tup + (tweet_count,)
tup = tup + (total_w,)
tup = tup + (total_p,)
tup = tup + (total_n,)
list.append(tup)
print list


filepaths = []

for dirpath, dirnames, filenames in os.walk("./clinton"):
    for filename in [f for f in filenames if f.endswith("00000")]:
        filepaths.append(os.path.join(dirpath, filename))

tup = ()

tweet_count = 0
total_w = 0
total_p = 0
total_n = 0
with open('clinton_nwords.csv', 'w') as nfile, open('clinton_pwords.csv', 'w') as pfile, open('clinton.csv','w') as completefile, open('clinton_timeseries.csv','w') as time_series:
    for fname in filepaths:
        with open(fname) as infile:
            for line in infile:
                tweet_count = tweet_count + 1
                words = line.split(",")
                total_w = total_w +int(words[2])

                date_t = datetime.strptime(words[1],"%a %b %d %H:%M:%S +0000 %Y")

                if int(words[6]) != 0 and int(words[6]) != 0:
                    nfile.write(re.sub('[^a-zA-Z0-9]', ' ', words[5]) + " ")
                    total_n = total_n + int(words[6])
                    pfile.write(re.sub('[^a-zA-Z0-9]', ' ', words[3]) + " ")
                    total_p = total_p + int(words[4])
                    completefile.write(line)
                    time_series.write(
                        str(date_t.day)+"/"+str(date_t.month)+ ","
                        +str(date_t.hour) + ","
                        +words[4] +","
                        +words[6]
                    ) 
                    break

                if int(words[6]) != 0:
                    completefile.write(line)
                    nfile.write(re.sub('[^a-zA-Z0-9]', ' ', words[5]) + " ")
                    total_n = total_n + int(words[6])
                    time_series.write(
                        str(date_t.day)+"/"+str(date_t.month)+ ","
                        +str(date_t.hour) + ","
                        +words[4] +","
                        +words[6]
                    )

                if int(words[4]) != 0:
                    completefile.write(line)
                    pfile.write(re.sub('[^a-zA-Z0-9]', ' ', words[3]) + " ")
                    total_p = total_p + int(words[4])
                    time_series.write(
                        str(date_t.day)+"/"+str(date_t.month)+ ","
                        +str(date_t.hour) + ","
                        +words[4] +","
                        +words[6]
                    )


df = pd.read_csv('clinton_timeseries.csv')
df.columns = ['date', 'hour', 'neg', 'pos']
df = df.sort_values(['date', 'hour'], ascending=[True, True])

df = df.groupby(['date','hour']).sum()
df.to_csv('clinton_timeseries.csv', sep=',')
print df

tup = tup + ("clinton",)
tup = tup + (tweet_count,)
tup = tup + (total_w,)
tup = tup + (total_p,)
tup = tup + (total_n,)
list.append(tup)
print list


with open('compare.csv','w') as out:
    csv_out=csv.writer(out)
    csv_out.writerow(['candidate', 'total tweets','total words', 'total positives', 'total negatives'])
    for row in list:
        csv_out.writerow(row)