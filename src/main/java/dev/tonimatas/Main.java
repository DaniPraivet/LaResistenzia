package dev.tonimatas;

import dev.tonimatas.config.Configs;
import dev.tonimatas.listeners.CountListener;
import dev.tonimatas.listeners.MemberListener;
import dev.tonimatas.listeners.SlashCommandListener;
import dev.tonimatas.roulette.RouletteSlashCommandsListener;
import dev.tonimatas.schedules.RouletteManager;
import dev.tonimatas.schedules.TemporalChannels;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Main {
    public static boolean STOP = false;
    public static Logger LOGGER = LoggerFactory.getLogger(Main.class);
    public static JDA JDA;
    
    public static void main(String[] args) {
        Configs.init();

        String token = Configs.BOT.getValue("token").get();
        
        if (token.isEmpty()) return;
        
        JDA = JDABuilder.createDefault(token)
                .enableIntents(Arrays.stream(GatewayIntent.values()).toList())
                .addEventListeners(new CountListener(), new SlashCommandListener(), new MemberListener(), new RouletteSlashCommandsListener())
                .setAutoReconnect(true)
                .build();
        
        JDA.updateCommands().addCommands(
                Commands.slash("ping", "Discord Ping! Pong!")
        ).queue();

        CountListener.init();
        RouletteManager.init();
        TemporalChannels.init();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        LOGGER.info("Done!");
    }
}