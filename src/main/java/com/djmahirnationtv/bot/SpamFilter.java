package com.djmahirnationtv.bot;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Map;
import java.util.HashMap;

public class SpamFilter extends ListenerAdapter {
    private boolean spamEnabled = true;
    private final Map<String, Integer> messageCounter = new HashMap<>();
    private final Map<String, Long> lastMessageTime = new HashMap<>();
    private final int maxAllowedMSGsWithinSeconds = 5;
    private final long MSGInterval = 10000; // 10 Seconds interval of Messages

    public void setSpamEnabled(boolean spamEnabled) {
        this.spamEnabled = spamEnabled; // Enabled Spam by Default
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return; // Will Ignore the Bot Messages
        }
        if (!spamEnabled) {
            return; // Will Ignore the Spam Filter if its disabled
        }
        String userID = event.getAuthor().getId(); // Gets Author ID (User ID basically)
        String message = event.getMessage().getContentRaw(); // Gets Message in RAW format

        long currenttime = System.currentTimeMillis(); // It will get milliseconds from X Time
        if (lastMessageTime.containsKey(userID) && currenttime - lastMessageTime.get(userID) < MSGInterval) {
            int Messgaeciunter = messageCounter.getOrDefault(userID, 0) +1;
            messageCounter.put(userID, Messgaeciunter);
            lastMessageTime.put(userID, currenttime);


            if (Messgaeciunter >= maxAllowedMSGsWithinSeconds) {
                event.getChannel().sendMessage("Please do not Spam!").queue();
                event.getMessage().delete().queue();
                System.out.println("Spam Detected! | Message has been Deleted from " + event.getAuthor().getAsTag() + " >> " + message);
                messageCounter.put(userID, 0); // Reset Counter
            }
        } else {
            messageCounter.put(userID, 1);
            lastMessageTime.put(userID, currenttime);
        }
    }
}

