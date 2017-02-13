package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
			ArrayList<GL3dObject> objects = generateObjects();
			mainRenderer.setObjects(objects);
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(cameraControl.doRender()){
			cameraControl.updateCamera(mainRenderer);
			newCanvas.display();
		}
		
		Holster.dispose();
	}
	
	public static ArrayList<GL3dObject> generateObjects() throws FileNotFoundException{
		ArrayList<GL3dObject> objectsList = new ArrayList<>();
		
		File objectDirectory = new File("./ObjectFiles");
		Scanner objectScanner;
		for(File fileName : objectDirectory.listFiles()){
			objectScanner = new Scanner(fileName);
			GL3dObject scannedObject = new GL3dObject();
			while(objectScanner.hasNextLine()){
				String lineToken = objectScanner.nextLine();
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
			objectsList.add(scannedObject);
		}
		return objectsList;
	}

}
