/**
 * Copyright Alwyn974 2019-2020
 * 
 * @author Developed By <a href="https://github.com/alwyn974"> Alwyn974</a>
 */

package com.mohistmc.miraimbot.minecraft.mcserverping;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Utils functions for Minecraft Server Ping
 * 
 * @author <a href="https://github.com/alwyn974"> Alwyn974 </a>
 * @since 1.0.0
 * @version 2.0.0
 */
public class MinecraftServerPingUtil {
	
	public static byte PACKET_HANDSHAKE = 0x00, PACKET_STATUSREQUEST = 0x00, PACKET_PING = 0x01;
    public static int PROTOCOL_VERSION = 4;
    public static int STATUS_HANDSHAKE = 1;
    
    private static final Pattern STRIP_EXTRAS_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");
    
    public static String removeExtraCode(final String motd) {
    	return STRIP_EXTRAS_PATTERN.matcher(motd).replaceAll("");
    }
	
	public static void validate(final Object object, final String error) {
        if (object == null) {
            throw new RuntimeException(error);
        }
    }

    public static void io(final boolean condition, final String error) throws IOException {
        if (condition) {
            throw new IOException(error);
        }
    }

	public static int readVarInt(DataInputStream in) throws IOException {
		int i = 0;
		int j = 0;
		while (true) {
			int k = in.readByte();
			i |= (k & 0x7F) << j++ * 7;
			if (j > 5)
				throw new RuntimeException("VarInt too big");
			if ((k & 0x80) != 128)
				break;
		}
		return i;
	}

	public static void writeVarInt(DataOutputStream out, int paramInt) throws IOException {
		while (true) {
			if ((paramInt & 0xFFFFFF80) == 0) {
				out.writeByte(paramInt);
				return;
			}
			out.writeByte(paramInt & 0x7F | 0x80);
			paramInt >>>= 7;
		}
	}

}
