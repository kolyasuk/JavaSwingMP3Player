package objects;

import java.io.File;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

public class Mp3Player {

	private BasicPlayer player = new BasicPlayer();

	private String currentFileName;
	private double currentVolumeValue;

	public void play(String fileName) {
		try {
			if (currentFileName != null && currentFileName.equals(fileName) && player.getStatus() == BasicPlayer.PAUSED) {
				player.resume();
				return;
			}

			currentFileName = fileName;
			player.open(new File(fileName));
			player.play();
			player.setGain(currentVolumeValue);

		} catch (BasicPlayerException e) {
			e.printStackTrace();
		}

	}

	public void stop() {
		try {
			player.stop();
		} catch (BasicPlayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void pause() {
		try {
			player.pause();
		} catch (BasicPlayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setVolume(int currentVolume, int maximumVolume) {
		try {
			this.currentVolumeValue = currentVolume;
			if (currentVolume == 0)
				player.setGain(0);
			else
				player.setGain(calcVolume(currentVolume, maximumVolume));
		} catch (BasicPlayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private double calcVolume(int currentVolume, int maximumVolume) {
		return currentVolumeValue = (double) currentVolume / (double) maximumVolume;
	}

}
