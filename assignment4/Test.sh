#!/bin/zsh

for file_a in $PWD/*
do
temp_file=`basename $file_a`
if [[ "$temp_file" == *name.txt ]]; then
rm $temp_file
fi
done

javac *.java
java Test Default DefNode6.txt
