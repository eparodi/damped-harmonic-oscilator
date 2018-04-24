#!/bin/bash
time=126227704
dt=30
fps=3600

for t in january february march april may june july august september october november december
do
	time java -jar target/damped-harmonic-oscilator-1.0-SNAPSHOT-jar-with-dependencies.jar -tf $time -dt $dt -vo -fps $fps -pf planets/$t.tsv > $t.xyz
done
