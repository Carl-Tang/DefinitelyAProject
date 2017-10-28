#!/bin/bash

record(){
	ffmpeg -loglevel quiet -f pulse -i default -t $2 -acodec pcm_s16le -ar 16000 -ac 1 -y $1.wav
}

replay(){
	ffplay -loglevel quiet -autoexit $1.wav;
}

remove(){
	rm -f $1.wav;
}

speechRecognize()
{
HVite -H HTK/HMMs/hmm15/macros -H HTK/HMMs/hmm15/hmmdefs -C HTK/user/configLR  -w HTK/user/wordNetworkNum -o SWT -l '*' -i HTK/recout.mlf -p 0.0 -s 5.0  HTK/user/dictionaryD HTK/user/tiedList "$1".wav
}

# call arguments verbatim:
$@
