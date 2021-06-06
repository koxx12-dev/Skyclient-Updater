package io.github.koxx12_dev.skyclient_updater;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class newAlgoTestZone {

    public static void main(String[] args) throws IOException {
        String fldmds = "ph";
        if (SystemUtils.IS_OS_WINDOWS) {
            fldmds = System.getenv("APPDATA") + "\\.minecraft\\skyclient\\mods";
        } if (SystemUtils.IS_OS_MAC) {
            fldmds = System.getenv("HOME") + "/Library/Application Support/minecraft/skyclient/mods";
        } if (SystemUtils.IS_OS_LINUX) {
            fldmds = System.getenv("HOME") + "/minecraft/skyclient/mods";
        }

        String[] filelist = new File(fldmds).list();

        List<ModAndID> repoFiles = new ArrayList<>();
        List<ModAndID> scjson = new ArrayList<>();

        String is = OSBase.request("https://raw.githubusercontent.com/nacrt/SkyblockClient-REPO/main/files/mods.json");

        JSONArray arry = new JSONArray(is);
        for (int i = 0; i < arry.length(); i++) {
            JSONObject obj = new JSONObject(arry.get(i).toString());
            assert false;
            repoFiles.add(new ModAndID(String.valueOf(obj.get("file")),String.valueOf(obj.get("id"))));
        }

        is = FileUtils.readFileToString(new File(System.getenv("APPDATA") + "\\.minecraft\\skyclient\\skyclient.json"),"UTF-8");

        arry = new JSONObject(is).getJSONArray("mods");
        for (int i = 0; i < arry.length(); i++) {
            JSONObject obj = new JSONObject(arry.get(i).toString());
            scjson.add(new ModAndID(String.valueOf(obj.get("file")), String.valueOf(obj.get("id"))));
        }
        //repoFiles.add(new ModAndID("yes","id"));


        List<ModAndID> tst = OSBase.getModsThatNeedUpdating(filelist,repoFiles,scjson);

        System.out.println(tst);

        for (int i = 0; i < (tst.size()); i++) {
            System.out.println(tst.get(i).file);
        }

        System.exit(0);

        System.out.println("repoFiles");
        for (int i = 0; i < (repoFiles.size()-1); i++) {
            JSONObject obj = new JSONObject(arry.get(i).toString());
            System.out.println(obj.get("file") +","+ obj.get("id"));
        }
        System.out.println("");
        System.out.println("filelist");
        for (int i = 0; i < filelist.length; i++) {
            System.out.println(Array.get(filelist,i));
        }





    }

}
