package net.main;

import java.util.Random;

import net.packets.ProjectileRemovePacket;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class MPProjectile {

	private float y;
	private float x;
	private Vector2f v;
	private Shape s;
	private Game g;
	public int id;
	public MPProjectile(float x, float y, Vector2f v, Game g, int id) {
		this.g = g;
		this.setX(x);
		this.setY(y);
		this.setV(v);
		this.id = id;
		setS(new Rectangle(x + 5, y + 5, 10, 10));
		g.mpprojectiles.put(id,this);
	}

	public void tick(GameContainer gc) {
		x += v.getX();
		y += v.getY();
		getS().setLocation(x, y);
	}


	public void render(Graphics g) {
		g.setColor(Color.white);
		g.fill(getS());
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public Vector2f getV() {
		return v;
	}

	public void setV(Vector2f v) {
		this.v = v;
	}

	public Shape getS() {
		return s;
	}

	public void setS(Shape s) {
		this.s = s;
	}

	public void remove() {
		ProjectileRemovePacket p = new ProjectileRemovePacket();
		p.id = id;
		g.n.c.sendTCP(p);
		g.mpprojectiles.remove(this);
	}
}
