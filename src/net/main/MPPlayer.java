package net.main;

import java.util.Iterator;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class MPPlayer extends Rectangle {
	private static final long serialVersionUID = 24856963180L;
	float x, y;
	final int id;
	String name;
	public int health;
	private Game g;
	public int died;

	public MPPlayer(float x, float y, int id, String name,Game g) {
		super(x, y, 40, 40);
		this.x = x;
		this.y = y;
		this.g = g;
		health = 100;
		this.id = id;
		this.name = name + "";
		System.out.println(name);
		setBounds(x, y, 40, 40);
	}

	public void tick(GameContainer gc) {
		setBounds(x,y,40,40);
		Iterator<Projectile> it = g.projectiles.iterator();
		while(it.hasNext()){
			Projectile proj = it.next();
			if(this.intersects(proj.getColl())){
				it.remove();
			}
		}
	}

	public void render(Graphics g) {
		g.setColor(new Color(id));
		g.fill(this);
		g.setColor(new Color(Color.white.getGreen() / 3 + (id / 3) - Color.white.getRed() / 3 + (id / 3) + Color.white.getBlue() / 3 + (id / 3)));
		g.drawString(name + " " + health +" Died " + died+" times!", x - ((name.length() + 19) * 2), y - (20));
	}

}
