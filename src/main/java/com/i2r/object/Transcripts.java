package com.i2r.object;


import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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

    public List<Transcript> getTranscripts() {
        return transcripts;
    }

    public void setTranscripts(List<Transcript> transcripts) {
        this.transcripts = transcripts;
    }

}
