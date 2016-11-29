package jgraphic;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Music implements Soundable {
	final int EXTERNAL_BUFFER_SIZE = 128;
	private SoundThread soundThread;
	private String loc;
	private boolean garbageCollect = true;
	public Music(String location){
		create(location);
	}
	public Music(String location, double scale){
		create(location);
		setVolume(scale);
	}
	public Music(String location, boolean garbageCollect){
		this.garbageCollect = garbageCollect;
		create(location);
		
	}
	private void create(String loc){
		this.loc = loc;
		soundThread = new SoundThread(loc);
		Thread t = new Thread(soundThread);
		t.setName("Sound Stream for " + loc);
		t.start();
	}
	public void setVolume(double scale){
		soundThread.setVolume(scale);
	}
	public void stopAndReset(){
		soundThread.resetPlaying();
		soundThread.pause();
	}
	public void drainClose(){
		soundThread.kill();
	}
	public boolean playing(){
		return soundThread.playing();
	}
	public void setLoop(boolean mode){
		soundThread.setLoop(mode);
	}
	public void play(){
		soundThread.play();
	}
	public void pause(){
		soundThread.pause();
	}
	public void playFromStart(){
		soundThread.playFromStart();
	}
	@Override
	public void finalize(){
		if(garbageCollect)
			soundThread.kill();
	}
	private class SoundThread implements Runnable{

		private FloatControl volume;
		private boolean loop = false;
		private AudioInputStream audioInputStream = null;
		private SourceDataLine	line = null;
		private boolean running = false;
		private byte[]	abData;
		private boolean alive = true;
		private double lastScale = 1;
		private int nBytesRead;
		public SoundThread(String loc){
			createStream(loc);
			createLine();

		}
		private void createStream(String location){
			InputStream is = getClass().getClassLoader().getResourceAsStream(location);
			InputStream bis = new BufferedInputStream(is);
			try {
				audioInputStream = AudioSystem.getAudioInputStream(bis);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}

		}
		private void createLine(){
			AudioFormat audioFormat = audioInputStream.getFormat();
			DataLine.Info	info = new DataLine.Info(SourceDataLine.class, audioFormat);
			try {
				line = (SourceDataLine) AudioSystem.getLine(info);
				line.open(audioFormat);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
				System.exit(1);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
			//line.start();
			volume = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
			setVolume(lastScale);
		}
		
		public void run() {
			nBytesRead = 0;
			abData = new byte[EXTERNAL_BUFFER_SIZE];
			while(alive){
				while (nBytesRead != -1 && audioInputStream.getFrameLength() > line.getFramePosition()) {
					try {
						if(running)
							nBytesRead = audioInputStream.read(abData, 0, abData.length);
					} catch (IOException e) {
				     	e.printStackTrace();
					}
					if (nBytesRead >= 0) {
				     	line.write(abData, 0, nBytesRead);
					}
				}
				if(loop){
					playFromStart();
				}
			}
			line.drain();
			line.close();
		}
		public synchronized boolean playing(){
			return (running && nBytesRead != -1);
		}
		public synchronized void play(){
			running = true;
			line.start();
		}
		public synchronized void pause(){
			running = false;
			line.stop();
		}
		public synchronized void kill(){
			alive = false;
		}
		public synchronized void resetPlaying(){
			line.stop();
			line.drain();
			line.close();
			running = false;
			nBytesRead = 0;
			abData = new byte[EXTERNAL_BUFFER_SIZE];
			try {
				audioInputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			createStream(loc);
			createLine();
		}
		public synchronized void setVolume(double scale){
			lastScale = scale;
			volume.setValue((float) (scale*(Math.abs(volume.getMinimum()) + Math.abs(volume.getMaximum())) + volume.getMinimum()));
		}
		public synchronized void playFromStart(){
			resetPlaying();
			play();
		}
		public void setLoop(boolean mode){
			loop = mode;
		}
	}
}