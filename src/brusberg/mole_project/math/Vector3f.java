package brusberg.mole_project.math;
//
//Contains all vector operations and basically is an array of points
//Vector represents a coordinate, 3 points, floats
public class Vector3f {

	public float x, y, z;

	public Vector3f() {
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
	}

	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

}
