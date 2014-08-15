package net.main;

import java.util.Random;

import net.packets.ProjectilePacket;
import net.packets.ProjectileRemovePacket;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class Projectile {

	private float y;
	private float x;
	private Vector2f v;
	private Shape s;
	private Timer t = new Timer(2);
	private Game g;
	public int id;

	public Projectile(float x, float y, Vector2f v, Game g) {
		this.g = g;
		this.setX(x);
		this.setY(y);
		this.setV(v);
		id = new Random().nextInt();
		setColl(new Rectangle(x + 5, y + 5, 10, 10));
		g.projectiles.add(this);
		t.start();
		if (g.isConnect()) {
			ProjectilePacket p = new ProjectilePacket();
			p.x = x;
			p.y = y;
			p.id = id;
			p.xVel = v.getX();
			p.yVel = v.getY();
			g.n.c.sendTCP(p);
		}
	}

	public boolean tick(GameContainer gc) {
		t.tick();
		x += v.getX();
		y += v.getY();
		getColl().setLocation(x, y);
		if (t.Ring()&& g.isConnect()) {
			ProjectileRemovePacket p = new ProjectileRemovePacket();
			p.id = id;
			g.n.c.sendTCP(p);
			return true;
		}
		return false;
	}

	public void remove() {
		g.projectiles.remove(this);
	}

	public void render(Graphics g) {
		g.setColor(Color.white);
		g.fill(getColl());
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

	public Shape getColl() {
		return s;
	}

	public void setColl(Shape s) {
		this.s = s;
	}

}
