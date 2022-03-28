/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package salehs.javaassembler;

import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 * @author Saleh
 */
public class Assembler {

    private AssemblerConfig config;

    public Assembler(AssemblerConfig config) {
        this.config = config;
    }

    public String assemble(String code, Map<String, Integer> labels) throws Exception {
        String assembled = "v2.0 raw\n";
        int inst_size = this.config.getIns_size();
        String inst_fmt = "%0" + (inst_size / 4) + "x";
        long inst_mask = (long) Math.pow(2, inst_size) - 1;
        String[] instructions = code.split("\n");
        for (int current = 0; current < instructions.length; current++) {
            long instruction = 0;
            String inst_name = instructions[current].split(" ")[0];
            InstructionType inst_type = config.getInstructionTypeOfInstruction(inst_name);
            if (inst_type == null) {
                throw new Exception("Instruction " + inst_name + " Not found");
            }
            String sep_str = "[" + inst_type.getSeperators() + "]+";
            String[] elements = Pattern.compile(sep_str).split(instructions[current]);
            Instruction instObj = inst_type.getInstructionObject(inst_name);
            if (instObj == null) {
                throw new Exception("Instruction " + inst_name + " Not found");
            }
            int opcode = instObj.getOpcode();
            instruction = opcode << (inst_size - config.getOpcode_offset());
            int funct = instObj.getFunctionCode();
            instruction |= funct << (inst_size - config.getFunct_offset());
            if (inst_type.getRs() != -1) {
                try {
                    int rs = Integer.parseInt(elements[inst_type.getRs()]);
                    instruction |= (rs << (inst_size - config.getRs_offset()));
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    throw new Exception(
                            "Instruction " + instructions[current] + " expects rs value at location "
                                    + inst_type.getRs());
                }
            }
            if (inst_type.getRt() != -1) {
                try {
                    int rt = Integer.parseInt(elements[inst_type.getRt()]);
                    instruction |= (rt << (inst_size - config.getRt_offset()));
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    throw new Exception(
                            "Instruction " + instructions[current] + " expects rt value at location "
                                    + inst_type.getRt());
                }
            }
            if (inst_type.getRd() != -1) {
                try {
                    int rd = Integer.parseInt(elements[inst_type.getRd()]);
                    instruction |= (rd << (inst_size - config.getRd_offset()));
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    throw new Exception(
                            "Instruction " + instructions[current] + " expects rd value at location "
                                    + inst_type.getRd());
                }
            }
            if (inst_type.getImm_int() != -1 && inst_type.getJmm_label() != -1
                    && inst_type.getImm_int() == inst_type.getJmm_label()) {
                // Special Case Scenario for jalr with either immediate value and a label
                try {
                    int imm_int = Integer.parseInt(elements[inst_type.getImm_int()]);
                    imm_int = imm_int & config.getIimm_mask();
                    if (inst_type.getSplit_imm_int() == 1) {
                        int imm_int_split0 = imm_int & inst_type.getMask_split_imm_int0();
                        int imm_int_split1 = (imm_int >> inst_type.getImm_int0_size())
                                & inst_type.getMask_split_imm_int1();
                        instruction |= (imm_int_split0) << (inst_size - inst_type.getOffset_split_imm_int0());
                        instruction |= (imm_int_split1) << (inst_size - inst_type.getOffset_split_imm_int1());
                    } else {
                        instruction |= (imm_int << (inst_size - config.getIimm_offset()));
                    }
                } catch (NumberFormatException e) {
                    String label = elements[inst_type.getJmm_label()].toLowerCase();
                    if (labels.containsKey(label)) {
                        int label_line = labels.get(label);
                        int jmm_int;
                        if (this.config.getJimm_abs() == 0) {
                            jmm_int = (label_line - current - 1) & config.getJimm_mask();
                        } else {
                            jmm_int = (label_line - 1) & config.getJimm_mask();
                        }
                        instruction |= (jmm_int << (inst_size - config.getJimm_offset()));
                    } else {
                        throw new Exception("Label (" + label + ") Not found");
                    }
                } catch (IndexOutOfBoundsException e) {
                    throw new Exception(
                            "Instruction " + instructions[current] + " expects immI value at location "
                                    + inst_type.getImm_int());
                }
            } else {
                if (inst_type.getImm_int() != -1) {
                    try {
                        int imm_int = Integer.parseInt(elements[inst_type.getImm_int()]);
                        imm_int = imm_int & config.getIimm_mask();
                        if (inst_type.getSplit_imm_int() == 1) {
                            int imm_int_split0 = imm_int & inst_type.getMask_split_imm_int0();
                            int imm_int_split1 = (imm_int >> inst_type.getImm_int0_size())
                                    & inst_type.getMask_split_imm_int1();
                            instruction |= (imm_int_split0) << (inst_size - inst_type.getOffset_split_imm_int0());
                            instruction |= (imm_int_split1) << (inst_size - inst_type.getOffset_split_imm_int1());
                        } else {
                            instruction |= (imm_int << (inst_size - config.getIimm_offset()));
                        }
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        throw new Exception(
                                "Instruction " + instructions[current] + " expects immI value at location "
                                        + inst_type.getImm_int());
                    }
                }
                if (inst_type.getJmm_label() != -1) {
                    String label = elements[inst_type.getJmm_label()].toLowerCase();
                    if (labels.containsKey(label)) {
                        int label_line = labels.get(label);
                        int jmm_int;
                        if (this.config.getJimm_abs() == 0) {
                            jmm_int = (label_line - current - 1) & config.getJimm_mask();
                        } else {
                            jmm_int = (label_line - 1) & config.getJimm_mask();
                        }
                        instruction |= (jmm_int << (inst_size - config.getJimm_offset()));
                    } else {
                        throw new Exception("Label (" + label + ") Not found");
                    }
                }
            }
            if (inst_type.getImm_label() != -1) {
                String label = elements[inst_type.getImm_label()].toLowerCase();
                if (labels.containsKey(label)) {
                    int label_line = labels.get(label);
                    int imm_int = (label_line - current - 1) & config.getIimm_mask();
                    if (inst_type.getSplit_imm_int() == 1) {
                        int imm_int_split0 = imm_int & inst_type.getMask_split_imm_int0();
                        int imm_int_split1 = (imm_int >> inst_type.getImm_int0_size())
                                & inst_type.getMask_split_imm_int1();
                        instruction |= (imm_int_split0) << (inst_size - inst_type.getOffset_split_imm_int0());
                        instruction |= (imm_int_split1) << (inst_size - inst_type.getOffset_split_imm_int1());
                    } else {
                        instruction |= (imm_int << (inst_size - config.getIimm_offset()));
                    }
                } else {
                    throw new Exception("Label (" + label + ") Not found");
                }
            }
            if (inst_type.getImm_int12() != -1) {
                try {
                    int imm_int12 = Integer.parseInt(elements[inst_type.getImm_int12()]);
                    instruction |= (imm_int12 << (inst_size - config.getJimm_offset()));
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    throw new Exception(
                            "Instruction " + instructions[current] + " expects imm_int12 value at location "
                                    + inst_type.getImm_int12());
                }

            }
            instruction = instruction & inst_mask;
            assembled += String.format(inst_fmt, instruction) + "\t";
        }

        return assembled;
    }

}
