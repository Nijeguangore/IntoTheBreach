package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JFrame;

import com.jogamp.opengl.awt.GLCanvas;

import listeners.CameraController;

public class Main {

	public static void main(String[] args) {
		GLCanvas newCanvas = new GLCanvas();
		
		CameraController cameraControl = new CameraController();
		RenderControl mainRenderer = new RenderControl();
		newCanvas.addGLEventListener(mainRenderer);
		newCanvas.addKeyListener(cameraControl);
		newCanvas.addMouseMotionListener(cameraControl);
		
		JFrame Holster = new JFrame();
		Holster.getContentPane().add(newCanvas);
		Holster.setSize(1200,900);
		Holster.setVisible(true);
		
		try {
			Scanner shipScanner = new Scanner(new File("Pship.obj"));
			GL3dObject scannedObject = new GL3dObject();
			while(shipScanner.hasNextLine()){
				String lineToken = shipScanner.nextLine();
				String[] lineSplit = lineToken.split(" ");
				if(lineSplit[0].equals("v")){
					scannedObject.addVertex(lineSplit[1],lineSplit[2],lineSplit[3]);
				}
				else if(lineSplit[0].equals("f")){
					String[] firstToken = lineSplit[1].split("//");
					String[] secondToken = lineSplit[2].split("//");
					String[] thirdToken = lineSplit[3].split("//");
					
					scannedObject.addFace(firstToken[0],secondToken[0],thirdToken[0]);
				}
			}
			mainRenderer.addObject(scannedObject);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Scanner shipScanner = new Scanner(new File("tsquare.obj"));
			GL3dObject scannedObject = new GL3dObject();
			while(shipScanner.hasNextLine()){
				String lineToken = shipScanner.nextLine();
				String[] lineSplit = lineToken.split(" ");
				if(lineSplit[0].equals("v")){
					scannedObject.addVertex(lineSplit[1],lineSplit[2],lineSplit[3]);
				}
				else if(lineSplit[0].equals("f")){
					String[] firstToken = lineSplit[1].split("//");
					String[] secondToken = lineSplit[2].split("//");
					String[] thirdToken = lineSplit[3].split("//");
					
					scannedObject.addFace(firstToken[0],secondToken[0],thirdToken[0]);
				}
			}
			mainRenderer.addObject(scannedObject);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(cameraControl.doRender()){
			cameraControl.updateCamera(mainRenderer);
			newCanvas.display();
		}
		
		Holster.dispose();
	}

}
