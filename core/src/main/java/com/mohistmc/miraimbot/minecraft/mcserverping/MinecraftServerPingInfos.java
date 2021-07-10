/**
 * Copyright Alwyn974 2019-2020
 * 
 * @author Developed By <a href="https://github.com/alwyn974"> Alwyn974</a>
 */

package com.mohistmc.miraimbot.minecraft.mcserverping;

import java.util.List;

/**
 * Informations of Minecraft Server
 * 
 * @author <a href="https://github.com/alwyn974"> Alwyn974 </a>
 * @since 1.0.0
 * @version 2.0.0
 */
public class MinecraftServerPingInfos {
	
	protected String description;
	private Players players;
	private Version version;
	private String favicon;
	private long latency;
	
	/**
	 * @return the MOTD
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @return a clean motd
	 */
	public String getStrippedDescription() {
		return MinecraftServerPingUtil.removeExtraCode(this.description);
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return {@link Player}
	 */
	public Players getPlayers() {
		return players;
	}

	/**
	 * @return {@link Version}
	 */
	public Version getVersion() {
		return version;
	}

	/**
	 * @return Base64 encoded favicon image
	 */
	public String getFavicon() {
		return favicon;
	}

	/**
	 * @return the latency
	 */
	public long getLatency() {
		return latency;
	}

	/**
	 * @param latency the latency to set
	 */
	public void setLatency(long latency) {
		this.latency = latency;
	}
	
	public class Players {
		private int max;
		private int online;
		private List<Player> sample;

		public int getMax() {
			return max;
		}

		public int getOnline() {
			return online;
		}

		public List<Player> getSample() {
			return sample;
		}
	}

	public class Player {
		private String name;
		private String id;

		public String getName() {
			return name;
		}

		public String getId() {
			return id;
		}
	}
	
	public class Version {
		private String name;
		private String protocol;

		public String getName() {
			return name;
		}

		public String getProtocol() {
			return protocol;
		}
	}
	
}
