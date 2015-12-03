package liquid.engine;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class Drain {
	private World myWorld;
	private PolygonShape myShape;
	private Transform t;
	
	public Drain(World theWorld, PolygonShape shape, float x, float y){
		myWorld = theWorld;
		myShape = shape;
		t = new Transform();
	}
	
	public void update(){
		myWorld.destroyParticlesInShape(myShape, new Transform());
	}
	
}
