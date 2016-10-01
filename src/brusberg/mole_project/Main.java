package brusberg.mole_project;

//Native bindings to the GLFW library
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
//The core OpenGL 1.1 functionality
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
//Imports NULL values for lwjgl to C
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL;

import brusberg.mole_project.graphics.Shader;
import brusberg.mole_project.input.Input;
import brusberg.mole_project.level.Falcon;
import brusberg.mole_project.level.Level;
import brusberg.mole_project.math.Matrix4f;


public class Main implements Runnable{
	
	private int width = 1440;
	private int height = 810;
	
	private Thread thread;//Declares a new thread
	private boolean running = false;
	
	private long window;//Identifier openGL is C
	
	//Takes in shaders
	private Level level;
	
	public static boolean control = true;
	
	private GLFWKeyCallback keyCallback;// *** FIX WITH IMPORT*********
	
	//============================================================================================================================================
	//============================================================================================================================================
	
	public void start(){
		running = true;
		thread = new Thread(this, "Game"); //Needs to take a Runnable object"this"/needs a function to run *** Game is the name of the thread
		thread.start();//Thread will run the run method(does the run method on thread), Java needs to impplement runnable ***Runnable class which is implemented only has the run method which is overiden below
		/*
		 * All rendering must be done on one thread
		 * Common practice to have 2 threads one for rendering(calling openGl) and the other for game project simple programs do not need this
		 */
	}
	//============================================================================================================================================
	//============================================================================================================================================
	
	private void init(){ //Initializes the game called in the run method which starts on the Game thread at the beggining of the program***Intializes openGL and has to be INTIALIZED on the GAME THREAD or the same thread that is rendering and under the run method
		
		//openGL init---------------------------
		if (glfwInit() != GL_TRUE){//If GLFW does not initialize do >
			//TODO
			return;
		}
		
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);//Finds values of the windows properties i.e refresh rate pixels colors etc blah blah blah and can be resizable
		window = glfwCreateWindow(width, height, "Molennium Falcon", NULL, NULL);//Identifier for window in C
		if (glfwInit() != GL_TRUE) {
			System.err.println("Could not initialize GLFW!");
			return;
		}
		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());//Byte Buffer to pass values of monitor
		glfwSetWindowPos(window, (GLFWvidmode.width(vidmode) / 4), (GLFWvidmode.height(vidmode) /4));//Sets the posistion of the window to the center of the primary screen
		
		glfwSetKeyCallback(window, keyCallback = new Input());//*****FIIXXX*********************//Sets the key callback of the specified window, which is called when a key is pressed, repeated or released. 
		
		glfwMakeContextCurrent(window);//Makes the OpenGL or OpenGL ES context of the specified window current on the calling thread. A context can only be made current on a single thread at a time and each thread can have only a single current context at a time. 
		glfwShowWindow(window);//Shows the window
		GL.createCapabilities();//Registers context in glfw into lwjgl
		
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);//Sets background color to white
		glEnable(GL_DEPTH_TEST);//Enables the specified OpenGL state.
		glEnable(GL_BLEND);
		glActiveTexture(GL_TEXTURE0);//Which texture is being referred to when binding a texture, this case is BG
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		System.out.println("OpenGL: " + glGetString(GL_VERSION));//Sets the key callback of the specified window, which is called when a key is pressed, repeated or released. 
		//openGL init---------------------------
		
		Shader.loadAll();
		
		Matrix4f pr_matrix = Matrix4f.orthographic(-10.0f, 10.0f, -10.0f * 9.0f / 16.0f, 10.0f * 9.0f / 16.0f, -1.0f, 1.0f);
				//What the orthographic"Projection Matrix"(pr_matrix) matrix looks like a 16 by 9
		Shader.BG.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.BG.setUniform1i("tex", 0);
		
		Shader.FALCON.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.FALCON.setUniform1i("tex", 0);
		
		Shader.ROCK.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.ROCK.setUniform1i("tex", 0);
		
		
		level = new Level();
		
	}
	//============================================================================================================================================
	//============================================================================================================================================
	
	public void run(){
		//While the game is running it will maintain to update and render the game
		//Main game loop
		init();//Initializes code
		
		long lastTime = System.nanoTime();
		double delta = 0.0;
		double ns = 1000000000.0 / 60.0;
		int updates = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();
		
		while (running){
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1.0){
				update();
				updates++;
				delta --;
			}
			render();//Left uncapped because why not frames per second does not matter with simple games
			frames++;
			if(System.currentTimeMillis() - timer > 1000){
				timer+= 1000;
				System.out.println(updates + " ups, " + frames + " fps");
				frames = 0;
				updates = 0;
				frames = 0;
			}
			
			if(glfwWindowShouldClose(window) == GL_TRUE){
				running = false;
			}
		}
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
		keyCallback.release();//****FIIIXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX*******************************
	}
	//============================================================================================================================================
	//============================================================================================================================================
	
	private void update() {
		glfwPollEvents();//key Events /\ This function processes only those events that are already in the event queue and then returns immediately. Processing events will cause the window and input callbacks associated with those events to be called.
		level.update();
		/*Flap console
		if(Input.keys[GLFW_KEY_SPACE]){//if input of the key in the class which is release equals space then flap
			System.out.println("Flap!");
		}
		*/
		if(level.isGameOver()){
			level = new Level();
			Falcon.posistion.y = 0.0f;
			control = true;
		}
	}
	//============================================================================================================================================
	//============================================================================================================================================
	
	private void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		level.render();
		
		int error = glGetError();
		if (error != GL_NO_ERROR){
			System.out.println(error);
		}
		glfwSwapBuffers(window);/* In every swap chain there are at least two buffers. The first framebuffer, the screenbuffer, is the buffer that is rendered to the output of the video card. The remaining buffers are known as backbuffers. Each time a new frame is displayed, the first backbuffer in
		 the swap chain takes the place of the screenbuffer, this is called presentation or swapping. A variety of other actions may be taken on the previous screenbuffer and other backbuffers (if they exist). The screenbuffer may be simply overwritten or returned to the
		 back of the swap chain for further processing. The action taken is decided by the client application and is API dependent.
		 */
		
	}
	//============================================================================================================================================
	//============================================================================================================================================
	public static void main(String[] args) {
		new Main().start();
	}

}
