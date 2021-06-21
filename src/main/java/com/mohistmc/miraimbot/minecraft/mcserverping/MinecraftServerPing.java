/**
 * Copyright Alwyn974 2019-2020
 * 
 * @author Developed By <a href="https://github.com/alwyn974"> Alwyn974</a>
 */

package com.mohistmc.miraimbot.minecraft.mcserverping;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.InitialDirContext;

/**
 * The main class of the Minecraft Server Ping !
 * 
 * @author <a href="https://github.com/alwyn974"> Alwyn974 </a>
 * @since 1.0.0
 * @version 2.0.0
 */
public class MinecraftServerPing {
	
	public MinecraftServerPingInfos getPing(final String hostname) throws IOException {
		return this.getPing(new MinecraftServerPingOptions().setHostname(hostname));
	}
		
	public MinecraftServerPingInfos getPing(final MinecraftServerPingOptions options) throws IOException  {
		MinecraftServerPingUtil.validate(options.getHostname(), "Hostname cannot be null");

        final Socket socket = new Socket();

        try {
            final Attribute host = new InitialDirContext().getAttributes("dns:/_Minecraft._tcp." + options.getHostname(), new String[] { "SRV" }).get("SRV");
            if (host != null) {
                final String[] domain = host.toString().split(" ");
                final String newip = domain[domain.length - 1].substring(0, domain[domain.length - 1].length() - 1);
                final int newport = Integer.parseInt(domain[domain.length - 2]);
                options.setHostname(newip);
                options.setPort(newport);
            }
        }
        catch (NamingException ex3) {}
        catch (NullPointerException ex1) {
            return null;
        }

		socket.connect(new InetSocketAddress(options.getHostname(), options.getPort()), options.getTimeout());
		final long startTime = System.currentTimeMillis();
		final DataInputStream in = new DataInputStream(socket.getInputStream());
        final DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        
        //-> Handshake
        
        ByteArrayOutputStream handshake_bytes = new ByteArrayOutputStream();
        DataOutputStream handshake = new DataOutputStream(handshake_bytes);
        
        handshake.writeByte(MinecraftServerPingUtil.PACKET_HANDSHAKE);
        MinecraftServerPingUtil.writeVarInt(handshake, MinecraftServerPingUtil.PROTOCOL_VERSION);
        MinecraftServerPingUtil.writeVarInt(handshake, options.getHostname().length());
        handshake.writeBytes(options.getHostname());
        handshake.writeShort(options.getPort());
        MinecraftServerPingUtil.writeVarInt(handshake, MinecraftServerPingUtil.STATUS_HANDSHAKE);
		
        MinecraftServerPingUtil.writeVarInt(out, handshake_bytes.size());
        out.write(handshake_bytes.toByteArray());
        
        //-> Status request

        out.writeByte(0x01); // Size of packet
        out.writeByte(MinecraftServerPingUtil.PACKET_STATUSREQUEST);
        
        //<- Status response
        
        MinecraftServerPingUtil.readVarInt(in); // Size
        int id = MinecraftServerPingUtil.readVarInt(in);
        
        MinecraftServerPingUtil.io(id == -1, "Server prematurely ended stream.");
        MinecraftServerPingUtil.io(id != MinecraftServerPingUtil.PACKET_STATUSREQUEST, "Server returned invalid packet id.");

        int length = MinecraftServerPingUtil.readVarInt(in);
        MinecraftServerPingUtil.io(length == -1, "Server prematurely ended stream.");
        MinecraftServerPingUtil.io(length == 0, "Server returned unexpected value.");

        byte[] data = new byte[length];
        in.readFully(data);
        String json = new String(data, options.getCharset());
        
        //-> Ping
        long now = System.currentTimeMillis();
        out.writeByte(0x09); // Size of packet
        out.writeByte(MinecraftServerPingUtil.PACKET_PING);
        out.writeLong(now);

        //<- Ping
        
        MinecraftServerPingUtil.readVarInt(in); // Size
        id = MinecraftServerPingUtil.readVarInt(in);
        MinecraftServerPingUtil.io(id == -1, "Server prematurely ended stream.");
        MinecraftServerPingUtil.io(id != MinecraftServerPingUtil.PACKET_PING, "Server returned invalid packet id.");
        
        GsonBuilder gsonBuilder = new GsonBuilder();
        String motd = null;
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
		if (jsonObject.get("description").isJsonObject()) {
			JsonObject desc = jsonObject.get("description").getAsJsonObject();
			gsonBuilder.excludeFieldsWithModifiers(Modifier.PROTECTED);
			
			StringBuilder builder = new StringBuilder();
			
			if (desc.has("extra")) {
				MotdExtra extras = gsonBuilder.create().fromJson(desc, MotdExtra.class);
				extras.getExtra().forEach(e -> {
					if (e.getColor() != null) builder.append(MotdExtra.Enum.valueOf(e.getColor().toUpperCase()).getExtraCode());
					if (e.isBold()) builder.append(MotdExtra.Enum.BOLD.getExtraCode());
					if (e.isItalic()) builder.append(MotdExtra.Enum.ITALIC.getExtraCode());
					if (e.isObfuscated()) builder.append(MotdExtra.Enum.OBFUSCATED.getExtraCode());
					if (e.isStrikethrough()) builder.append(MotdExtra.Enum.STRIKETHROUGH.getExtraCode());
					if (e.isUnderline()) builder.append(MotdExtra.Enum.UNDERLINE.getExtraCode());
					builder.append(e.getText());
				});
				String text = desc.get("text").getAsString();
				builder.append(text.isEmpty() ? "" : text);
			} else 
				builder.append(desc.get("text").getAsString());
			motd = builder.toString();
		} 
		
        MinecraftServerPingInfos infos = gsonBuilder.create().fromJson(json, MinecraftServerPingInfos.class);
        infos.setLatency(now - startTime);
        if (motd != null) infos.setDescription(motd);
        
        //<- Close

        handshake.close();
        handshake_bytes.close();
        out.close();
        in.close();
        socket.close();
        
        return infos;
	}

}
