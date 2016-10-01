package brusberg.mole_project.level;


//import java.awt.Font;
import java.util.Random;

import org.lwjgl.glfw.GLFW;
//import org.newdawn.slick.TrueTypeFont;
//import org.newdawn.slick.Color;

import brusberg.mole_project.level.Rock;
import brusberg.mole_project.Main;
import brusberg.mole_project.graphics.Shader;
import brusberg.mole_project.graphics.Texture;
import brusberg.mole_project.graphics.VertexArray;
import brusberg.mole_project.input.Input;
import brusberg.mole_project.math.Matrix4f;
import brusberg.mole_project.math.Vector3f;

public class Level {

	private VertexArray background, fade;
	private Texture bgTexture;
	
	private int xScroll = 0;
	private int map = 0;
	
	private Falcon falcon;
	
	private Rock[] rocks = new Rock[5 * 2];
	
	private int index = 0; 
	
	private Random random = new Random();
	
	private int OFFSET = 15;
	
	public boolean control = true, reset = false;
	
	private float time = 0;
	
	//private int scoreTimer, score;
	
	
	/*TrueTypeFont font;
	Font awtFont = new Font("Garamond", java.awt.Font.BOLD, 11);
    font = new TrueTypeFont(awtFont, false);
	*/
	public Level(){
		
		float[]vertices = new float[]{
			//x, y, z
				-10.0f, -10.0f * 9.0f / 16.0f, 0.0f,
				-10.0f,  10.0f * 9.0f / 16.0f, 0.0f,
				  0.0f,  10.0f * 9.0f / 16.0f, 0.0f,
				  0.0f, -10.0f * 9.0f / 16.0f, 0.0f
				  /*
				   * -10.0f, -10.0f * 9.0f / 16.0f, 0.0f,
				-10.0f,  10.0f * 9.0f / 16.0f, 0.0f,
				  0.0f,  10.0f * 9.0f / 16.0f, 0.0f,
				  0.0f, -10.0f * 9.0f / 16.0f, 0.0f
				   */
		};
		//Basically everything in gaming is made out of triangles, so indices stops from repeating vertices because their will be matching vertices with 2 triangles making 1 rectangle
		byte[] indicies = new byte[]{
				0,1,2,
				2,3,0
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
		fade = new VertexArray(6);
		background = new VertexArray(vertices, indicies, tcs);
		bgTexture = new Texture("res/SpaceBG2.jpg");
		
		falcon = new Falcon();
		
		createRocks();
	}
	
	private void createRocks() {
		Rock.create();
		for (int i = 0; i < 5 * 2; i += 2) {
			rocks[i] = new Rock(OFFSET + index * 3.0f, random.nextFloat() * 4.0f);
			rocks[i + 1] = new Rock(rocks[i].getX(), rocks[i].getY() - 5.0f);
			index += 2;
		}
	}
	
	public boolean isGameOver(){
		return reset;
	}
	
	public boolean isControlTrue(){
		if (control = true){
			return true;
		}else
			return false;
	}
	
	private void updateRocks(){
		
			rocks[index % 10] =  new Rock(OFFSET + index * 3.0f, random.nextFloat() * 4.0f);
			rocks[(index+ 1) % 10 ] = new Rock(rocks[index % 10].getX(), rocks[index % 10].getY() - ((random.nextFloat() *8.0f) + 2));
			index += 2;
	}
		
	public void update(){
		if(control){
			xScroll -= 4;
		
		if(-xScroll % (4 * 50) == 0) map ++;
		if(-xScroll > 870 && -xScroll % (4 * 50) <= 0)
			updateRocks();
			//scoreTimer++;
		}
		falcon.update();
		if(control && collision()){
			System.out.println("***************");
			time = 1;
			control = false;
			Main.control = false;
		}
		
		if(!control && Input.isKeyDown(GLFW.GLFW_KEY_UP) || !control && Input.isKeyDown(GLFW.GLFW_KEY_DOWN)){
			reset = true;
		}
		
		if(control){
			time += 0.01f;
		}else if(!control){
			time -= 0.008f;
		}
		/*
		if(scoreTimer % 10 == 0){
			score++;
		}
		*/
		
		//font.drawString(100, 50, getScore(), Color.black); //x, y, string to draw, color
	}
	/*
	private String getScore(){
		return Integer.toString(score);
	}
	*/
	private boolean collision() {
		for (int i = 0; i < 10; i++) {
			float fx = -xScroll * 0.03f;
			float fy = falcon.getY();
			float rx = rocks[i].getX();
			float ry = rocks[i].getY();
			
			float fx0 = fx - falcon.getSize() / 2.7f;
			float fx1 = fx + falcon.getSize() / 2.7f;
			float fy0 = fy - falcon.getSize() / 2.7f;
			float fy1 = fy + falcon.getSize() / 2.7f;
			
			float rx0 = rx;
			float rx1 = rx + Rock.getWidth();
			float ry0 = ry;
			float ry1 = ry + Rock.getHeight();
			
			if (fx1 > rx0 && fx0 < rx1) {
				if (fy1 > ry0 && fy0 < ry1) {
					return true;
				}
			}
		}
		return false;
	}
	/*
	private boolean collision() {
		for (int i = 0; i < 9; i++) {
		
			float fx = -xScroll * 0.05f;
			float fy = falcon.getY();
			float rx = rocks[i].getX();
			float ry = rocks[i].getY();
			
			float fx0 = fx - falcon.getSize() / 2.0f;
			float fx1 = fx + falcon.getSize() / 2.0f;
			float fy0 = fy - falcon.getSize() / 2.0f;
			float fy1 = fy + falcon.getSize() / 2.0f;
			
			float rx0 = rx;
			float rx1 = rx + Rock.getWidth();
			float ry0 = ry;
			float ry1 = ry + Rock.getHeight();
			
			if (fx1 > rx0 && fx0 < rx1) {
				if (fy1 > ry0 && fy0 < ry1) {
					return true;
				}
			}
		}
		return false;
	}
	*/
	
	private void renderRocks(){
		Shader.ROCK.enable();
		Shader.ROCK.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(xScroll * 0.03f, 0.0f, 0.0f)));
		Rock.getTexture().bind();
		Rock.getMesh().bind();
		
		for (int i = 0; i < 5 * 2; i++){
			Shader.ROCK.setUniformMat4f("ml_matrix", rocks[i].getModelMatrix());
			Rock.getMesh().draw();
		}
		Rock.getMesh().unbind();
		Rock.getTexture().unbind();
	}
	
	public void render(){
			bgTexture.bind();
			Shader.BG.enable();
			background.bind();
			for(int i = map; i < map + 4; i++){
				Shader.BG.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(i * 10  + xScroll * 0.05f, 0.0f, 0.0f)));
				background.draw();
			}
			Shader.BG.disable();
			bgTexture.unbind();
			
			renderRocks();
			falcon.render();
			
			Shader.FADE.enable();
			Shader.FADE.setUniform1f("time", time);
			fade.render();
			Shader.FADE.disable();
	}
	
	
}
