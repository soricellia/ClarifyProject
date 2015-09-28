package hackny.clarifyproject;

/**
 * Created by Tony on 9/27/2015.
 */
public class Extras {
    String text;
    boolean isBefore;

    public boolean isBefore() {
        return isBefore;
    }

    public void setIsBefore(boolean isBefore) {
        this.isBefore = isBefore;
    }
    public Extras(String text, boolean isBefore){
        this.text = text;
        this.isBefore = isBefore;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
