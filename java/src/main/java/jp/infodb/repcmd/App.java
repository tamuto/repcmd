package jp.infodb.repcmd;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Iterator;
import java.util.Map;
import java.util.Collection;
import java.io.IOException;

import net.sf.jasperreports.engine.JRException;

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
            
            String name = (String) map.get("name");
            logger.info("template: " + name);

            Report report = new Report(name);

            if (map.containsKey("properties")) {
                Iterator<Map.Entry<String, Object>> iter =
                    ((Map<String, Object>) map.get("properties")).entrySet().iterator();
                while(iter.hasNext()) {
                    Map.Entry<String, Object> entry = iter.next();
                    report.addItem(entry.getKey(), (String) entry.getValue());
                }
            }
            if (map.containsKey("detail")) {
                report.addDetail((Collection<Map<String, ?>>) map.get("detail"));
            }
            report.execute();
            logger.info("end repcmd.");
        }
        catch(IOException | JRException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
