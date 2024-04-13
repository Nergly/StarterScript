package net.botwithus;

import net.botwithus.Skills.Divination;
import net.botwithus.internal.scripts.ScriptDefinition;
import net.botwithus.rs3.game.Client;
import net.botwithus.rs3.game.scene.entities.characters.player.LocalPlayer;
import net.botwithus.rs3.script.Execution;
import net.botwithus.rs3.script.LoopingScript;
import net.botwithus.rs3.script.config.ScriptConfig;


import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static net.botwithus.rs3.game.skills.Skills.DIVINATION;

public class SkeletonScript extends LoopingScript {
    public BotState botState = BotState.IDLE;
    private Random random = new Random();
    private SkeletonScriptGraphicsContext GraphicsContext;
    private Divination divinationSkill;

    /////////////////////////////////////Botstate//////////////////////////
    public enum BotState {
        //define your own states here
        IDLE,
        DIVINATIONSKILLING,
        DIVINATIONDEPOSIT,
        DIVINATIONTRAVERSE
        //...
    }

    public SkeletonScript(String s, ScriptConfig scriptConfig, ScriptDefinition scriptDefinition) {
        super(s, scriptConfig, scriptDefinition);
        this.sgc = new SkeletonScriptGraphicsContext(getConsole(), this);
        loadConfiguration(); // Load configuration when the script starts
    }

    @Override
    public void onLoop() {
        LocalPlayer player = Client.getLocalPlayer();
        if (player == null || Client.getGameState() != Client.GameState.LOGGED_IN) {
            Execution.delay(random.nextLong(3000, 7000));
            return;
        }

        switch (botState) {
            case IDLE -> {
                println("We're idle!");
                Execution.delay(random.nextLong(1000, 3000));
            }
            case DIVINATIONSKILLING -> {
                //do questing stuff
                Execution.delay(divinationSkill.handleSkilling(player, divinationSkill.wispState.name()));
            }
            }
        }

    ////////////////Save & Load Config/////////////////////
    void loadConfiguration() {
        try {
            String selectedWispTypeName = configuration.getProperty("selectedWispType");
            if (selectedWispTypeName != null && !selectedWispTypeName.isEmpty()) {
                // Convert the saved name back to a WispType enum
                Divination.WispType selectedWispType = Divination.WispType.valueOf(selectedWispTypeName);
                divinationSkill.setWispType(selectedWispType);
                println("WispType configuration loaded successfully: " + selectedWispType.name());
            }

            // Load and set the state of progressive mode
            String progressiveModeEnabledString = configuration.getProperty("progressiveModeEnabled");
            if (progressiveModeEnabledString != null && !progressiveModeEnabledString.isEmpty()) {
                GraphicsContext.progressiveModeEnabled = Boolean.parseBoolean(progressiveModeEnabledString);
                println("Progressive mode configuration loaded successfully: " + progressiveModeEnabledString);
            }

        } catch (Exception e) {
            println("Error loading configuration: \n" + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            println("This is a non-fatal error, you can ignore it.");
        }
    }

    void saveConfiguration() {
        try {
            // Save the selected WispType using its name
            configuration.addProperty("selectedWispType", divinationSkill.getCurrentWispType().name());
            configuration.addProperty("progressiveModeEnabled", String.valueOf(GraphicsContext.progressiveModeEnabled));
            configuration.save();
            println("WispType configuration saved successfully.");
        } catch (Exception e) {
            println("Error saving configuration: \n" + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            println("This is a non-fatal error, you can ignore it.");
        }
    }

    ////////////////////Botstate/////////////////////
    public BotState getBotState() {
        return botState;
    }

    public void setBotState(BotState botState) {
        this.botState = botState;
    }
}

