package com.dong.android.utils.eventbus;

/**
 * @author <dr_dong>
 * @time 2017/3/27 10:52
 */
public class EventUtils<T> {

    public static final int T_CODE_TEST = 10001;
    public static final int A_CODE_TEST = 20001;

    /**
     * 发射码
     */
    private int transmittedCode;

    /**
     * 接收码
     */
    private int acceptanceCode;

    private T t;

    public EventUtils(int transmittedCode, int acceptanceCode, T t) {
        this.transmittedCode = transmittedCode;
        this.acceptanceCode = acceptanceCode;
        this.t = t;
    }

    public int getTransmittedCode() {
        return transmittedCode;
    }

    public void setTransmittedCode(int transmittedCode) {
        this.transmittedCode = transmittedCode;
    }

    public int getAcceptanceCode() {
        return acceptanceCode;
    }

    public void setAcceptanceCode(int acceptanceCode) {
        this.acceptanceCode = acceptanceCode;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
