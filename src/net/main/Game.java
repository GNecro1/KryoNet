package net.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.packets.PlayerAddPacket;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Game extends BasicGame {

	Network n;
	private int port;
	private String ip;
	Player p;
	private String name;
	private boolean connect;
	public Map<Integer, MPPlayer> players = new HashMap<Integer, MPPlayer>();
	public Map<Integer, Stain> stains = new HashMap<Integer, Stain>();
	public ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	public Map<Integer, MPProjectile> mpprojectiles = new HashMap<Integer, MPProjectile>();

	public Game(String title, String ip, int port, String name, boolean connect) {
		super(title);
		this.ip = ip;
		this.port = port;
		this.name = name;
		this.setConnect(connect);
		new Stain(-30, -30, 400, this);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		n = new Network(ip, port, this);
		p = new Player(gc.getWidth() - (gc.getWidth() / 2) - 20, gc.getHeight() - (gc.getHeight() / 2) - 20, name, this);
		if (isConnect()) {
			n.start();
			PlayerAddPacket pap = p.getAddPacket();
			n.c.sendTCP(pap);
		}
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		p.tick(gc);
		if (isConnect()) {
			p.sendX(n);
			p.sendY(n);
			p.sendHealth(n);
			n.sendKeepAlive(1000);
		}
		for (MPPlayer mpp : players.values()) {
			mpp.tick(gc);
		}
		for (Stain s : stains.values()) {
			s.tick(gc);
		}
		for (MPProjectile p : mpprojectiles.values()) {
			p.tick(gc);
		}
		Iterator<Projectile> it = projectiles.iterator();
		while (it.hasNext()) {
			Projectile proj = it.next();
			if (proj.tick(gc)) {
				it.remove();
			}
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		g.setColor(Color.pink);
		g.translate(-p.getXOffset(), -p.getYOffset());
		for (MPPlayer mpp : players.values()) {
			mpp.render(g);
		}
		for (Stain s : stains.values()) {
			s.render(g);
		}
		for (Projectile p : projectiles) {
			p.render(g);
		}
		for (MPProjectile p : mpprojectiles.values()) {
			p.render(g);
		}
		String[] i = n.sm.split("\n");
		g.drawString(n.sm, 5, gc.getHeight() - (i.length * 20));
		p.render(g);
	}

	public static void main(String[] args) throws SlickException {
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		String ip = JOptionPane.showInputDialog(null, "Insert the ip, the port is allways 4321!");
		boolean connect = true;
		String name = null;
		if (ip.equalsIgnoreCase("noc") || ip.equalsIgnoreCase("") || ip.equalsIgnoreCase(" ")) {
			connect = false;
			name = System.getProperty("user.name");
		}
		if (name == null) {
			name = JOptionPane.showInputDialog(null, "Your name,nickname.....");
		}
		System.out.println("Status : " + name + " is connecting to " + ip + ":4321");
		AppGameContainer agc = new AppGameContainer(new Game("Game MP", ip, 4321, name, connect));
		agc.setShowFPS(true);
		agc.setAlwaysRender(true);
		agc.setTargetFrameRate(60);
		if (JOptionPane.showConfirmDialog(null, "Fullscreen?", "Fullscreen?", JOptionPane.YES_NO_OPTION) == 0) {
			agc.setDisplayMode(1024, 768, true);
		} else {
			agc.setDisplayMode(1024, 768, false);
		}
		agc.start();
	}

	public boolean isConnect() {
		return connect;
	}

	public void setConnect(boolean connect) {
		this.connect = connect;
	}
}
