package listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.jogamp.opengl.math.VectorUtil;

import main.RenderControl;

public class KeyboardCamera implements KeyListener {

	boolean[] keys = new boolean[1024];
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	public void updateCamera(RenderControl newRenderer) {
		if(keys[KeyEvent.VK_W]){
			VectorUtil.addVec3(newRenderer.camLocation, newRenderer.camLocation, new float[]{0.0f,0.0f,-0.1f});
		}
	}

}
