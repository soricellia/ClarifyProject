package hackny.clarifyproject;

import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Tony on 9/27/2015.
 */

public class HashTag {
    private String tagName;
    private Extras extra;
    private final String HASHTAG = "#";
    private boolean isSelected;

    public HashTag(String tagName, DataManager dm){
        extra = dm.getExtrasList().get((int)(Math.random()*dm.getExtrasList().size()));
        setTagName(tagName);
    }
    private void setTagName(String tag){
        String editTag = "";
        if(extra.isBefore) {
            editTag = tag.substring(0,1).toUpperCase()+tag.substring(1,tag.length());
            tagName = HASHTAG + extra.getText() + editTag;
        }else {
            tagName = HASHTAG + tag + extra.getText();
        }
    }
    public boolean isSelected(){
        return isSelected;
    }
    public void select(boolean select){
        isSelected = select;
    }

    public String getTagName() {
        return tagName;
    }

    public Extras getExtra() {
        return extra;
    }
}
