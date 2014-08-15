package net.main;

import java.io.IOException;

import javax.swing.JOptionPane;

import net.packets.DiedCountPacket;
import net.packets.MessagePacket;
import net.packets.PlayerAddPacket;
import net.packets.PlayerHealthPacket;
import net.packets.PlayerRemovePacket;
import net.packets.PlayerXUpdatePacket;
import net.packets.PlayerYUpdatePacket;
import net.packets.ProjectilePacket;
import net.packets.ProjectileRemovePacket;

import org.newdawn.slick.geom.Vector2f;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class Network extends Listener {

	private String ip;
	private int port;
	public Client c;
	private Game game;
	public String sm = "-";
	public boolean showChat;

	public Network(String ip, int port, Game game) {
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
		kryo.register(MessagePacket.class);
		kryo.register(ProjectilePacket.class);
		kryo.register(ProjectileRemovePacket.class);
		kryo.register(PlayerHealthPacket.class);
		kryo.register(DiedCountPacket.class);
		c.start();
		try {
			c.connect(10000, ip, port);
		} catch (IOException e) {
			reconect();
		} finally {
			System.out.println("Status : " + c.isConnected());
			c.setTimeout(120000);
		}
	}

	public void sendKeepAlive(int time) {
		c.setKeepAliveTCP(time * 1000);
	}

	private void reconect() {
		try {
			c.reconnect();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Cannot connect to : " + ip + ":" + port + " will exit!");
			System.exit(0);
		}
	}

	public void received(Connection c, Object o) {
		if (o instanceof PlayerAddPacket) {
			PlayerAddPacket p = (PlayerAddPacket) o;
			MPPlayer mmp = new MPPlayer(p.x, p.y, p.id, p.name, game);
			mmp.died = 0;
			game.players.put(p.id, mmp);
		} else if (o instanceof PlayerXUpdatePacket) {
			PlayerXUpdatePacket p = (PlayerXUpdatePacket) o;
			game.players.get(p.id).x = p.x;
		} else if (o instanceof PlayerYUpdatePacket) {
			PlayerYUpdatePacket p = (PlayerYUpdatePacket) o;
			game.players.get(p.id).y = p.y;
		} else if (o instanceof PlayerRemovePacket) {
			PlayerRemovePacket p = (PlayerRemovePacket) o;
			game.players.remove(p.id);
		} else if (o instanceof MessagePacket) {
			MessagePacket p = (MessagePacket) o;
			if (p.id == -1) {
				sm += "\n " + p.message;
			}
		} else if (o instanceof ProjectilePacket) {
			ProjectilePacket p = (ProjectilePacket) o;
			Vector2f v = new Vector2f(p.xVel, p.yVel);
			MPProjectile proj = new MPProjectile(p.x, p.y, v, game,p.id);
		}else if(o instanceof ProjectileRemovePacket){			
			ProjectileRemovePacket p = (ProjectileRemovePacket) o;
			game.mpprojectiles.remove(p.id);
		}else if(o instanceof PlayerHealthPacket){
			PlayerHealthPacket p = (PlayerHealthPacket) o;
			game.players.get(p.id).health = p.health;	
		}else if(o instanceof DiedCountPacket){
			DiedCountPacket d = (DiedCountPacket) o;
			game.players.get(d.id).died = d.died;
		}
		c.setTimeout(120000);
		c.setKeepAliveTCP(120000);
	}
}
