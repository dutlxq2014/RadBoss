package com.codesky.radboss.core;

/**
 *
 * Created by xueqiulxq on 8/21/16.
 */
public class RadParams {

    public ERadOpt opcode;

    public Object operand;

    public RadCallback callback;

    public RadParams(ERadOpt opt) {
        opcode = opt;
    }
}
