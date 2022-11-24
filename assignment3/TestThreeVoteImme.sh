#!/bin/zsh

for file_a in $PWD/*
do
temp_file=`basename $file_a`
if [[ "$temp_file" == *.txt ]]; then
rm $temp_file
fi
done

javac *.java
java ImmediateResponse

echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
echo "Compare result:"
java CompareLeader