package com.designdemo.uaha.data;

import android.os.Build;
import android.util.Log;

import com.support.android.designlibdemo.R;

import java.util.Random;

public class VersionData {
    public static final int NUM_OF_OS = 12;
    public static final int NUM_OF_DEVICES = 9;

    public static int getOsDrawable(int osVersion) {
        int retDraw = 0;

        switch (osVersion) {
            case OS_CUPCAKE:
                retDraw = R.drawable.ic_cupcake;
                break;
            case OS_DONUT:
                retDraw = R.drawable.ic_donut;
                break;
            case OS_ECLAIR:
                retDraw = R.drawable.ic_eclair;
                break;
            case OS_FROYO:
                retDraw = R.drawable.ic_froyo;
                break;
            case OS_GINGERBREAD:
                retDraw = R.drawable.ic_gingerbread;
                break;
            case OS_HONEYCOMB:
                retDraw = R.drawable.ic_honeycomb;
                break;
            case OS_ICS:
                retDraw = R.drawable.ic_ics;
                break;
            case OS_JB:
                retDraw = R.drawable.ic_jb;
                break;
            case OS_KITKAT:
                retDraw = R.drawable.ic_kitkat;
                break;
            case OS_LOLLIPOP:
                retDraw = R.drawable.ic_lollipop;
                break;
            case OS_MARSHMALLOW:
                retDraw = R.drawable.ic_marshmallow;
                break;
            case OS_NOUGAT:
                retDraw = R.drawable.ic_nougat;
                break;
            case OS_OREO:
                retDraw = R.drawable.ic_oreo;
                break;
            case DEVICE_DROID:
                retDraw = R.drawable.ic_device_droid;
                break;
            case DEVICE_G1:
                retDraw = R.drawable.ic_device_g1;
                break;
            case DEVICE_GNEX:
                retDraw = R.drawable.ic_device_gnex;
                break;
            case DEVICE_N1:
                retDraw = R.drawable.ic_device_n1;
                break;
            case DEVICE_N4:
                retDraw = R.drawable.ic_device_n4;
                break;
            case DEVICE_N5:
                retDraw = R.drawable.ic_device_n5;
                break;
            case DEVICE_N6:
                retDraw = R.drawable.ic_device_n6;
                break;
            case DEVICE_N7:
                retDraw = R.drawable.ic_device_n7;
                break;
            case DEVICE_N9:
                retDraw = R.drawable.ic_device_n9;
                break;
            case DEVICE_NEXS:
                retDraw = R.drawable.ic_device_nexs;
                break;
            default:
                retDraw = 0;
                break;
        }
        return retDraw;
    }

    public static int getOsNum(String osName) {
        int osNum = 0;

        if (osName.equals(getProductName(OS_CUPCAKE))) {
            return OS_CUPCAKE;
        } else if (osName.equals(getProductName(OS_DONUT))) {
            return OS_DONUT;
        } else if (osName.equals(getProductName(OS_ECLAIR))) {
            return OS_ECLAIR;
        } else if (osName.equals(getProductName(OS_FROYO))) {
            return OS_FROYO;
        } else if (osName.equals(getProductName(OS_GINGERBREAD))) {
            return OS_GINGERBREAD;
        } else if (osName.equals(getProductName(OS_HONEYCOMB))) {
            return OS_HONEYCOMB;
        } else if (osName.equals(getProductName(OS_ICS))) {
            return OS_ICS;
        } else if (osName.equals(getProductName(OS_JB))) {
            return OS_JB;
        } else if (osName.equals(getProductName(OS_KITKAT))) {
            return OS_KITKAT;
        } else if (osName.equals(getProductName(OS_LOLLIPOP))) {
            return OS_LOLLIPOP;
        } else if (osName.equals(getProductName(OS_MARSHMALLOW))) {
            return OS_MARSHMALLOW;
        }else if (osName.equals(getProductName(OS_NOUGAT))) {
            return OS_NOUGAT;
        }else if (osName.equals(getProductName(OS_OREO))) {
            return OS_OREO;
        }else if (osName.equals(getProductName(DEVICE_DROID))) {
            return DEVICE_DROID;
        } else if (osName.equals(getProductName(DEVICE_G1))) {
            return DEVICE_G1;
        } else if (osName.equals(getProductName(DEVICE_GNEX))) {
            return DEVICE_GNEX;
        } else if (osName.equals(getProductName(DEVICE_N1))) {
            return DEVICE_N1;
        } else if (osName.equals(getProductName(DEVICE_N4))) {
            return DEVICE_N4;
        } else if (osName.equals(getProductName(DEVICE_N5))) {
            return DEVICE_N5;
        } else if (osName.equals(getProductName(DEVICE_N6))) {
            return DEVICE_N6;
        } else if (osName.equals(getProductName(DEVICE_N7))) {
            return DEVICE_N7;
        } else if (osName.equals(getProductName(DEVICE_N9))) {
            return DEVICE_N9;
        } else if (osName.equals(getProductName(DEVICE_NEXS))) {
            return DEVICE_NEXS;
        }


        return osNum;
    }

    public static final int OS_CUPCAKE = Build.VERSION_CODES.CUPCAKE;
    public static final int OS_DONUT = Build.VERSION_CODES.DONUT;
    public static final int OS_ECLAIR = Build.VERSION_CODES.ECLAIR;
    public static final int OS_FROYO = Build.VERSION_CODES.FROYO;
    public static final int OS_GINGERBREAD = Build.VERSION_CODES.GINGERBREAD;
    public static final int OS_HONEYCOMB = Build.VERSION_CODES.HONEYCOMB;
    public static final int OS_ICS = Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    public static final int OS_JB = Build.VERSION_CODES.JELLY_BEAN;
    public static final int OS_KITKAT = Build.VERSION_CODES.KITKAT;
    public static final int OS_LOLLIPOP = Build.VERSION_CODES.LOLLIPOP;
    public static final int OS_MARSHMALLOW = Build.VERSION_CODES.M;
    public static final int OS_NOUGAT = Build.VERSION_CODES.N;
    public static final int OS_OREO = Build.VERSION_CODES.O;
    public static final int DEVICE_G1 = 100;
    public static final int DEVICE_DROID = 101;
    public static final int DEVICE_GNEX = 102;
    public static final int DEVICE_NEXS = 103;
    public static final int DEVICE_N1 = 104;
    public static final int DEVICE_N4 = 105;
    public static final int DEVICE_N5 = 106;
    public static final int DEVICE_N6 = 107;
    public static final int DEVICE_N7 = 108;
    public static final int DEVICE_N9 = 109;

    public static final int[] osVersions = {OS_CUPCAKE, OS_DONUT, OS_ECLAIR, OS_FROYO, OS_GINGERBREAD, OS_HONEYCOMB, OS_ICS, OS_JB, OS_KITKAT, OS_LOLLIPOP};

    public static final int[] deviceVersions = {DEVICE_G1, DEVICE_DROID, DEVICE_N1, DEVICE_NEXS, DEVICE_GNEX, DEVICE_N4, DEVICE_N5, DEVICE_N6, DEVICE_N7, DEVICE_N9};

    public static final String getRandomProductName() {
        int randInt = new Random().nextInt(osVersions.length - 1);
        return getProductName(osVersions[randInt]);
    }

    public static String getProductName(int osVersion) {
        String retStr = "";

        switch (osVersion) {
            case OS_CUPCAKE:
                retStr = "Cupcake-API Level 3";
                break;
            case OS_DONUT:
                retStr = "Donut-API Level 4";
                break;
            case OS_ECLAIR:
                retStr = "Eclair-API Level 5 to 7";
                break;
            case OS_FROYO:
                retStr = "Froyo-API Level 8";
                break;
            case OS_GINGERBREAD:
                retStr = "Gingerbread-API Level 9, 10";
                break;
            case OS_HONEYCOMB:
                retStr = "Honeycomb-API Level 11 to 13";
                break;
            case OS_ICS:
                retStr = "Ice Cream Sandwich-API Level 14, 15";
                break;
            case OS_JB:
                retStr = "Jelly Bean-API Level 16 to 18";
                break;
            case OS_KITKAT:
                retStr = "KitKat-API Level 19, 20";
                break;
            case OS_LOLLIPOP:
                retStr = "Lollipop-API Level 21";
                break;
            case OS_MARSHMALLOW:
                retStr = "Marshmallow-API Level 22";
                break;
            case OS_NOUGAT:
                retStr = "Nougat-API Level 23";
                break;
            case OS_OREO:
                retStr = "Oreo-API Level 24";
                break;
            case DEVICE_DROID:
                retStr = "Droid-Motorola";
                break;
            case DEVICE_G1:
                retStr = "G1-HTC";
                break;
            case DEVICE_GNEX:
                retStr = "Galaxy Nexus-Samsung";
                break;
            case DEVICE_N1:
                retStr = "Nexus One-HTC";
                break;
            case DEVICE_N4:
                retStr = "Nexus 4-LG";
                break;
            case DEVICE_N5:
                retStr = "Nexus 5-LG";
                break;
            case DEVICE_N6:
                retStr = "Nexus 6-Motorola";
                break;
            case DEVICE_N7:
                retStr = "Nexus 7-ASUS";
                break;
            case DEVICE_N9:
                retStr = "Nexus 9-HTC";
                break;
            case DEVICE_NEXS:
                retStr = "Nexus S-Samsung";
                break;
            default:
                retStr = "None-Set";
                break;
        }
        return retStr;
    }

    public static String getWikiQuery(int osVersion) {
        String retStr = "";

        switch (osVersion) {
            case OS_CUPCAKE:
                retStr = "Android%20Cupcake";
                break;
            case OS_DONUT:
                retStr = "Android%20Donut";
                break;
            case OS_ECLAIR:
                retStr = "Android%20Eclair";
                break;
            case OS_FROYO:
                retStr = "Android%20Froyo";
                break;
            case OS_GINGERBREAD:
                retStr = "Android%20Gingerbread";
                break;
            case OS_HONEYCOMB:
                retStr = "Android%20Honeycomb";
                break;
            case OS_ICS:
                retStr = "Android%20Ice%20Cream%20Sandwich";
                break;
            case OS_JB:
                retStr = "Android%20Jelly%20Bean";
                break;
            case OS_KITKAT:
                retStr = "Android%20KitKat";
                break;
            case OS_LOLLIPOP:
                retStr = "Android%20Lollipop";
                break;
            case DEVICE_DROID:
                retStr = "Motorola%20Droid";
                break;
            case DEVICE_G1:
                retStr = "HTC%20G1";
                break;
            case DEVICE_GNEX:
                retStr = "Samsung%20Galaxy%20Nexus";
                break;
            case DEVICE_N1:
                retStr = "HTC%20Nexus%201";
                break;
            case DEVICE_N4:
                retStr = "LG%20Nexus%204";
                break;
            case DEVICE_N5:
                retStr = "LG%20Nexus%205";
                break;
            case DEVICE_N6:
                retStr = "Motorola%20Nexus%206";
                break;
            case DEVICE_N7:
                retStr = "ASUS%20Nexus%207";
                break;
            case DEVICE_N9:
                retStr = "HTC%20Nexus%209";
                break;
            case DEVICE_NEXS:
                retStr = "Samsung%20Nexus%20S";
                break;
            default:
                retStr = "None Set";
                break;
        }
        return retStr;
    }

    public static final String[] osStrings = {
            getProductName(OS_CUPCAKE),
            getProductName(OS_DONUT),
            getProductName(OS_ECLAIR),
            getProductName(OS_FROYO),
            getProductName(OS_GINGERBREAD),
            getProductName(OS_HONEYCOMB),
            getProductName(OS_ICS),
            getProductName(OS_JB),
            getProductName(OS_KITKAT),
            getProductName(OS_LOLLIPOP),
            getProductName(OS_MARSHMALLOW),
            getProductName(OS_NOUGAT),
            getProductName(OS_OREO)
    };

    public static final String[] deviceStrings = {
            getProductName(DEVICE_G1),
            getProductName(DEVICE_DROID),
            getProductName(DEVICE_N1),
            getProductName(DEVICE_GNEX),
            getProductName(DEVICE_NEXS),
            getProductName(DEVICE_N4),
            getProductName(DEVICE_N5),
            getProductName(DEVICE_N7),
            getProductName(DEVICE_N6),
            getProductName(DEVICE_N9)
    };
}
