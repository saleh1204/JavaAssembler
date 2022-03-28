/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package salehs.javaassembler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Saleh
 */
public class AssemblerConfig {

    private int ins_size, rt_offset, rs_offset, funct_offset, rd_offset, iimm_offset, jimm_offset, jimm_abs,
            opcode_offset, iimm_mask, jimm_mask;
    private ArrayList<InstructionType> instructionTypes;
    private JSONObject jsonObject;

    public AssemblerConfig(String filename) {
        File jsonFile = new File(filename);
        Scanner sc;
        String jsonData = "";
        if (!jsonFile.exists()) {
            System.out.println("Error");
            String msg = String.format(
                    "Configuration File (%s) is not found.\nEnsure it is located in the same folder as the JAR file.\nOr select its location using the following dialog",
                    filename);
            JOptionPane.showMessageDialog(null, msg, "Configuration File Not Found", JOptionPane.ERROR_MESSAGE);
            JFileChooser fileChooser = new JFileChooser(".");
            FileNameExtensionFilter jsonFilter = new FileNameExtensionFilter("JSON Files (.json)", "json", "text");
            fileChooser.setFileFilter(jsonFilter);
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                jsonFile = fileChooser.getSelectedFile();
            } else {
                System.exit(-1);
            }
        }
        try {
            sc = new Scanner(jsonFile);
            while (sc.hasNextLine()) {
                jsonData += sc.nextLine();
            }
        } catch (FileNotFoundException ex) {
            String msg = String.format("Configuration File (%s) is not found.\nTerminating the application",
                    jsonFile.getAbsolutePath());
            JOptionPane.showMessageDialog(null, msg, "Configuration File Not Found", JOptionPane.ERROR_MESSAGE);
        }

        jsonObject = new JSONObject(jsonData);
        this.ins_size = jsonObject.getInt("ins_size");
        this.rt_offset = jsonObject.getInt("rt_offset");
        this.rs_offset = jsonObject.getInt("rs_offset");
        this.funct_offset = jsonObject.getInt("funct_offset");
        this.rd_offset = jsonObject.getInt("rd_offset");
        this.iimm_offset = jsonObject.getInt("iimm_offset");
        this.jimm_offset = jsonObject.getInt("jimm_offset");
        this.jimm_abs = jsonObject.getInt("jimm_abs");
        this.opcode_offset = jsonObject.getInt("opcode_offset");
        this.iimm_mask = Integer.parseInt(jsonObject.getString("iimm_mask"), 16);
        this.jimm_mask = Integer.parseInt(jsonObject.getString("jimm_mask"), 16);

        instructionTypes = new ArrayList<>();
        JSONArray arr = jsonObject.getJSONArray("Types");
        JSONObject set = jsonObject.getJSONObject("set");
        Iterator<Object> it = arr.iterator();
        while (it.hasNext()) {
            String type_name = (String) it.next();
            JSONObject type_json = set.getJSONObject(type_name);
            InstructionType inst_type = new InstructionType(type_name, type_json);
            this.instructionTypes.add(inst_type);
        }

    }

    public String[] getInstrucionsName() {
        ArrayList<String> list = new ArrayList<>();
        this.instructionTypes.forEach(inst_type -> {
            inst_type.getInstructions().forEach(inst -> {
                list.add(inst.getName());
            });
        });
        String[] instructions = new String[list.size()];
        // System.out.println(Arrays.toString(list.toArray()));
        return list.toArray(instructions);
    }

    public InstructionType getInstructionTypeOfInstruction(String instruction) {
        InstructionType inst_type = null;
        for (InstructionType it : this.instructionTypes) {
            for (Instruction inst : it.getInstructions()) {
                if (inst.getName().equals(instruction)) {
                    inst_type = it;
                    break;
                }
            }
        }
        return inst_type;
    }

    /**
     * @return the ins_size
     */
    public int getIns_size() {
        return ins_size;
    }

    /**
     * @param ins_size the ins_size to set
     */
    public void setIns_size(int ins_size) {
        this.ins_size = ins_size;
    }

    /**
     * @return the rt_offset
     */
    public int getRt_offset() {
        return rt_offset;
    }

    /**
     * @param rt_offset the rt_offset to set
     */
    public void setRt_offset(int rt_offset) {
        this.rt_offset = rt_offset;
    }

    /**
     * @return the rs_offset
     */
    public int getRs_offset() {
        return rs_offset;
    }

    /**
     * @param rs_offset the rs_offset to set
     */
    public void setRs_offset(int rs_offset) {
        this.rs_offset = rs_offset;
    }

    /**
     * @return the funct_offset
     */
    public int getFunct_offset() {
        return funct_offset;
    }

    /**
     * @param funct_offset the funct_offset to set
     */
    public void setFunct_offset(int funct_offset) {
        this.funct_offset = funct_offset;
    }

    /**
     * @return the rd_offset
     */
    public int getRd_offset() {
        return rd_offset;
    }

    /**
     * @param rd_offset the rd_offset to set
     */
    public void setRd_offset(int rd_offset) {
        this.rd_offset = rd_offset;
    }

    /**
     * @return the iimm_offset
     */
    public int getIimm_offset() {
        return iimm_offset;
    }

    /**
     * @param iimm_offset the iimm_offset to set
     */
    public void setIimm_offset(int iimm_offset) {
        this.iimm_offset = iimm_offset;
    }

    /**
     * @return the jimm_offset
     */
    public int getJimm_offset() {
        return jimm_offset;
    }

    /**
     * @param jimm_offset the jimm_offset to set
     */
    public void setJimm_offset(int jimm_offset) {
        this.jimm_offset = jimm_offset;
    }

    /**
     * @return the jimm_abs
     */
    public int getJimm_abs() {
        return jimm_abs;
    }

    /**
     * @param jimm_abs the jimm_abs to set
     */
    public void setJimm_abs(int jimm_abs) {
        this.jimm_abs = jimm_abs;
    }

    /**
     * @return the opcode_offset
     */
    public int getOpcode_offset() {
        return opcode_offset;
    }

    /**
     * @param opcode_offset the opcode_offset to set
     */
    public void setOpcode_offset(int opcode_offset) {
        this.opcode_offset = opcode_offset;
    }

    /**
     * @return the iimm_mask
     */
    public int getIimm_mask() {
        return iimm_mask;
    }

    /**
     * @param iimm_mask the iimm_mask to set
     */
    public void setIimm_mask(int iimm_mask) {
        this.iimm_mask = iimm_mask;
    }

    /**
     * @return the jimm_mask
     */
    public int getJimm_mask() {
        return jimm_mask;
    }

    /**
     * @param jimm_mask the jimm_mask to set
     */
    public void setJimm_mask(int jimm_mask) {
        this.jimm_mask = jimm_mask;
    }

    /**
     * @return the instructionTypes
     */
    public ArrayList<InstructionType> getInstructionTypes() {
        return instructionTypes;
    }

    /**
     * @param instructionTypes the instructionTypes to set
     */
    public void setInstructionTypes(ArrayList<InstructionType> instructionTypes) {
        this.instructionTypes = instructionTypes;
    }

    /**
     * @return the jsonObject
     */
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    /**
     * @param jsonObject the jsonObject to set
     */
    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.instructionTypes.toArray());
    }
}
