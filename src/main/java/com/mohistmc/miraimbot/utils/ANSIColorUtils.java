package com.mohistmc.miraimbot.utils;

public class ANSIColorUtils {

    public static String getColor(String text, String d) {
        switch (text) {
            case "1":
                text = "\u001B[34;22m";// 蓝
                break;
            case "2":
                text = "\u001B[32;22m";// 绿
                break;
            case "3":
                text = "\u001B[36;22m";// 深绿
                break;
            case "4":
                text = "\u001B[31;22m";// 红
                break;
            case "5":
                text = "\u001B[35;22m";// 紫
                break;
            case "6":
                text = "\u001B[33;22m";// 黄
                break;
            case "7":
                text = "\u001B[37;22m";// 白
                break;
            case "8":
                text = "\u001B[30;1m";// 黑&高亮
                break;
            case "9":
                text = "\u001B[34;1m";// 蓝&高亮
                break;
            case "a":
                text = "\u001B[32;1m";// 绿&高亮
                break;
            case "b":
                text = "\u001B[36;1m";// 深绿&高亮
                break;
            case "c":
                text = "\u001B[31;1m";// 红&高亮
                break;
            case "d":
                text = "\u001B[35;1m";// 紫&高亮
                break;
            case "e":
                text = "\u001B[33;1m";// 黄&高亮
                break;
            case "f":
                text = "\u001B[37;1m";// 白&高亮
                break;
            case "r":
                text = "\u001B[39;0m";
                break;
            default:
                text = d;
        }
        return text;
    }
}
