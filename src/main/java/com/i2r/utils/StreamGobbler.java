package com.i2r.utils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
/**
 * multimedia-data-processing
 * com.i2r.utils
 * Created by Zhang Chen
 * 6/2/2018
 */
@Slf4j
public class StreamGobbler extends Thread
{
    InputStream is;
    String type;

    public StreamGobbler(InputStream is, String type)
    {
        this.is = is;
        this.type = type;
    }

    public void run()
    {
        try
        {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null)
                log.info(type + ">" + line);
        } catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
}