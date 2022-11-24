#!/bin/zsh

java GETClient http://127.0.0.1:4567
java GETClient http://127.0.0.1:4567
java GETClient http://127.0.0.1:4567

for file_a in $PWD/*
do
temp_file=`basename $file_a`
if [[ "$temp_file" == Client* ]]; then
java CompareTxt $temp_file
fi
done

sleep 3
echo "the files will be delete"
for file_a in $PWD/*
do
temp_file=`basename $file_a`
if [[ "$temp_file" == Client* ]]; then
rm $temp_file
fi
done