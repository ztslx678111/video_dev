package cn.hncu.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MergeVideoMp3 {
  
	private String ffmpegEXE;
	
	public MergeVideoMp3(String ffmpegEXE) {
		super();
		this.ffmpegEXE = ffmpegEXE;
	}

	
	public void convertor(String videoInputPath, String mp3InputPath,double seconds,String videoOutputPath) throws Exception {
		// ffmpeg.exe -i 111.mp4 -i 出山.mp3 -t 10 -y new.mp4
		List<String> command = new ArrayList<>();
		command.add(ffmpegEXE);
		
		command.add("-i");
		command.add(videoInputPath);
		
		command.add("-i");
		command.add(mp3InputPath);
		
		command.add("-t");
		command.add(String.valueOf(seconds));
		
		command.add("-y");
		command.add(videoOutputPath);
		
		/*for(String c : command) {
			System.out.println(c);
		}*/
		
		ProcessBuilder bulider = new ProcessBuilder(command);
	    Process process = bulider.start();
	    
	    InputStream errorStream = process.getErrorStream();
	    InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
	    BufferedReader br = new BufferedReader(inputStreamReader);
	    
	    String str = "";
	    while((str = br.readLine()) != null) {
	    }
	    
	    if(br != null) {
	    	br.close();
	    }
	    
	    if(inputStreamReader != null) {
	    	inputStreamReader.close();
	    }
	    
	    if(errorStream != null) {
	    	errorStream.close();
	    }
	}
	
	public static void main(String[] args) {
	   MergeVideoMp3 ffmpeg = new MergeVideoMp3("D:\\ffmpeg\\bin\\ffmpeg.exe");
	   try {
		ffmpeg.convertor("D:\\ffmpeg\\bin\\出山.mp3", "D:\\ffmpeg\\bin\\111.mp4",7.5,"D:\\ffmpeg\\bin\\new.mp4");
	} catch (Exception e) {
		e.printStackTrace();
	}
	}
}
