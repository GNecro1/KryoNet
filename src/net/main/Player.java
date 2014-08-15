package net.main;

import java.util.Random;

import javax.swing.JOptionPane;

import net.packets.DiedCountPacket;
import net.packets.PlayerAddPacket;
import net.packets.PlayerHealthPacket;
import net.packets.PlayerXUpdatePacket;
import net.packets.PlayerYUpdatePacket;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

public class Player extends Rectangle {
	private static final long serialVersionUID = 2485690671739263180L;
	float x, y, netX, netY, xOff, yOff;
	int id;
	String name;
	Timer send = new Timer(0.1);
	Timer q = new Timer(2);
	private float x2;
	private float y2;
	private Game g;
	public int health, netHealth, died = 0;

	public Player(float x, float y, String name, Game g) {
		super(x, y, 40, 40);
		x2 = x;
		this.x = x;
		y2 = y;
		health = 100;
		netHealth = 100;
		this.y = y;
		this.xOff = x;
		this.yOff = y;
		this.g = g;
		id = new Random().nextInt() >> 16;
		this.name = name;
		send.start();
		q.start();
	}

	public float getXOffset() {
		return xOff - x2;
	}

	public float getYOffset() {
		return yOff - y2;
	}

	public void sendX(Network n) {
		if (send.Ring()) {
			if (netX != x) {
				netX = x;
				PlayerXUpdatePacket p = new PlayerXUpdatePacket();
				p.x = netX;
				p.id = id;
				n.c.sendTCP(p);
			}
		}
	}

	public void sendY(Network n) {
		if (send.Ring()) {
			if (netY != y) {
				netY = y;
				PlayerYUpdatePacket p = new PlayerYUpdatePacket();
				p.y = netY;
				p.id = id;
				n.c.sendTCP(p);
			}
		}
	}

	public void sendHealth(Network n) {
		if (send.Ring()) {
			if (netHealth != health) {
				netHealth = health;
				PlayerHealthPacket p = new PlayerHealthPacket();
				p.health = health;
				p.id = id;
				p.died = died;
				n.c.sendTCP(p);
			}
		}
	}

	public PlayerAddPacket getAddPacket() {
		PlayerAddPacket p = new PlayerAddPacket();
		p.x = x;
		p.y = y;
		p.id = id;
		p.name = name;
		return p;
	}

	public void tick(GameContainer gc) {
		send.tick();
		q.tick();
		if (gc.getInput().isKeyDown(Input.KEY_W)) {
			y -= 3;
			yOff -= 3;
		}
		if (gc.getInput().isKeyDown(Input.KEY_S)) {
			y += 3;
			yOff += 3;
		}
		if (gc.getInput().isKeyDown(Input.KEY_D)) {
			x += 3;
			xOff += 3;
		}
		if (gc.getInput().isKeyDown(Input.KEY_A)) {
			x -= 3;
			xOff -= 3;
		}
		if (gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && q.Ring()) {
			q.reset();
			Vector2f v = new Vector2f();
			float targetX = gc.getInput().getAbsoluteMouseX() + getXOffset() - 20, targetY = gc.getInput().getAbsoluteMouseY() + getYOffset() - 20;
			float rot = angle(new Vector2f(x, y), new Vector2f((float) targetX, (float) targetY));
			v = point(new Vector2f(0, 0), new Vector2f(0, -4), rot);
			new Projectile(x + 20, y + 20, v, g);

		}
		for (MPProjectile mpp : g.mpprojectiles.values()) {
			if (this.intersects(mpp.getS())) {
				health -= 10;
				g.mpprojectiles.remove(mpp.id);
			}
		}
		if (health < 1) {
			int i = JOptionPane.showConfirmDialog(null, "Respawn?", "Respawn?", JOptionPane.YES_NO_OPTION);
			if (i == 0) {
				health = 100;
				x = x2;
				y = y2;
				xOff = x;
				yOff = y;
				died++;
				DiedCountPacket d = new DiedCountPacket();
				d.died = died;
				d.id = id;
				g.n.c.sendTCP(d);
			} else {
				System.exit(0);
			}
		}
		setBounds(x, y, 40, 40);
	}

	public Vector2f point(Vector2f pivot, Vector2f point, float rotation) {

		float rot = (float) (1f / 180 * rotation * Math.PI);

		float x = point.x - pivot.x;
		float y = point.y - pivot.y;

		float newx = (float) (x * Math.cos(rot) - y * Math.sin(rot));
		float newy = (float) (x * Math.sin(rot) + y * Math.cos(rot));

		newx += pivot.x;
		newy += pivot.y;

		return new Vector2f(newx, newy);
	}

	public int angle(Vector2f pivot, Vector2f point) {

		float xdiff = pivot.x - point.x;
		float ydiff = pivot.y - point.y;

		float angle = (float) ((Math.atan2(xdiff, ydiff)) * 180 / Math.PI);

		return -(int) angle;
	}

	public void render(Graphics g) {
		g.setColor(new Color(id));
		g.fill(this);
		g.setColor(new Color(Color.white.getGreen() / 3 + (id / 3) - Color.white.getRed() / 3 + (id / 3) + Color.white.getBlue() / 3 + (id / 3)));
		g.drawString(name + " " + health + " Died " + died + " times!", x - ((name.length() + 19) * 2), y - (20));
	}
}
