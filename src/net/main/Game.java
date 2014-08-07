package net.main;
import com.esotericsoftware.minlog.Log;
import com.sun.java.swing.plaf.motif.*;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.packets.PlayerAddPacket;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
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

	public Game(String title, String ip, int port, String name, boolean connect) {
		super(title);
		this.ip = ip;
		this.port = port;
		this.name = name;
		this.connect = connect;
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		n = new Network(ip, port,this);
		p = new Player(40, 100, name);
		if (connect) {
			n.start();
			PlayerAddPacket pap = p.getAddPacket();
			n.c.sendTCP(pap);
		}
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		p.tick(gc);
		if (connect) {
			p.sendX(n);
			p.sendY(n);
			n.sendKeepAlive(1000);
		}
		for (MPPlayer mpp : players.values()) {
			mpp.tick(gc);
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		p.render(g);
		for (MPPlayer mpp : players.values()) {
			mpp.render(g);
		}

	}

	public static void main(String[] args) throws SlickException {
		String ip = JOptionPane.showInputDialog(null, "Insert the ip, the port is allways 4321!");
		String name = JOptionPane.showInputDialog(null, "Your name,nickname.....");
		boolean connect = true;
		if (ip.equalsIgnoreCase("noc"))
			connect = false;

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
		try {
			 UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.TRACE();
		agc.start();
	}
}
