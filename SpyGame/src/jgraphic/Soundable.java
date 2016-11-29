package jgraphic;

public interface Soundable {
	void setLoop(boolean mode);
	void play();
	void pause();
	void setVolume(double scale);
	boolean playing();
	void stopAndReset();
	void drainClose();
}
