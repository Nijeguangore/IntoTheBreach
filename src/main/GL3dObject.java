package main;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class GL3dObject {

	ArrayList<Vertex> vertexes = new ArrayList<>();
	ArrayList<Face> faces = new ArrayList<>();
	public int[] VAO = new int[1];
	private float[] position = new float[3];
	
	public GL3dObject() {
		position[0] = 0;
		position[1] = 0.3f;
		position[2] = 0.0f;
	}
	
	
	
	public float[] getPosition(){
		return position;
	}
	
	public void addVertex(String string, String string2, String string3) {
		Vertex newVertex = new Vertex(Float.parseFloat(string),Float.parseFloat(string2),Float.parseFloat(string3));
		vertexes.add(newVertex);
	}

	public void addFace(String string, String string2, String string3) {
		Face newFace = new Face(Integer.parseInt(string),Integer.parseInt(string2),Integer.parseInt(string3));
		faces.add(newFace);
	}
	
	public class Vertex{
		float vert1;
		float vert2;
		float vert3;
		
		public Vertex(float one, float two,float three){
			vert1 = one;
			vert2 = two;
			vert3 = three;
		}
		
		public float getVertex(int vertex) {
			if(vertex == 0){
				return vert1;
			}
			else if(vertex == 1){
				return vert2;
			}
			else if(vertex == 2){
				return vert3;
			}
			return -1;
		}
		
	}
	public class Face{
		int firstVertex,secondVertex,thirdVertex;
		
		public Face(int first,int second,int third){
			firstVertex = first-1;
			secondVertex = second-1;
			thirdVertex = third-1;
		}

		public int getVertex(int vertex) {
			if(vertex == 0){
				return firstVertex;
			}
			else if(vertex == 1){
				return secondVertex;
			}
			else if(vertex == 2){
				return thirdVertex;
			}
			return -1;
		}
	}
	public ArrayList<Face>  getFaces() {
		return faces;
	}

	public ArrayList<Vertex> getVertexes() {
		
		return vertexes;
	}
}
