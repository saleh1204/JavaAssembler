/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package salehs.javaassembler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Saleh
 */
public class InstructionType {

    private ArrayList<Instruction> instructions;
    private String name, seperators;
    private int rd, rs, rt, imm_label, imm_int, jmm_label, imm_int12,
            split_imm_int, offset_split_imm_int0, mask_split_imm_int0,
            offset_split_imm_int1, mask_split_imm_int1, imm_int0_size;

    public InstructionType(String name, JSONObject jsonObject) {
        this.name = name;
        this.instructions = new ArrayList<>();
        Iterator<String> it = jsonObject.getJSONObject("instructions").keys();
        while (it.hasNext()) {
            String inst_name = (String) it.next();
            JSONArray tmp = jsonObject.getJSONObject("instructions").getJSONArray(inst_name);
            int opcode = Integer.parseInt(tmp.getString(0), 2);
            int function_code = Integer.parseInt(tmp.getString(1), 2);
            Instruction inst = new Instruction(inst_name, opcode, function_code);
            this.instructions.add(inst);
        }
        this.seperators = jsonObject.getString("seperator");
        this.rd = -1;
        this.rs = -1;
        this.rt = -1;
        this.imm_label = -1;
        this.imm_int = -1;
        this.jmm_label = -1;
        this.imm_int12 = -1;
        this.split_imm_int = -1;
        this.offset_split_imm_int0 = -1;
        this.mask_split_imm_int0 = -1;
        this.offset_split_imm_int1 = -1;
        this.mask_split_imm_int1 = -1;
        this.imm_int0_size = -1;

        JSONObject regsObj = jsonObject.getJSONObject("regs");
        if (regsObj.has("rd")) {
            this.rd = regsObj.getInt("rd");
        }
        if (regsObj.has("rs")) {
            this.rs = regsObj.getInt("rs");
        }
        if (regsObj.has("rt")) {
            this.rt = regsObj.getInt("rt");
        }
        if (regsObj.has("rd")) {
            this.rd = regsObj.getInt("rd");
        }
        if (regsObj.has("imm_label")) {
            this.imm_label = regsObj.getInt("imm_label");
        }
        if (regsObj.has("imm_int")) {
            this.imm_int = regsObj.getInt("imm_int");
        }
        if (regsObj.has("imm_int12")) {
            this.imm_int12 = regsObj.getInt("imm_int12");
        }
        if (regsObj.has("jmm_label")) {
            this.jmm_label = regsObj.getInt("jmm_label");
        }
        if (regsObj.has("split_imm_int")) {
            this.split_imm_int = regsObj.getInt("split_imm_int");
        }
        if (regsObj.has("offset_split_imm_int0")) {
            this.offset_split_imm_int0 = regsObj.getInt("offset_split_imm_int0");
        }
        if (regsObj.has("mask_split_imm_int0")) {
            this.mask_split_imm_int0 = regsObj.getInt("mask_split_imm_int0");
        }
        if (regsObj.has("offset_split_imm_int1")) {
            this.offset_split_imm_int1 = regsObj.getInt("offset_split_imm_int1");
        }
        if (regsObj.has("mask_split_imm_int1")) {
            this.mask_split_imm_int1 = regsObj.getInt("mask_split_imm_int1");
        }
        if (regsObj.has("imm_int0_size")) {
            this.imm_int0_size = regsObj.getInt("imm_int0_size");
        }
    }

    public Instruction getInstructionObject(String instruction) {
        Instruction inst = null;
        for (Instruction it : this.getInstructions()) {
            if (it.getName().equalsIgnoreCase(instruction)) {
                inst = it;
                break;
            }
        }
        return inst;
    }

    public ArrayList<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(ArrayList<Instruction> instructions) {
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeperators() {
        return seperators;
    }

    public void setSeperators(String seperators) {
        this.seperators = seperators;
    }

    public int getRd() {
        return rd;
    }

    public void setRd(int rd) {
        this.rd = rd;
    }

    public int getRs() {
        return rs;
    }

    public void setRs(int rs) {
        this.rs = rs;
    }

    public int getRt() {
        return rt;
    }

    public void setRt(int rt) {
        this.rt = rt;
    }

    public int getImm_label() {
        return imm_label;
    }

    public void setImm_label(int imm_label) {
        this.imm_label = imm_label;
    }

    public int getImm_int() {
        return imm_int;
    }

    public void setImm_int(int imm_int) {
        this.imm_int = imm_int;
    }

    public int getJmm_label() {
        return jmm_label;
    }

    public void setJmm_label(int jmm_label) {
        this.jmm_label = jmm_label;
    }

    public int getSplit_imm_int() {
        return split_imm_int;
    }

    public void setSplit_imm_int(int split_imm_int) {
        this.split_imm_int = split_imm_int;
    }

    public int getOffset_split_imm_int0() {
        return offset_split_imm_int0;
    }

    public void setOffset_split_imm_int0(int offset_split_imm_int0) {
        this.offset_split_imm_int0 = offset_split_imm_int0;
    }

    public int getMask_split_imm_int0() {
        return mask_split_imm_int0;
    }

    public void setMask_split_imm_int0(int mask_split_imm_int0) {
        this.mask_split_imm_int0 = mask_split_imm_int0;
    }

    public int getOffset_split_imm_int1() {
        return offset_split_imm_int1;
    }

    public void setOffset_split_imm_int1(int offset_split_imm_int1) {
        this.offset_split_imm_int1 = offset_split_imm_int1;
    }

    public int getMask_split_imm_int1() {
        return mask_split_imm_int1;
    }

    public void setMask_split_imm_int1(int mask_split_imm_int1) {
        this.mask_split_imm_int1 = mask_split_imm_int1;
    }

    public int getImm_int0_size() {
        return imm_int0_size;
    }

    public void setImm_int0_size(int imm_int0_size) {
        this.imm_int0_size = imm_int0_size;
    }

    @Override
    public String toString() {
        return this.name + "\t" + Arrays.toString(this.instructions.toArray()) + "\t" + this.seperators;
    }

    /**
     * @return the imm_int12
     */
    public int getImm_int12() {
        return imm_int12;
    }

    /**
     * @param imm_int12 the imm_int12 to set
     */
    public void setImm_int12(int imm_int12) {
        this.imm_int12 = imm_int12;
    }
}
