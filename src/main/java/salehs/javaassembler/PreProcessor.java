/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package salehs.javaassembler;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Saleh
 */
public class PreProcessor {

    private Map<String, Integer> labels;

    public PreProcessor() {
        labels = new HashMap<>();
    }

    public String process(String code) {
        getLabels().clear();
        int line_no = 1;
        String processed = "";
        for (String line : code.split("\n")) {
            line = line.trim();
            if (line.startsWith("#")) {
                continue;
            }
            if (line.contains(":")) {
                String label = line.substring(0, line.indexOf(":")).toLowerCase();
                getLabels().put(label, line_no);
                line = line.substring(line.indexOf(":") + 1);
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    if (line.contains("#")) {
                        line = line.substring(0, line.indexOf("#"));
                    }
                    processed += line + "\n";
                    line_no++;
                }
            } else if (!line.isEmpty()) {
                if (line.contains("#")) {
                    line = line.substring(0, line.indexOf("#"));
                }
                processed += line + "\n";
                line_no++;
            }
        }
        return processed;
    }

    /**
     * @return the labels
     */
    public Map<String, Integer> getLabels() {
        return labels;
    }

    /**
     * @param labels the labels to set
     */
    public void setLabels(Map<String, Integer> labels) {
        this.labels = labels;
    }
}
