package jp.infodb.repcmd;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

public class Report {
    private String name;
    private Map<String, Object> map;
    private JRDataSource ds;

    public Report(String name) {
        this.name = name;
        this.map = new HashMap<>();
    }

    public void reset() {
        map.clear();
        ds = null;
    }

    public void addItem(String name, String value) {
        map.put(name, value);
    }

    public void addDetail(Collection<Map<String, ?>> col) {
        ds = new JRMapCollectionDataSource(col);
    }

    public JasperPrint execute() throws JRException {
        if (ds == null) {
            ds = new JREmptyDataSource();
        }
        return JasperFillManager.fillReport(name, map, ds);
    }
}
