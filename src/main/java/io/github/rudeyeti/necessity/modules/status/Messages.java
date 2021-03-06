package io.github.rudeyeti.necessity.modules.status;

import github.scarsz.discordsrv.dependencies.jda.api.EmbedBuilder;
import io.github.rudeyeti.necessity.Config;
import io.github.rudeyeti.necessity.Necessity;

import java.awt.*;

public class Messages {
    protected static void initialize() {
        Status.statusChannel = Necessity.guild.getTextChannelById(Config.get.statusChannelId);

        if (Config.get.messageId.isEmpty()) {
            String messageId = Status.statusChannel.sendMessage(serverOn(true).build()).complete().getId();
            Config.setValue("message-id", "", messageId);
            Config.updateConfig();
        } else {
            Status.statusChannel.editMessageById(Config.get.messageId, serverOn(true).build()).queue();
        }
    }

    protected static EmbedBuilder serverOn(boolean includeStatus) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        int onlinePlayers = Status.onlinePlayers.size();

        embedBuilder.setTitle(Config.get.serverAddress, null);
        embedBuilder.setColor(new Color(0x759965));

        if (includeStatus) {
            embedBuilder.addField(
                    "Status:",
                    "Online",
                    false
            );
        }

        embedBuilder.addField(
                "Online Players:",
                onlinePlayers + "/" + Necessity.server.getMaxPlayers(),
                false
        );

        if (onlinePlayers > 0) {
            final StringBuilder[] playerList = {new StringBuilder()};

            Status.onlinePlayers.forEach((player) -> {
                playerList[0].append(player).append("\n");
            });

            // Discord has an embed field character limit of 1024.
            if (playerList[0].length() > 1024) {
                int length = playerList[0].length();

                playerList[0].delete(1017, length);
                playerList[0].delete(playerList[0].lastIndexOf("\n"), length);
                playerList[0].append("\nMore...");
            }

            embedBuilder.addField(
                    "Player List:",
                    playerList[0].toString(),
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
