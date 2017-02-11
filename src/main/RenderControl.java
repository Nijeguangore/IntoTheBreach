package main;

import java.awt.event.KeyListener;
import java.awt.peer.KeyboardFocusManagerPeer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.math.FloatUtil;

public class RenderControl implements GLEventListener{
	int vertShader,fragShader, prgrmID;
	String[] vertSrc = {
			"#version 330 core\n"+
			"layout (location = 0) in vec3 position;\n"+
			"uniform mat4 worldSpace;\n"+
			"uniform mat4 cameraSpace;\n"+
			"uniform mat4 clipSpace;\n"+
			"void main(){\n"+
			"gl_Position = clipSpace*cameraSpace*worldSpace * vec4(position,1.0);\n"+
			"}\n"
	};
	String[] fragSrc = {
			"#version 330 core\n"+
			"out vec4 color;\n"+
			"void main(){\n"+
			"color = vec4(1.0f,1.0f,1.0f,1.0f);\n"+
			"}\n"
	};
	
	public float[] camLocation = new float[]{0.0f,0.0f,5.0f};
	
	@Override
	public void init(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();
		gl.glClearColor(1.0f, 0.2f, 0.5f, 1.0f);
		
		vertShader = createShader(gl,GL3.GL_VERTEX_SHADER,vertSrc);
		fragShader = createShader(gl,GL3.GL_FRAGMENT_SHADER,fragSrc);
		
		prgrmID = createProgram(gl,vertShader,fragShader);
		
	}
	
	private int createProgram(GL3 context, int vertShader,int fragShader){
		IntBuffer successBuffer = IntBuffer.wrap(new int[1]);
		
		int program = context.glCreateProgram();
		context.glAttachShader(program, vertShader);
		context.glAttachShader(program, fragShader);
		context.glLinkProgram(program);
		
		context.glGetProgramiv(program, GL3.GL_LINK_STATUS, successBuffer);
		if(successBuffer.get(0) == 0){
			ByteBuffer log = ByteBuffer.wrap(new byte[512]);

			context.glGetProgramInfoLog(program, 512, (IntBuffer)null, log);
			System.err.println(new String(log.array(),Charset.forName("UTF-8")));
			return -1;
		}
		else{
			return program;
		}
		
	}
	
	private int createShader(GL3 context,int type, String[] src){
		IntBuffer successBuffer = IntBuffer.wrap(new int[1]);
		
		int shader = context.glCreateShader(type);
		context.glShaderSource(shader, src.length, src, null);
		context.glCompileShader(shader);
		
		context.glGetShaderiv(shader, GL3.GL_COMPILE_STATUS, successBuffer);
		if(successBuffer.get(0) == 0){
			ByteBuffer log = ByteBuffer.wrap(new byte[512]);
			context.glGetShaderInfoLog(shader, 512, (IntBuffer)null, log);
			System.err.println(new String(log.array(),Charset.forName("UTF-8")));

			return -1;
		}
		else{
			return shader;
		}
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();
		gl.glClear(gl.GL_COLOR_BUFFER_BIT);
		
		float[] vertices = { -0.5f,0.0f,0.0f, 0.5f,0.0f,0.0f, 0.0f,0.5f,0.0f};
		int[] indices = {0,1,2};
		int[] VBO = new int[1];
		int[] EBO = new int[1];
		
		gl.glGenBuffers(1, EBO,0);
		gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, EBO[0]);
		gl.glBufferData(gl.GL_ELEMENT_ARRAY_BUFFER, indices.length*4, IntBuffer.wrap(indices), gl.GL_STATIC_DRAW);
		
		
		gl.glGenBuffers(1, VBO,0);
		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, VBO[0]);
		gl.glBufferData(gl.GL_ARRAY_BUFFER, vertices.length*4, FloatBuffer.wrap(vertices), gl.GL_STATIC_DRAW);
		
		gl.glVertexAttribPointer(0, 3, gl.GL_FLOAT, false, 0, 0);
		gl.glUseProgram(prgrmID);
		
		float[] worldSpace = new float[16];
		int matLocation = gl.glGetUniformLocation(prgrmID, "worldSpace");
		FloatUtil.makeIdentity(worldSpace);
		gl.glUniformMatrix4fv(matLocation, 1, false, FloatBuffer.wrap(worldSpace));
		
		float[] cameraSpace = new float[16];
		
		float[] camLook = new float[]{0.0f,0.5f,0.0f};
		
		float[] camUp = new float[]{0.0f,1.0f,0.0f};
		matLocation = gl.glGetUniformLocation(prgrmID, "cameraSpace");
		FloatUtil.makeIdentity(cameraSpace);
		FloatUtil.makeLookAt(cameraSpace, 0, camLocation , 0, camLook, 0,camUp, 0, new float[16]);
		gl.glUniformMatrix4fv(matLocation, 1, false, FloatBuffer.wrap(cameraSpace));
		
		float[] clipSpace = new float[16];
		FloatUtil.makePerspective(clipSpace, 0, true, 45.0f, 1200/900,0.01f,100.0f);
		matLocation = gl.glGetUniformLocation(prgrmID, "clipSpace");
		gl.glUniformMatrix4fv(matLocation, 1, false, FloatBuffer.wrap(clipSpace));
		
		
		gl.glEnableVertexAttribArray(0);
		
		gl.glPolygonMode(gl.GL_FRONT_AND_BACK, gl.GL_LINE);
		
		gl.glDrawElements(gl.GL_TRIANGLES,3,gl.GL_UNSIGNED_INT, 0);
	}	

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// TODO Auto-generated method stub

	}

}
