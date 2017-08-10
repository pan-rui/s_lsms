package com.lsms.pack;


import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import static java.text.MessageFormat.format;


/**
 * @Description: ${Description}
 * @Author: 潘锐 (2017-07-12 21:52)
 * @version: \$Rev: 3996 $
 * @UpdateAuthor: \$Author: panrui $
 * @UpdateDateTime: \$Date: 2017-08-08 09:14:56 +0800 (周二, 08 8月 2017) $
 */
public class ExecutorUtil {

    private static Log log = LogFactory.getLog(ExecutorUtil.class);

    public static List<String> syncExecute(String cmd) {
        Executor executor = new DefaultExecutor();
        CommandLine commandLine = CommandLine.parse(cmd);
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        executor.setStreamHandler(new PumpStreamHandler(outputStream, errorStream));
        try {
            executor.setExitValues(null);
            int x = executor.execute(commandLine);
            log.debug("exitValue: " + x);
            String cmdOutput = outputStream.toString();
            return lineToStringArray(cmdOutput);
        }
        catch (Throwable e) {
            String errorStreamMessage = errorStream.toString();
            String exceptionMessage = format("执行命令\"{0}\"发生异常\"{1}\",命令行输出:\"{2}\"", cmd, e.getMessage(), errorStreamMessage);
            log.error(exceptionMessage, e);
            throw new Error(exceptionMessage);
        }
    }

    private static List<String> lineToStringArray(String str) throws IOException {
        List<String> lines = new ArrayList<String>();

        BufferedReader br = new BufferedReader(new StringReader(str));
        String line = br.readLine();
        while (line != null) {
            if(line != null){
                log.debug(line);
                lines.add(line);
            }
            line = br.readLine();
        }
        br.close();

        return lines;
    }
}