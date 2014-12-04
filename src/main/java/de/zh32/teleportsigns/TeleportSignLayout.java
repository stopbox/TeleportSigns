package de.zh32.teleportsigns;

import de.zh32.teleportsigns.ping.ServerInfo;
import lombok.Data;
import org.bukkit.ChatColor;

/**
 *
 * @author zh32
 */
@Data
class TeleportSignLayout implements SignLayout {
    private final String name;    
    private final String online;
    private final String offline;
    private final String[] layout;
    private final boolean teleport;
    private final String offlineInteger;
    private int timeout = 0;

    @Override
    public String[] parseLayout(ServerInfo sinfo) {
        String[] laa = new String[layout.length];
        int motdCount = 0;
        String tempMotd = sinfo.getMotd() == null ? "" : sinfo.getMotd();
        String[] splitMotd = tempMotd.split("(?<=\\G.{15})");
        for (int i = 0; i < layout.length; i++) {
            String line = layout[i];
            line = line.replace("%displayname%", sinfo.getDisplayname());
            if (sinfo.isOnline()) {
                timeout = 0;
                line = line.replace("%isonline%", online);
                line = line.replace("%numpl%", String.valueOf(sinfo.getPlayersOnline()));
                line = line.replace("%maxpl%", String.valueOf(sinfo.getMaxPlayers()));
                if (line.contains("%motd%")) {
                    if (motdCount < splitMotd.length) {
                        String motd = splitMotd[motdCount];
                        if (motd != null) {
                            line = line.replace("%motd%", motd);
                        }
                        motdCount++;
                    } else {
                        line = line.replace("%motd%", "");
                    }
                }
            }
            else {
		if (timeout != 20) {
			timeout++;
			sign.setLine(0, ChatColor.DARK_RED + "█████████");
			sign.setLine(1, ChatColor.translateAlternateColorCodes('&', name));
			sign.setLine(2, ChatColor.RED + "Rebooting");
			sign.setLine(3, ChatColor.DARK_RED + "█████████");
		} else if (timeout == 20) {
			sign.setLine(0, ChatColor.DARK_RED + "█████████");
			sign.setLine(1, ChatColor.translateAlternateColorCodes('&', name));
			sign.setLine(2, ChatColor.RED + "Maintenance");
			sign.setLine(3, ChatColor.DARK_RED + "█████████");
		}
            }
            laa[i] = ChatColor.translateAlternateColorCodes('&', line);
        }
        return laa;
    }
}
