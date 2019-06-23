package com.wcl.raidnight;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wcl.raidnight.models.GuildReport;
import com.wcl.raidnight.models.ImmutableGuildReport;
import org.apache.http.client.fluent.Request;
import org.immutables.value.Value;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author dtou
 */

// This class stores a report JSON from wcl API

public class GuildReportsResponse {
    final ObjectMapper mapper = new ObjectMapper();
    final int timeout = 5000;
    private List<GuildReport> guildReportList = new ArrayList<GuildReport>();

    public GuildReportsResponse(String guildName, String serverName, String region) throws IOException {
        URI uri;

        try {
            String uriString = String.format("https://www.warcraftlogs.com/v1/reports/guild/%s/%s/%s?api_key=bb7a652ddabff076285430d88b002dc8", guildName, serverName, region).replace(" ", "%20");
            uri = new URI(uriString);

            Request request = Request.Get(uri);

            final byte[] dataRaw = request
                    .connectTimeout(timeout)
                    .socketTimeout(timeout)
                    .execute()
                    .returnContent()
                    .asBytes();
            final String data = new String(dataRaw);
            JsonNode jsonNode = mapper.readTree(data);

            for (JsonNode reportNode : jsonNode) {
                guildReportList.add(buildGuildReport(reportNode));
            }

            System.out.println(jsonNode.toString());
        } catch (URISyntaxException e) {
        }
    }

    private GuildReport buildGuildReport(JsonNode jsonNode) {
        ImmutableGuildReport.Builder builder = ImmutableGuildReport.builder();

        return builder.id(jsonNode.path("id").asText())
                .title(jsonNode.path("title").asText())
                .owner(jsonNode.path("owner").asText())
                .start(jsonNode.path("start").asLong())
                .end(jsonNode.path("end").asLong())
                .zone(jsonNode.path("zone").asInt())
                .build();
    }

    public GuildReport getMostRecentGuildReport(int zone) {
        int highestIndex = 0;
        long mostRecent = 0;
        int index = 0;
        int maxIndex = getNumReports();

        while (index < maxIndex) {
            GuildReport currentReport = guildReportList.get(index);

            if (currentReport.zone() == zone && currentReport.start() > mostRecent) {
                highestIndex = index;
                mostRecent = currentReport.start();
            }

            index++;
        }

        System.out.println(highestIndex);

        return guildReportList.get(highestIndex);
    }

    public int getNumReports() {
        return guildReportList.size();
    }
}
