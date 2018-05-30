package com.i2r.utils;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.i2r.object.Transcript;
import com.i2r.object.Transcripts;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * multimedia-data-processing
 * com.i2r.utils
 * Created by Zhang Chen
 * 5/30/2018
 */

@Slf4j
public class XMLMapper {

    private  XMLMapper(){}

    private static XmlMapper xmlMapper = null;

    public static <T> void objToXML(String path, T src) {
        if (xmlMapper == null)
            xmlMapper = new XmlMapper();

        if(src == null)
            return;
        try {
                xmlMapper.writeValue(new File(path), src);
        } catch (Exception e) {
            log.error("Parse Object to XML exception, Object: {}, error: {}", src, e);
        }
    }

    public static <T> void listToXML(String path, List<T> srcList) throws JAXBException {
        if (xmlMapper == null) {
            xmlMapper = new XmlMapper();
            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        }
        if(srcList.isEmpty())
            return;
        if(srcList.get(0) instanceof Transcript) {
            List<Transcript> tempList = new ArrayList<>();
            for(T t: srcList) {
                tempList.add((Transcript) t);
            }
            Transcripts transcripts = new Transcripts();
            transcripts.setTranscripts(tempList);
            JAXBContext jaxbContext = JAXBContext.newInstance(Transcripts.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(transcripts, new File(path));
        }
    }

}
