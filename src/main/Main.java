package main;

import javax.swing.JFrame;

import com.jogamp.opengl.awt.GLCanvas;

import listeners.KeyboardCamera;

public class Main {

	public static void main(String[] args) {
		GLCanvas newCanvas = new GLCanvas();
		
		KeyboardCamera newKeyListener = new KeyboardCamera();
		RenderControl newRenderer = new RenderControl();
		newCanvas.addGLEventListener(newRenderer);
		newCanvas.addKeyListener(newKeyListener);
		
		JFrame Holster = new JFrame();
		Holster.getContentPane().add(newCanvas);
		Holster.setSize(1200,900);
		Holster.setVisible(true);
		
		while(true){
			newKeyListener.updateCamera(newRenderer);
			newCanvas.display();
		}
		

	}

}
