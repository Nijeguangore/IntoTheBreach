package main;

import javax.swing.JFrame;

import com.jogamp.opengl.awt.GLCanvas;

public class Main {

	public static void main(String[] args) {
		GLCanvas newCanvas = new GLCanvas();
		
		RenderControl newRenderer = new RenderControl();
		newCanvas.addGLEventListener(newRenderer);
		
		JFrame Holster = new JFrame();
		Holster.getContentPane().add(newCanvas);
		Holster.setSize(1200,900);
		Holster.setVisible(true);
		
		

	}

}
