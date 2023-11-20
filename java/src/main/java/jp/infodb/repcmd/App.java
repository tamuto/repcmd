package jp.infodb.repcmd;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Iterator;
import java.util.Map;
import java.util.Collection;
import java.io.IOException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperExportManager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class App
{
    private static final Logger logger = Logger.getLogger(App.class.getName());
    public static void main( String[] args )
    {
        logger.info("start repcmd");
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(
                System.in,
                new TypeReference<Map<String, Object>>(){});

            String name = (String) map.get("template");
            logger.info("template: " + name);

            JasperPrint print = null;
            Report report = new Report(name);
            for (Map<String, Object> dataSet : ((Collection<Map<String, Object>>) map.get("data"))) {
                report.reset();
                if (dataSet.containsKey("properties")) {
                    Iterator<Map.Entry<String, Object>> iter =
                        ((Map<String, Object>) dataSet.get("properties")).entrySet().iterator();
                    while(iter.hasNext()) {
                        Map.Entry<String, Object> entry = iter.next();
                        report.addItem(entry.getKey(), (String) entry.getValue());
                    }
                }
                if (dataSet.containsKey("detail")) {
                    report.addDetail((Collection<Map<String, ?>>) dataSet.get("detail"));
                }
                JasperPrint result = report.execute();
                if (print == null) {
                    print = result;
                } else {
                    for (JRPrintPage page : result.getPages()) {
                        print.addPage(page);
                    }
                }
            }
            if (print == null) {
                logger.info("no data.");
                return;
            }
            JasperExportManager.exportReportToPdfStream(print, System.out);
            logger.info("end repcmd.");
        }
        catch(IOException | JRException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
