package io.github.rudeyeti.necessity.modules.status;

import github.scarsz.discordsrv.dependencies.jda.api.EmbedBuilder;
import io.github.rudeyeti.necessity.Config;
import io.github.rudeyeti.necessity.Necessity;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Messages {
    protected static void initialize() {
        Status.statusChannel = Necessity.guild.getTextChannelById(Config.get.statusChannelId);

        if (Config.get.messageId.isEmpty()) {
            Status.statusChannel.sendMessage(serverOn().build()).queue((message) -> {
                try {
                    Path config = new File(Necessity.plugin.getDataFolder(), "config.yml").toPath();
                    String content = new String(Files.readAllBytes(config));
                    content = content.replaceAll("message-id: \"\"", "message-id: \"" + message.getId() + "\"");

                    Files.write(config, content.getBytes());
                    Necessity.plugin.reloadConfig();
                    Config.updateConfig();
                } catch (IOException error) {
                    error.printStackTrace();
                }
            });
        } else {
            Status.statusChannel.editMessageById(Config.get.messageId, serverOn().build()).queue();
        }
    }

    protected static EmbedBuilder serverOn() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        int onlinePlayers = Status.onlinePlayers.size();

        embedBuilder.setTitle(Config.get.serverAddress, null);
        embedBuilder.setColor(new Color(0x759965));

        embedBuilder.addField(
                "Status:",
                "Online",
                false
        );

        embedBuilder.addField(
                "Online Players:",
                onlinePlayers + "/" + Necessity.server.getMaxPlayers(),
                false
        );

        if (onlinePlayers > 0) {
            final String[] playerList = {""};

            Status.onlinePlayers.forEach((player) -> {
                playerList[0] += player + "\n";
            });

            // Discord has an embed field character limit of 1024.
            if (playerList[0].length() > 1024) {
                playerList[0] = playerList[0].substring(0, 1017);
                playerList[0] = playerList[0].substring(0, playerList[0].lastIndexOf("\n"));
                playerList[0] = playerList[0].concat("More...");
            }

            embedBuilder.addField(
                    "Player List:",
                    playerList[0],
                    false
            );
        }

        return embedBuilder;
    }

    protected static EmbedBuilder serverOff() {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle(Config.get.serverAddress, null);
        embedBuilder.setColor(new Color(0xBF5843));

        embedBuilder.addField(
                "Status:",
                "Offline",
                false
        );

        return embedBuilder;
    }
}
