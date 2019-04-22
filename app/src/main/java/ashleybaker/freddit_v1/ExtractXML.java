package ashleybaker.freddit_v1;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ExtractXML {

    private static final String TAG = "ExtractXML";

    private String tag;
    private String xml;
    private String endTag;

    public ExtractXML(String xml, String tag) {
        this.tag = tag;
        this.xml = xml;
    }

    public ExtractXML(String xml, String tag, String endTag) {
        this.tag = tag;
        this.xml = xml;
        this.endTag = endTag;
    }

    public List<String> start(){
        List<String> result = new ArrayList<>();
        String[] splitXML = null;
        String stop = null;

        if(endTag.equals("NONE")){
            stop = "\"";
            splitXML = xml.split(tag + stop);
        }
        else{
            stop = endTag;
            splitXML = xml.split(tag + stop);
        }

        int count = splitXML.length;

        for(int i = 1; i < count; i++){
            String temp = splitXML[i];
            int index = temp.indexOf(stop);
            Log.d(TAG, "start: index: " + index);
            Log.d(TAG, "start: extracted: " + temp);

            temp = temp.substring(0,index);
            Log.d(TAG, "start: snipped: " + temp);

            result.add(temp);

        }

        return result;
    }
}
