# Java-Screen-Audio-Video-Recorder
JSAVR (Java Screen audio video recorder) is a very helpful tool to record ones screen's audio and video. 

Run: You can use ant build tool to build and run this code, 
Issuing 'ant clean compile jar run' would be sufficient enough to start the application, or import into an IDE like Eclipse and build and run the main class 'com.sansuns.jsavr.JsavrMain'
It is capable of recording mic or in-line audio. 
Audio, video are recorder and under <installation-dir>\res directory as '<installation-dir>\res\outputAudio.wav' and '<installation-dir>\res\outputVideo.mp4'.
The final merged audio/video file is <installation-dir>\res\outputAuVuMerged.flv. 

To compile and run  Java JDK 5 or above, ant and Xuggler API are needed. This is tested in Windows system only. 

