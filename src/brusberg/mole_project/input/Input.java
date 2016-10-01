package brusberg.mole_project.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class Input extends GLFWKeyCallback{//Input class for key input that extends an abstract class from lwjgl***Wrapping the input in a class to pass on into main

	public static boolean[] keys = new boolean[65536]; //array of all possible keys basically
	
	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		keys[key] = action != GLFW.GLFW_RELEASE;//Key event for when the mouse is unlicked
		/*#define GLFW_PRESS   1 
		The key or mouse button was pressed. 
		#define GLFW_RELEASE   0   
		The key or mouse button was released. 
		#define GLFW_REPEAT   2 
		The key was held down until it repeated. */
	}
	
	public static boolean isKeyDown(int keycode){
		return keys[keycode];
	}
	

}
