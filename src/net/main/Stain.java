package net.main;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class Stain {

	private float size;
	private float x;
	private float y;

	public Stain(float x, float y, float size, Game g) {
		this.setX(x);
		this.setY(y);
		this.setSize(size);
		g.stains.put(new Random().nextInt(), this);
	}

	public void tick(GameContainer gc) {

	}

	public void render(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(x, y, size, size);
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

}
