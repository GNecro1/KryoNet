package net.main;

import java.util.Random;

import net.packets.PlayerAddPacket;
import net.packets.PlayerXUpdatePacket;
import net.packets.PlayerYUpdatePacket;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

public class Player extends Rectangle {
	private static final long serialVersionUID = 2485690671739263180L;
	float x, y, netX, netY;
	int id;
	String name;
	Timer send = new Timer(0.1,this);

	public Player(float x, float y, String name) {
		super(x, y, 40, 40);
		this.x = x;
		this.y = y;
		id = new Random().nextInt() >> 16;
		this.name = name;
		send.start();
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
				System.out.println("sent");
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
		if (gc.getInput().isKeyDown(Input.KEY_W)) {
			y -= 3;
		}
		if (gc.getInput().isKeyDown(Input.KEY_S)) {
			y += 3;
		}
		if (gc.getInput().isKeyDown(Input.KEY_D)) {
			x += 3;
		}
		if (gc.getInput().isKeyDown(Input.KEY_A)) {
			x -= 3;
		}
		setBounds(x, y, 40, 40);
	}

	public void render(Graphics g) {
		g.setColor(new Color(id));
		g.fill(this);
		g.setColor(Color.white);
		g.drawString(name, x - (name.length() * 2), y - (20));
	}
}
