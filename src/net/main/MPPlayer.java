package net.main;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class MPPlayer extends Rectangle {
	private static final long serialVersionUID = 24856963180L;
	float x, y;
	final int id;
	String name;

	public MPPlayer(float x, float y, int id, String name) {
		super(x, y, 40, 40);
		this.x = x;
		this.y = y;
		this.id = id;
		this.name = name + "";
		System.out.println(name);
		setBounds(x, y, 40, 40);
	}

	public void tick(GameContainer gc) {
		setBounds(x,y,40,40);
	}

	public void render(Graphics g) {
		g.setColor(new Color(id));
		g.fill(this);
		g.setColor(Color.white);
		g.drawString(name, x - (name.length() * 2), y - (20));
	}

}
