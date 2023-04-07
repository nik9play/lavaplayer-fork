package com.sedmelluq.discord.lavaplayer.source.vk;

import com.sedmelluq.discord.lavaplayer.container.mp3.Mp3AudioTrack;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.JsonBrowser;
import com.sedmelluq.discord.lavaplayer.tools.io.HttpInterface;
import com.sedmelluq.discord.lavaplayer.tools.io.PersistentHttpStream;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.sedmelluq.discord.lavaplayer.track.DelegatedAudioTrack;
import com.sedmelluq.discord.lavaplayer.track.playback.LocalAudioTrackExecutor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import java.net.URI;

public class VkAudioTrack extends DelegatedAudioTrack {
    private final VkSourceManager sourceManager;

    public VkAudioTrack(AudioTrackInfo trackInfo, VkSourceManager sourceManager) {
        super(trackInfo);
        this.sourceManager = sourceManager;
    }

    @Override
    public void process(LocalAudioTrackExecutor executor) throws Exception {
        try (HttpInterface httpInterface = sourceManager.getHttpInterfaceManager().getInterface()) {
            processStatic(executor, httpInterface);
        }
    }

    private void processStatic(LocalAudioTrackExecutor executor, HttpInterface httpInterface) throws Exception {
        try (CloseableHttpResponse response = httpInterface.execute(new HttpGet(VkUtils.getTrackUrl(trackInfo.identifier, sourceManager.getAccessToken())))) {
            String responseStr = EntityUtils.toString(response.getEntity());
            String url = JsonBrowser.parse(responseStr).get("response").index(0).get("url").text();
            processDelegate(new Mp3AudioTrack(trackInfo, new PersistentHttpStream(httpInterface, URI.create(url), null)), executor);
        }
    }

    @Override
    public AudioTrack makeClone() {
        return new VkAudioTrack(trackInfo, sourceManager);
    }

    @Override
    public AudioSourceManager getSourceManager() {
        return sourceManager;
    }
}