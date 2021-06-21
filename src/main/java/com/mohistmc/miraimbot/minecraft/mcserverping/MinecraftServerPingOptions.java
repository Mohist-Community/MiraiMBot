/**
 * Copyright Alwyn974 2019-2020
 * 
 * @author Developed By <a href="https://github.com/alwyn974"> Alwyn974</a>
 */

package com.mohistmc.miraimbot.minecraft.mcserverping;

/**
 * Some options for Minecraft Server Ping
 * 
 * @author <a href="https://github.com/alwyn974"> Alwyn974 </a>
 * @since 1.0.0
 * @version 1.0.0
 */
public class MinecraftServerPingOptions {

	private String hostname;
	private int port = 25565;
	private int timeout = 2000;
	private String charset = "UTF-8";

	/**
	 * @param hostname the hostname to set
	 * @return this
	 */
	public MinecraftServerPingOptions setHostname(String hostname) {
		this.hostname = hostname;
		return this;
	}

	/**
	 * @param port the port to set
	 * @return this
	 */
	public MinecraftServerPingOptions setPort(int port) {
		this.port = port;
		return this;
	}

	/**
	 * @param timeout the timeout to set
	 * @return this
	 */
	public MinecraftServerPingOptions setTimeout(int timeout) {
		this.timeout = timeout;
		return this;
	}

	/**
	 * @param charset the charset to set
	 * @return this
	 */
	public MinecraftServerPingOptions setCharset(String charset) {
		this.charset = charset;
		return this;
	}
	
	/**
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * @return the port <i>default : 25565</i>
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @return the timeout <i>default : 2000</i>
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * @return the charset <i>default : UTF-8</i>
	 */
	public String getCharset() {
		return charset;
	}

}
