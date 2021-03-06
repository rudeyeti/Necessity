package io.github.rudeyeti.necessity;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Guild;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Role;
import github.scarsz.discordsrv.dependencies.jda.api.requests.GatewayIntent;
import github.scarsz.discordsrv.dependencies.okhttp3.ResponseBody;
import github.scarsz.discordsrv.util.DiscordUtil;
import io.github.rudeyeti.necessity.listeners.DiscordSRVListener;
import io.github.rudeyeti.necessity.listeners.JDAListener;
import io.github.rudeyeti.necessity.modules.status.Status;
import io.github.rudeyeti.necessity.utils.Control;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Logger;

public final class Necessity extends JavaPlugin {

    public static Plugin plugin;
    public static Server server;
    public static Logger logger;
    public static Guild guild;
    public static Role builderRole;
    public static ResponseBody membersFirstPage;
    public static List<Member> initialBuildTeamMembersList;
    public static Member lastRoleChange;
    public static int lastPage;

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        plugin = getPlugin(this.getClass());
        server = plugin.getServer();
        logger = this.getLogger();
        Config.config = plugin.getConfig();
        plugin.saveDefaultConfig();

        Control.enable();
    }

    @Override
    public void onDisable() {
        try {
            if (Config.get.status) {
                Status.statusChannel.editMessageById(Config.get.messageId, Status.serverOff().build()).complete();
            }

            Control.disable(false);
        } catch (NullPointerException ignored) {}
    }
}
