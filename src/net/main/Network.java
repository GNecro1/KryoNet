package net.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import net.packets.PlayerAddPacket;
import net.packets.PlayerRemovePacket;
import net.packets.PlayerXUpdatePacket;
import net.packets.PlayerYUpdatePacket;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class Network extends Listener {

	private String ip;
	private int port;
	public Client c;
	private Game game;

	public Network(String ip, int port,Game game) {
		this.port = port;
		this.ip = ip;
		this.game = game;
	}

	public void start() {
		c = new Client();
		c.addListener(this);
		Kryo kryo = c.getKryo();
		kryo.register(PlayerAddPacket.class);
		kryo.register(PlayerRemovePacket.class);
		kryo.register(PlayerXUpdatePacket.class);
		kryo.register(PlayerYUpdatePacket.class);
		c.start();
		try {
			c.connect(10000, ip, port);
		} catch (IOException e) {
			e.printStackTrace();
			reconect();
		} finally {
			System.out.println("Status : " + c.isConnected());
		}
	}
	
	public void sendKeepAlive(int time){
		c.setKeepAliveTCP(time*1000);
	}

	private void reconect() {
		try {
			c.reconnect();
		} catch (IOException e) {
			e.printStackTrace();
			String i = JOptionPane.showInputDialog(null, "Cannot connect to : " + ip + port + " will exit!", 1);
			System.exit(0);
		}
	}

	public void received(Connection c, Object o) {
		if (o instanceof PlayerAddPacket) {
			PlayerAddPacket p = (PlayerAddPacket) o;
			MPPlayer mmp = new MPPlayer(p.x, p.y, p.id, p.name);

			game.players.put(p.id, mmp);
		}
		if (o instanceof PlayerXUpdatePacket) {
			PlayerXUpdatePacket p = (PlayerXUpdatePacket) o;
			game.players.get(p.id).x = p.x;
		}
		if (o instanceof PlayerYUpdatePacket) {
			PlayerYUpdatePacket p = (PlayerYUpdatePacket) o;
			game.players.get(p.id).y = p.y;
		}
		if (o instanceof PlayerRemovePacket) {
			PlayerRemovePacket p = (PlayerRemovePacket) o;
			game.players.remove(p.id);
		}
		c.setKeepAliveTCP(120000);
	}
}
