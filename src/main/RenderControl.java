package main;

import java.awt.event.KeyListener;
import java.awt.peer.KeyboardFocusManagerPeer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.math.FloatUtil;
import com.jogamp.opengl.math.VectorUtil;

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
	public float[] camLook = new float[]{0.0f,0.0f,-1.0f};
	public float[] camUp = new float[]{0.0f,1.0f,0.0f};
	
	public ArrayList<GL3dObject> objectsToRender = new ArrayList<>();
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
		
		int faceVertCount = 30;
		
		
		
		int[] buffers = prepObjects(gl);
		
		gl.glBindVertexArray(buffers[1]);
		
		gl.glUseProgram(prgrmID);
		
		float[] worldSpace = new float[16];
		int matLocation = gl.glGetUniformLocation(prgrmID, "worldSpace");
		FloatUtil.makeIdentity(worldSpace);
		gl.glUniformMatrix4fv(matLocation, 1, false, FloatBuffer.wrap(worldSpace));
		
		float[] cameraSpace = new float[16];
		
		
		float[] gazeVector = new float[3];
		VectorUtil.addVec3(gazeVector, camLocation, camLook);
		
		matLocation = gl.glGetUniformLocation(prgrmID, "cameraSpace");
		FloatUtil.makeIdentity(cameraSpace);
		FloatUtil.makeLookAt(cameraSpace, 0, camLocation , 0, gazeVector, 0,camUp, 0, new float[16]);
		gl.glUniformMatrix4fv(matLocation, 1, false, FloatBuffer.wrap(cameraSpace));
		
		float[] clipSpace = new float[16];
		FloatUtil.makePerspective(clipSpace, 0, true, 45.0f, 1200/900,0.01f,100.0f);
		matLocation = gl.glGetUniformLocation(prgrmID, "clipSpace");
		gl.glUniformMatrix4fv(matLocation, 1, false, FloatBuffer.wrap(clipSpace));
		
		
		gl.glEnableVertexAttribArray(0);
		
		gl.glPolygonMode(gl.GL_FRONT_AND_BACK, gl.GL_LINE);
		
		gl.glDrawElements(gl.GL_TRIANGLES,faceVertCount,gl.GL_UNSIGNED_INT, 0);
		
		
	}	

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// TODO Auto-generated method stub

	}

	public void addObject(GL3dObject newObject){
		objectsToRender.add(newObject);
	}
	
	private int[] prepObjects(GL3 context){
		int[] buffers = new int[objectsToRender.size()];
		int[] psudoBuffers= new int[2];
		for(int i = 0 ; i < objectsToRender.size(); i++){
			context.glGenVertexArrays(1, buffers, i);
			
			context.glBindVertexArray(buffers[i]);
			
			int vertexCount = objectsToRender.get(i).vertexes.size()*3;
			float[] vertexData = new float[vertexCount];
			for(int j = 0 ; j < vertexCount ; j++){
				int index = j/3;
				int vertex = j%3;
				vertexData[j] = objectsToRender.get(i).getVertexes().get(index).getVertex(vertex);
			}
			
			int faceVertCount = objectsToRender.get(i).faces.size()*3;
			int[] faceArray = new int[faceVertCount];
			for(int j = 0; j < faceVertCount; j++){
				int index = j/3;
				int vertex = j%3;
				
				faceArray[j] = objectsToRender.get(i).getFaces().get(index).getVertex(vertex);
			}
			context.glGenBuffers(1, psudoBuffers,0);
			context.glBindBuffer(context.GL_ELEMENT_ARRAY_BUFFER, psudoBuffers[0]);
			context.glBufferData(context.GL_ELEMENT_ARRAY_BUFFER, faceVertCount*4, IntBuffer.wrap(faceArray), context.GL_STATIC_DRAW);
			
			
			context.glGenBuffers(1, psudoBuffers,1);
			context.glBindBuffer(context.GL_ARRAY_BUFFER, psudoBuffers[1]);
			context.glBufferData(context.GL_ARRAY_BUFFER, vertexCount*4, FloatBuffer.wrap(vertexData), context.GL_STATIC_DRAW);

			context.glVertexAttribPointer(0, 3, context.GL_FLOAT, false, 0, 0);
			context.glBindVertexArray(0);
		}
		return buffers;
	}
}
