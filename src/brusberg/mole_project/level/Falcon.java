package brusberg.mole_project.level;

import static org.lwjgl.glfw.GLFW.*;

//import org.lwjgl.glfw.GLFW;

import brusberg.mole_project.Main;
import brusberg.mole_project.graphics.Shader;
import brusberg.mole_project.graphics.Texture;
import brusberg.mole_project.graphics.VertexArray;
import brusberg.mole_project.input.Input;
import brusberg.mole_project.math.Matrix4f;
import brusberg.mole_project.math.Vector3f;

public class Falcon{

	private static float SIZE = 1.5f;
	private VertexArray mesh;
	private Texture texture;
	
	public static Vector3f posistion = new Vector3f();
	private float rot;
	private float delta = 0.0f;
	
	public Falcon(){
		
		float[]vertices = new float[]{
			//x, y, z
				-SIZE / 2.0f, - SIZE / 2.0f, 0.2f,
				-SIZE / 2.0f,   SIZE / 2.0f, 0.2f,
				 SIZE / 2.0f,   SIZE / 2.0f, 0.2f,
				 SIZE / 2.0f, - SIZE / 2.0f, 0.2f
				  /*
				-10.0f, -10.0f * 9.0f / 16.0f, 0.0f,
				-10.0f,  10.0f * 9.0f / 16.0f, 0.0f,
				  0.0f,  10.0f * 9.0f / 16.0f, 0.0f,
				  0.0f, -10.0f * 9.0f / 16.0f, 0.0f
				   */
		};
		//Basically everything in gaming is made out of triangles, so indices stops from repeating vertices because their will be matching vertices with 2 triangles making 1 rectangle
		byte[] indicies = new byte[]{
				0, 1, 2,
				2, 3, 0
				//The triangles made^ with the vertices above
		};
		
		float[] tcs = new float[]{
				//Texture Coordinates of the vertices above
				0, 1,//For vertices 0
				0, 0,//For vertices 1
				1, 0,//For vertices 2
				1, 1 //For vertices 3
		};
		
		//Takes the values above passed into VertexArray class to create an openGL object for the texture
		mesh = new VertexArray(vertices, indicies, tcs);
		texture = new Texture("res/Falcon1.png");
		//*****
	}
	
	public void update(){
		
		/*if(Input.keys[GLFW.GLFW_KEY_LEFT])
			posistion.x -=0.1f;
		if(Input.keys[GLFW.GLFW_KEY_RIGHT])
			posistion.x +=0.1f;*/
		
		/*
		if (Input.isKeyDown(GLFW_KEY_SPACE)){
			//delta = -0.15f;
		}else{ 
			//delta += 0.01f;
		}
		
		if(Input.keys[GLFW.GLFW_KEY_UP])
			posistion.y +=0.1f;
		if(Input.keys[GLFW.GLFW_KEY_DOWN])
			posistion.y -=0.1f;
		*/
	
		if(Main.control ==true){
		if(Input.isKeyDown(GLFW_KEY_UP)){
			delta -=0.01f;
		}else if(!(Input.isKeyDown(GLFW_KEY_UP)) && delta < 0){
			delta +=0.01f;
		}
		
		if(Input.isKeyDown(GLFW_KEY_DOWN)){
			delta +=0.01f;
		}else if(!(Input.isKeyDown(GLFW_KEY_DOWN)) && delta > 0){
			delta -=0.01f;
		}
		}
		rot = -delta * 90.0f;
		
		posistion.y -= delta;
		
		if(Main.control == false){
			
		}
		
	}
	
	
	public void render(){
		Shader.FALCON.enable();
		Shader.FALCON.setUniformMat4f("ml_matrix", Matrix4f.translate(posistion).multiply(Matrix4f.rotate(rot)));
		texture.bind();
		mesh.render();
		Shader.FALCON.disable();
	}
	
	
	public float getX(){
		return posistion.x;
	}
	
	public float getY(){
		return posistion.y;
	}
	
	
	public static float getWidth(){
		return SIZE;
	}
	
	public static float getHeight(){
		return SIZE;
	}

	public float getSize() {
		
		return SIZE;
	}
	
}
