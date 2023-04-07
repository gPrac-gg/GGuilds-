package org.radium.guildsplugin.commands.guild.SubCommands;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.enums.GuildRankType;
import org.radium.guildsplugin.manager.CommunicationManager;
import org.radium.guildsplugin.manager.LanguageManager;
import org.radium.guildsplugin.manager.object.member.GuildMember;
import org.radium.guildsplugin.util.TextHelper;

import java.util.concurrent.TimeUnit;

public class SetColorSubCommand {
    private final String subCommandSection = "Command.Guild.SubCommands.GuildSetColor.";

    public SetColorSubCommand(ProxiedPlayer player, String[] args) {
        if (args.length != 2) {
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "Usage"));
            return;
        }

        if (!Core.getInstance().getGuildMemberManager().isInGuild(player.getName())) {
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.NotInGuild"));
            return;
        }

        GuildMember guildMember = Core.getInstance().getGuildMemberManager().getGuildMember(player.getName());

        if (guildMember.getGuildRank() != GuildRankType.MASTER) {
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.NotTheGuildLeader"));
            return;
        }

        String newColor = args[1];

        if (newColor.length() != 2 || newColor.charAt(0) != '&') {
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "InvaildColor"));
            return;
        }

        if (!"0123456789AaBbCcDdEeFfRr".contains(newColor.substring(1))) {
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "InvaildColor"));
            return;
        }

        if (Core.getInstance().getGuildManager().getGuild(guildMember.getGuildId()).getSettings().getGuildColor().equals(newColor.replace("&", ""))) {
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "AlreadyTheSame"));
            return;
        }

        Core.getInstance().getGuildManager().setGuildColor(guildMember.getGuildId(), newColor.replace("&", ""));

        Core.getInstance().getGuildManager().sendPrefixedMessageToGuildMembers(
                guildMember.getGuildId(), LanguageManager.getMsg(subCommandSection + "ToGuildMessage")
                        .replace("$newColor", newColor)
        );
    }
}
