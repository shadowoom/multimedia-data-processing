package com.i2r.object;

import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * multimedia-data-processing
 * com.i2r.object
 * Created by Zhang Chen
 * 5/30/2018
 */

@XmlRootElement(name = "transcript")
@XmlAccessorType(XmlAccessType.FIELD)
@ToString
@Getter
@Setter
public class Transcript {

    private String speaker;

    private String name;

    private String text;

    private String sentiment;

    private String language;

    private String startTime;

    private String endTime;

    private String duration;

    private String sequence;

    private String audioOutputLocation;

    private String videoOutputLocation;

    private String link;

}
