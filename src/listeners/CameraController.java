package listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import com.jogamp.opengl.math.VectorUtil;

import main.RenderControl;

public class CameraController extends MouseMotionAdapter implements KeyListener {

	boolean[] keys = new boolean[1024];
	
	private int lastX,lastY,currX,currY;
	private float pitch = 0.0f;
	private float yaw = -90.0f;
	private boolean initMouse = false;
	private boolean endProg = false;
	
	@Override	
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			endProg = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}
	
	@Override
	public void mouseMoved(MouseEvent e){
		if(!initMouse){
			initMouse = true;
			currX = e.getX();
			currY = e.getY();
		}
		else{
			lastX = currX;
			lastY = currY;
					
			currX = e.getX();
			currY = e.getY();
			if(currX > lastX){
				yaw+=0.4f;
			}
			else if(currX < lastX){
				yaw-=0.4f;
			}
			
			if(pitch < 89.0f && pitch >-89.0f){
				if(lastY > currY){
					pitch += 0.4f;
				}
				else if(lastY<currY){
					pitch -= 0.4f;
				}
			}
			else{
				pitch = (pitch>89.0f)?88.0f:-88.0f;
			}
		}
	}
	public void updateCamera(RenderControl renderSpace) {
		float[] gaze = new float[3];
		gaze[0] = (float) (Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw)));
		gaze[1] = (float) Math.sin(Math.toRadians(pitch));
		gaze[2] = (float) (Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw)));
		
		VectorUtil.normalizeVec3(gaze);
		renderSpace.camLook = gaze;
		
		float[] forward = new float[3];
		float[] right = new float[3];
		VectorUtil.addVec3(forward, forward, renderSpace.camLook);
		VectorUtil.normalizeVec3(forward);
		VectorUtil.scaleVec3(forward, forward, 0.1f);
		
		VectorUtil.crossVec3(right, renderSpace.camUp, renderSpace.camLook);
		VectorUtil.normalizeVec3(right);
		VectorUtil.scaleVec3(right, right, 0.1f);
		
		if(keys[KeyEvent.VK_W]){
			VectorUtil.addVec3(renderSpace.camLocation, renderSpace.camLocation, forward);
		}
		else if(keys[KeyEvent.VK_S]){
			VectorUtil.subVec3(renderSpace.camLocation, renderSpace.camLocation, forward);
		}
		if(keys[KeyEvent.VK_A]){
			VectorUtil.addVec3(renderSpace.camLocation, renderSpace.camLocation, right);
		}
		else if(keys[KeyEvent.VK_D]){
			VectorUtil.subVec3(renderSpace.camLocation, renderSpace.camLocation, right);
		}
	}

	public boolean doRender() {
		return !endProg;
	}

}
