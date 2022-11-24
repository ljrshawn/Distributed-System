#!/bin/zsh

java ContentServer http://127.0.0.1:4567 input.txt &
java ContentServer http://127.0.0.1:4567 input2.txt &
java ContentServer http://127.0.0.1:4567 input1.txt &

