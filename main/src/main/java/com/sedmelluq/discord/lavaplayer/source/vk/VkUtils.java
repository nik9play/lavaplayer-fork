package com.sedmelluq.discord.lavaplayer.source.vk;

import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;

public class VkUtils {

    private static final String VK_ORIGIN = "https://api.vk.com/method/";
    private static final String AUDIO_GET_BY_ID = "audio.getById";
    private static final String API_VERSION = "5.131";

    public static URI getTrackUrl(String trackId, String accessToken) throws URISyntaxException {
        return new URIBuilder(VK_ORIGIN + AUDIO_GET_BY_ID)
                .addParameter("access_token", accessToken)
                .addParameter("v", API_VERSION)
                .addParameter("audios", trackId)
                .build();
    }
}