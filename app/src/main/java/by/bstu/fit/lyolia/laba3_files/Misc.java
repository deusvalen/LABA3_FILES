package by.bstu.fit.lyolia.laba3_files;

import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 22.02.2018.
 */

public class Misc {

    public static final String TAG = "TEST";

    public static final String FILENAME = "dictionary_end.txt";


    public static boolean writeInFile (String text, String filename, boolean append) {

        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return false;
        }
        File sdPath = Environment.getExternalStorageDirectory();
        sdPath = new File(sdPath.getAbsolutePath() + File.separator + "Dictionary");
        sdPath.mkdirs();
        File sdFile = new File(sdPath, filename);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile, append));
            bw.write(text);
            bw.close();
            Log.d(TAG, "Файл записан на SD: " + sdFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Nullable
    public static List<String> readFileSD(String filename) {

        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return null;
        }

        File sdPath = Environment.getExternalStorageDirectory();
        sdPath = new File(sdPath.getAbsolutePath() + File.separator + "Dictionary");
        File sdFile = new File(sdPath, filename);
        try {
            BufferedReader br = new BufferedReader(new FileReader(sdFile));
            List<String> chunks = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                for (String chunk : line.split("\\s")) {
                    chunks.add(chunk);
                }
            }
            return chunks;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int levensteinDist(String S1, String S2) {
        int m = S1.length(), n = S2.length();
        int[] D1;
        int[] D2 = new int[n + 1];

        for (int i = 0; i <= n; i++)
            D2[i] = i;

        for (int i = 1; i <= m; i++) {
            D1 = D2;
            D2 = new int[n + 1];
            for (int j = 0; j <= n; j++) {
                if (j == 0) D2[j] = i;
                else {
                    int cost = (S1.charAt(i - 1) != S2.charAt(j - 1)) ? 1 : 0;
                    if (D2[j - 1] < D1[j] && D2[j - 1] < D1[j - 1] + cost)
                        D2[j] = D2[j - 1] + 1;
                    else if (D1[j] < D1[j - 1] + cost)
                        D2[j] = D1[j] + 1;
                    else
                        D2[j] = D1[j - 1] + cost;
                }
            }
        }
        return D2[n];
    }

    public static void initDictionary () {

        HashMap <String,String> dictionary = new HashMap<>();
        dictionary.put("Хлеб", "Bread");
        dictionary.put("Фрукт", "Fruit");
        dictionary.put("Масло", "Butter");
        dictionary.put("Банан", "Banana");
        dictionary.put("Яблоко", "Apple");
        dictionary.put("Молоко", "Milk");
        dictionary.put("Стекло", "Glass");
        dictionary.put("Глаз", "Eye");
        dictionary.put("Вишня", "Cherry");
        dictionary.put("Соль", "Salt");

        for (String key:
             dictionary.keySet()) {
            Log.d(TAG, key + ":" + dictionary.get(key));
            writeInFile(key + ":" + dictionary.get(key) + "\n", FILENAME, true);
        }

    }
}
