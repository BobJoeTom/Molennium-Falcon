package brusberg.mole_project.math;

import static java.lang.Math.*;

import java.nio.FloatBuffer;

import brusberg.mole_project.utils.BufferUtils;


	/*
	 Rows by Columns
	 Matrix is a box of numbers, elements are parts of the matrix
	 
	 Adding Matrixes:
	 	Add the value of the same row and column with the corresponding matrix to add
	 	Subtracting is the same thing
	 	
	 Multiplying a Matrix by a Scaler (number out front):
	 	Multiply each element by the Scaler
	 
	 Multiplying Matrices Together:
	 	The first matrix's amount of columns must equal the second matirx's amount of rows
		
		The new matrix will be the first matrix's amount of rows by the amount of the second matrixes columns
		The result of the first row of the new matrix is the first row's elements multiplied by its corresponding element in the 2nd matrix's colum
		
		2, 3, 1		3, 4, 5    	11, 12, 26
		2,-7, 4	 x	1, 1, 4  =	 7, 5, -2
					2, 1, 4
					
	Identity matrices must be a square
	
	[# + # * 4] Is the position in the elements for example
				 Columns
				 0 1 2 3
				---------
			   0|1 0 0 0|
		Rows   1|0 1 0 0|
			   2|0 0 1 0|
			   3|0 0 0 1|
				---------
				---------	
				|A E I L|
				|B F J M|
				|C G J N|
				|D H K O|
				---------
				---------
				|1 2 3 4|
				|5 6 7 8|
				|9 8 7 6|
				|5 4 3 2|
				---------
				
				
				
				********************************OPENGL******************************
		openGL reverses the order of matrices to column row order instead of row column order
				********************************OPENGL******************************
	 */

//Class that holds a matrix of 4 by 4 of floats
public class Matrix4f {
	
	public static final int SIZE = 4 * 4;
	public float[] elements = new float[SIZE];//4 by 4 array of floats, single dimensional because thats how you pass it into openGL
	
	public Matrix4f(){
		
	}
	
	public static Matrix4f identity(){
		Matrix4f result = new Matrix4f();
		for (int i = 0; i < 4 * 4; i ++){
			result.elements[i] = 0.0f;
		}
		//Sets it all to 0
		
		//Result.elements [Row + Column * 4(Constant width) = 1.0f (Identity
		result.elements [0 + 0 * 4] = 1.0f;
		result.elements [1 + 1 * 4] = 1.0f;
		result.elements [2 + 2 * 4] = 1.0f;
		result.elements [3 + 3 * 4] = 1.0f;
		//Sets certain elements to 1
		
		return result;
		//Returns the result of the matrix as the matrix object
		//static because it returns the same result
	}
	
	public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far){
		Matrix4f result = identity();
		
		//How orthograpics works or projection
		
		result.elements[0 + 0 * 4] = 2.0f / (right - left);
		
		result.elements[1 + 1 * 4] = 2.0f / (top - bottom);
		
		result.elements[2 + 2 * 4] = 2.0f / (near - far);
		
		result.elements[0 + 3 * 4] = (left + right) / (left - right);
		
		result.elements[1 + 3 * 4] = (bottom + top) / (bottom - top);
		
		result.elements[2 + 3 * 4] = (near + far) / (far - near);
		
		return result;
		
		//http://www.songho.ca/opengl/gl_projectionmatrix.html
	}
	
	
	// Multiplies Matrixes
	public Matrix4f multiply(Matrix4f matrix) {
		Matrix4f result = new Matrix4f();
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				float sum = 0.0f;
				for (int e = 0; e < 4; e++) {
					sum += this.elements[x + e * 4] * matrix.elements[e + y * 4]; 
				}			
				result.elements[x + y * 4] = sum;
			}
		}
		return result;
	}
		
		//Translating Function
		public static Matrix4f translate(Vector3f vector){
			Matrix4f result = identity();
			
			result.elements[0 + 3 * 4] = vector.x;
			result.elements[1 + 3 * 4] = vector.y;
			result.elements[2 + 3 * 4] = vector.z;
			
			/*Basically the vector is the degree or input of translation
			 *How objects translate in programming is simple addition of matrixes
			 *The elements 13, 14, 15 ^elements[0 + 3 * 4]elements[1 + 3 * 4]elements[2 + 3 * 4]^ are really x, y , z
			 *The vector tells how much the object moves on the x, the y, and the z
			*/
			return result;
		}
		
		//Rotating Function
		public static Matrix4f rotate(float angle) {
			//Only can rotate around the x
			Matrix4f result = identity();
			float r = (float) toRadians(angle);
			float cos = (float) cos(r);
			float sin = (float) sin(r);
			
			result.elements[0 + 0 * 4] = cos;
			result.elements[1 + 1 * 4] = cos;
			result.elements[1 + 0 * 4] = sin;
			result.elements[0 + 1 * 4] = -sin;
			
			return result;
			
			/*TRIG
			 * for any given angle A
			 * X1 = Xx cos(a) + Yx - sin(A)
			 * Y1 = Xx sin(a) + Yx cos(A)
			 *///I Understand the matrix part... but not the trig.... which is the easier part
		}
		
		//SCALING 
		
		public FloatBuffer toFloatBuffer() {
			return BufferUtils.createFloatBuffer(elements);
		}

}
