package main;

import javax.swing.JFrame;

import com.jogamp.opengl.awt.GLCanvas;

import listeners.CameraController;

public class Main {

	public static void main(String[] args) {
		GLCanvas newCanvas = new GLCanvas();
		
		CameraController newKeyListener = new CameraController();
		RenderControl newRenderer = new RenderControl();
		newCanvas.addGLEventListener(newRenderer);
		newCanvas.addKeyListener(newKeyListener);
		newCanvas.addMouseMotionListener(newKeyListener);
		
		JFrame Holster = new JFrame();
		Holster.getContentPane().add(newCanvas);
		Holster.setSize(1200,900);
		Holster.setVisible(true);
		
		while(newKeyListener.doRender()){
			newKeyListener.updateCamera(newRenderer);
			newCanvas.display();
		}
		
		Holster.dispose();
	}

}
