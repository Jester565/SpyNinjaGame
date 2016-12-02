package jgraphic;

public interface Soundable {
	double DEFAULT_VOLUME_SCALE = .95;
	
	void setLoop(boolean mode);
	void play();
	void pause();
	void setVolume(double scale);
	boolean playing();
	void stopAndReset();
	void drainClose();
}
