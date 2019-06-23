package com.wcl.raidnight;

import com.wcl.raidnight.models.GuildReport;

import java.io.IOException;

/**
 * @author dtou
 */

public class Summarizer {
    public static void main(String[] args) throws IOException {
        String guildName = args[0];
        String serverName = args[1];
        String region = "us";

        System.out.println(args[0]);
        System.out.println(args[1]);

        GuildReportsResponse myResponse = new GuildReportsResponse(guildName, serverName, region);

        System.out.println(myResponse.getNumReports());

        GuildReport guildReport = myResponse.getMostRecentGuildReport(22);
    }
}
