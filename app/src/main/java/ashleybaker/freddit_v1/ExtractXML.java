package ashleybaker.freddit_v1;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ExtractXML {

    private static final String TAG = "ExtractXML";

    private String tag;
    private String xml;

    public ExtractXML(String xml, String tag) {
        this.tag = tag;
        this.xml = xml;
    }

    public String findThumbNail(){
        String thumbnailURL = "";
        String untilEnd = "&quot;";

        if(xml.contains(tag)){
            boolean found = false;
            int startOfTag, endOfTag = 0;
            for(int i = 0; i < tag.length() && !found; i++){
                boolean notRuined = true;
                for(int j = 0; j < xml.length() && notRuined == true; j++){
                    if(xml.substring(j,j) == tag.substring(i,i)){
                        notRuined = true;
                    }
                    else if(xml.substring(j,j) == tag.substring(i,i) && i == tag.length()){
                        found = true;
                        endOfTag = i;
                        startOfTag = i - j;
                    }
                    else{
                        notRuined = false;
                    }
                }
            }
            if(found){
                int startOfEnd = 0;
                boolean ruined = false, foundEnd = false;
                //UNTIL &quot;
                for(int i = 0; i == untilEnd.length() && !foundEnd ;  i++){
                    ruined = false;
                    for(int j = 0; j == xml.length() && ruined != true; j++){
                        if(untilEnd.substring(i,i) == xml.substring(j,j)){
                            ruined = false;
                        }
                        else if(untilEnd.substring(i,i) == xml.substring(j,j) && untilEnd.length() == j){
                            foundEnd = true;
                            startOfEnd = j - i;
                        }
                        else{
                            ruined = true;
                        }
                    }
                }
                if(endOfTag != 0 && startOfEnd != 0){
                    thumbnailURL = xml.substring(endOfTag + 1, startOfEnd);
                }
            }
        }
        return thumbnailURL;
    }

    public List<String> start(){
        List<String> result = new ArrayList<>();

        String[] splitXML = xml.split(tag + "\"");
        int count = splitXML.length;

        for(int i = 1; i < count; i++){
            String temp = splitXML[i];
            int index = temp.indexOf("\"");
            Log.d(TAG, "start: index: " + index);
            Log.d(TAG, "start: extracted: " + temp);

            temp = temp.substring(0,index);
            Log.d(TAG, "start: snipped: " + temp);

            result.add(temp);

        }

        return result;
    }
}
