package cn.hncu.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FFMpegTest {
  
	private String ffmpegEXE;
	
	public FFMpegTest(String ffmpegEXE) {
		super();
		this.ffmpegEXE = ffmpegEXE;
	}

	
	public void convertor(String videoInputPath, String videoOutputPath) throws Exception {
		// ffmpeg -i input.mp5 output.avi
		List<String> command = new ArrayList<>();
		command.add(ffmpegEXE);
		command.add("-i");
		command.add(videoInputPath);
		command.add(videoOutputPath);
		
		for(String c : command) {
			System.out.println(c);
		}
		
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
	   FFMpegTest ffmpeg = new FFMpegTest("C:\\ffmpeg\\bin\\ffmpeg.exe");
	   try {
		ffmpeg.convertor("C:\\ffmpeg\\bin\\222.mp4", "C:\\ffmpeg\\bin\\222.avi");
	} catch (Exception e) {
		e.printStackTrace();
	}
	}
}
