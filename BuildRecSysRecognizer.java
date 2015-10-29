package edu.cmu.sphinx.demo.buildrecsyst10team4;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.recognizer.Recognizer.State;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.util.props.PropertyException;

/**
 * responsible for speech recognition for the project
 * 
 * @author eslam
 */
public class BuildRecSysRecognizer implements Runnable {
	private Microphone microphone;
	private Recognizer recognizer;
	private List<RecListener> recListeners = new ArrayList<RecListener>();
	private char theCharacter;
	
	/**
	 * create the recognizer
	 * 
	 * @throws IOException
	 */
	public BuildRecSysRecognizer() throws IOException {
		try {
            URL url = this.getClass().getResource("buildrecsys.config.xml");
            if (url == null) {
                throw new IOException("Can't find buildrecsys.config.xml");
            }
            ConfigurationManager cm = new ConfigurationManager(url);
            recognizer = (Recognizer) cm.lookup("recognizer");
            microphone = (Microphone) cm.lookup("microphone");
        } catch (PropertyException e) {
            throw new IOException("Problem configuring BuildRecSysRecognizer " + e);
        }
		
	}
	
	 /** Turns on the microphone and starts recognition */
    public boolean microphoneOn() {
        if (microphone.getAudioFormat() == null) {
            return false;
        } else {
            new Thread(this).start();
            return true;
        }
    }


    /** Turns off the microphone, ending the current recognition in progress */
    public void microphoneOff() {
        microphone.stopRecording();
    }


    /** Allocates resources necessary for recognition. */
    public void startup() throws IOException {
        recognizer.allocate();
    }


    /** Releases recognition resources */
    public void shutdown() {
        microphoneOff();
        if (recognizer.getState() == State.ALLOCATED) {
            recognizer.deallocate();
        }
    }
	
	@Override
	public void run() {
		microphone.clear();
		microphone.startRecording();
		Result result = recognizer.recognize();
		microphone.stopRecording();
		if(result != null) {
			String resultText = result.getBestFinalResultNoFiller();
			setTheCharacter(resultText.toCharArray()[0]);
			if (!resultText.isEmpty()) {
				fireListeners(resultText);
			} else {
				fireListeners(null);
			}
		
		}
		System.out.println("$$$$$$$$$$$$$$$$$$$$");
		
	}
	
	public void setTheCharacter(char c) {
		this.theCharacter = c;
	}
	
	public char getTheCharacter() {
		return this.theCharacter;
	}
	
	

	public synchronized void addRecListener(RecListener rs) {
		recListeners.add(rs);
	}
	
	public synchronized void removeRecListener(RecListener rs) {
		recListeners.remove(rs);
	}
	
	private synchronized void fireListeners(String word) {
		for(RecListener rl : recListeners) {
			rl.notify(word);
		}
	}
	
	
	
}

interface RecListener {
	
	void notify(String word);
}