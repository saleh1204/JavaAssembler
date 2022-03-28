/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package salehs.javaassembler;

/**
 *
 * @author Saleh
 */
public class Instruction {

    private String name;
    private int opcode, functionCode;

    public Instruction(String name, int opcode, int functionCode) {
        this.name = name;
        this.opcode = opcode;
        this.functionCode = functionCode;
    }

    public String getName() {
        return this.name;
    }

    public int getOpcode() {
        return this.opcode;
    }

    public int getFunctionCode() {
        return this.functionCode;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setOpcode(int newOpcode) {
        this.opcode = newOpcode;
    }

    public void setFunctionCode(int newFunctionCode) {
        this.functionCode = newFunctionCode;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
