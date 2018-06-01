package com.i2r.object;


import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * multimedia-data-processing
 * com.i2r.object
 * Created by Zhang Chen
 * 5/30/2018
 */


@XmlRootElement(name = "transcripts")
@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor
public class Transcripts {

    @XmlElement(name = "transcript")
    private List<Transcript> transcripts = null;

    private String id;

    private String link;

    private String inputPath;

    private String audioOutputLocation;

    private String videoOutputLocation;

    public List<Transcript> getTranscripts() {
        return transcripts;
    }

    public void setTranscripts(List<Transcript> transcripts) {
        this.transcripts = transcripts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAudioOutputLocation() {
        return audioOutputLocation;
    }

    public void setAudioOutputLocation(String audioOutputLocation) {
        this.audioOutputLocation = audioOutputLocation;
    }

    public String getVideoOutputLocation() {
        return videoOutputLocation;
    }

    public void setVideoOutputLocation(String videoOutputLocation) {
        this.videoOutputLocation = videoOutputLocation;
    }

    public String getInputPath() {
        return inputPath;
    }

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }
}
