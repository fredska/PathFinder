package com.fska.pf.pathing;

public class Vector2_Int {
	public int x,y;
	
	public Vector2_Int(){
		this(0,0);
	}
	
	public Vector2_Int(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Vector2_Int copy(){
		return new Vector2_Int(x,y);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector2_Int other = (Vector2_Int) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
}
