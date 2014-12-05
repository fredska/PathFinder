package com.fska.pf.pathing;

public class Vertex extends Vector2_Int {
	
	private double dist;
	private Vertex prev;
	
	public Vertex(){
		super();
	}
	
	public Vertex(int x, int y, double dist, Vertex previous){
		super(x,y);
		this.dist = dist;
		this.prev = previous;
	}

	public double getDist() {
		return dist;
	}

	public void setDist(double dist) {
		this.dist = dist;
	}

	public Vertex getPrev() {
		return prev;
	}

	public void setPrev(Vertex prev) {
		this.prev = prev;
	}
	
	public Vector2_Int getVector2_Int(){
		return new Vector2_Int(x,y);
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
