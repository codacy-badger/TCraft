package com.tincher.tcraft.feature.temp;

import android.support.annotation.IntDef;
import android.support.annotation.IntRange;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by dks on 2019/1/17.
 */
public class Station {
    public final static int STATE_NORMAL   = 0;
    public final static int STATE_ABNORMAL = 1;
    public final static int STATE_STOP     = 2;
    public final static int STATE_OTHER    = 3;

    @IntDef({STATE_NORMAL, STATE_ABNORMAL, STATE_STOP, STATE_OTHER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface StationState {
    }


    private int stationCode;
    private int state;
    private int workload;

    private boolean pump1working;
    private boolean pump2working;
    private boolean pump3working;
    private boolean pump4working;


    public int getStationCode() {
        return stationCode;
    }

    public void setStationCode(int stationCode) {
        this.stationCode = stationCode;
    }

    public int getState() {
        return state;
    }

    public void setState(@StationState int state) {
        this.state = state;
        if (state == STATE_STOP) {
            setPump1working(false);
            setPump2working(false);
            setPump3working(false);
            setPump4working(false);
        }
    }

    public int getWorkload() {
        return workload;
    }

    public void setWorkload(int workload) {
        this.workload = workload;
    }

    public boolean isPump1working() {
        return pump1working;
    }

    public void setPump1working(boolean pump1working) {
        this.pump1working = pump1working;
    }

    public boolean isPump2working() {
        return pump2working;
    }

    public void setPump2working(boolean pump2working) {
        this.pump2working = pump2working;
    }

    public boolean isPump3working() {
        return pump3working;
    }

    public void setPump3working(boolean pump3working) {
        this.pump3working = pump3working;
    }

    public boolean isPump4working() {
        return pump4working;
    }

    public void setPump4working(boolean pump4working) {
        this.pump4working = pump4working;
    }

    /**
     * *****位运算方式****************************************************************************
     */

    private int flag; //站 状态

    public int getFlag() {
        return flag & ((1 << (4 + 2 + 8 + 8)) - 1);
    }

    // 从右起： 4位 泵；5-6位 状态；7-14位 workload工作量；其他站号
//    private static final int PUMP_SHIFT     = 0;
    private static final int STATE_SHIFT    = 4;
    private static final int WORKLOAD_SHIFT = 6;
    private static final int CODE_SHIFT     = 14;


    public final static int MUSK_PUMP     = 7;//  00000000  0000 0000  11  0000
    public final static int MUSK_STATE    = 3 << STATE_SHIFT;//  00000000  0000 0000  11  0000
    public final static int MUSK_WORKLOAD = 255 << WORKLOAD_SHIFT;//  00000000  1111 1111  00  0000
    public final static int MUSK_CODE     = 255 << CODE_SHIFT;//  11111111  0000 0000  00  0000


    // 泵4位
    public final static int PUMP_1 = 0x1; //泵号1
    public final static int PUMP_2 = 0x2;
    public final static int PUMP_3 = 0x4;
    public final static int PUMP_4 = 0x8;

    @IntDef({PUMP_1, PUMP_2, PUMP_3, PUMP_4})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Pump {
    }

    /**
     * @param pump 0x1 0x2 0x4 0x8 分别为单个泵，可以组合，如 0x6 代表 是否是 2、3号开启，1、4号关闭 状态
     */
    public boolean isPumpWorking(@Pump int pump) {
        return (flag & MUSK_PUMP & pump) == pump;
    }

    /**
     * @param pump 泵号
     */
    public void setPumpWorking(@Pump int pump) {
        this.flag |= (pump & MUSK_PUMP);
    }

    public void setPumpStopping(@Pump int pump) {
        this.flag &= ~(pump & MUSK_PUMP);
    }

    public void setPump(int working) {
        this.flag = flag;
    }

    /**
     * 直接设置四个泵状态
     *
     * @param pumpByte 四个泵状态
     */
    public void setAllPumpWorkingOrStopping(@IntRange(from = 0, to = (1 << STATE_SHIFT) - 1) int pumpByte) {
        this.flag = (flag & ~MUSK_PUMP) | (pumpByte & MUSK_PUMP);
    }


    // 状态2位
    public final static int BYTE_STATE_NORMAL   = 0;
    public final static int BYTE_STATE_ABNORMAL = 1 << STATE_SHIFT;
    public final static int BYTE_STATE_STOP     = 2 << STATE_SHIFT;
    public final static int BYTE_STATE_OTHER    = 3 << STATE_SHIFT;

    @IntDef({BYTE_STATE_NORMAL, BYTE_STATE_ABNORMAL, BYTE_STATE_STOP, BYTE_STATE_OTHER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ByteState {
    }


    public int getStateByByte() {
        return flag & MUSK_STATE;
    }

    public void setStateByByte(@ByteState int byteState) {
        this.flag = (flag & ~MUSK_STATE) | byteState;
        if (byteState == BYTE_STATE_STOP) {
            setAllPumpWorkingOrStopping(0);
        }
    }


    //工作量8位 0-255
    public int getWorkloadByByte() {
        return (flag & MUSK_WORKLOAD) >> WORKLOAD_SHIFT;
    }

    public void setWorkloadByByte(@IntRange(from = 0, to = ((1 << 8) - 1)) int workload) {
        this.flag = (flag & ~MUSK_WORKLOAD) | (workload << WORKLOAD_SHIFT);
    }


    //站编号 8位 0-255
    public int getStationCodeByByte() {
        return (flag & MUSK_CODE) >> CODE_SHIFT;
    }

    public void setStationCodeByByte(@IntRange(from = 0, to = ((1 << 8) - 1)) int code) {
        this.flag = (flag & ~MUSK_CODE) | (code << CODE_SHIFT);
    }


    public String getFlagString() {
        try {
            String       binaryString = Integer.toBinaryString(getFlag());
            StringBuffer buffer       = new StringBuffer(binaryString);

            for (int i = 0; i < 22 - binaryString.length(); i++) {
                buffer.insert(0, "0");
            }
            buffer.insert(4, " ");
            buffer.insert(9, ",");
            buffer.insert(14, " ");
            buffer.insert(19, ",");
            buffer.insert(22, ",");
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public static void main(String[] args) {
        System.out.print(Integer.toBinaryString((1 << (4 + 2 + 8 + 8)) - 1));
        System.out.print("\n---------------------------------\n");
//        System.out.print(flag);
        Station station = new Station();
        station.setStateByByte(BYTE_STATE_STOP);
//        System.out.print("\n" + "State: " + station.getStateByByte() + "\n");
//        System.out.print("\n" + "State == BYTE_STATE_STOP: " + (station.getStateByByte() == BYTE_STATE_STOP) + "\n");


        station.setStationCodeByByte(255);
//        System.out.print("\n" + "Code: " + station.getStationCodeByByte() + "\n");

        station.setWorkloadByByte(18);
//        System.out.print("\n" + "Workload: " + station.getWorkloadByByte() + "\n");

        station.setPumpWorking(PUMP_1);
//        System.out.print("\n" + "PUMP_1: " + station.isPumpWorking(PUMP_1) + "\n");
//        System.out.print("\n" + "PUMP_3: " + station.isPumpWorking(PUMP_3) + "\n");

        station.setPumpStopping(PUMP_1);
//        System.out.print("\n" + "PUMP_1: " + station.isPumpWorking(PUMP_1) + "\n");

        station.setAllPumpWorkingOrStopping(6);
//        System.out.print("\n" + "PUMP_1: " + station.isPumpWorking(PUMP_1) + "\n");
//        System.out.print("\n" + "PUMP_2: " + station.isPumpWorking(PUMP_2) + "\n");
//        System.out.print("\n" + "PUMP_3: " + station.isPumpWorking(PUMP_3) + "\n");
//        System.out.print("\n" + "PUMP_4: " + station.isPumpWorking(PUMP_4) + "\n");

        System.out.print("\n" + station.getFlagString() + "\n");

        System.out.print("\n" + "State: " + station.getStateByByte() + "\n");
        System.out.print("\n" + "State == BYTE_STATE_STOP: " + (station.getStateByByte() == BYTE_STATE_STOP) + "\n");

        System.out.print("\n" + "Workload: " + station.getWorkloadByByte() + "\n");

        System.out.print("\n" + "Code: " + station.getStationCodeByByte() + "\n");

        System.out.print("\n" + "PUMP_1: " + station.isPumpWorking(PUMP_1) + "\n");
        System.out.print("\n" + "PUMP_2: " + station.isPumpWorking(PUMP_2) + "\n");


        System.out.print("\n");

        System.out.print("\n---------------------------------\n");


    }

}
