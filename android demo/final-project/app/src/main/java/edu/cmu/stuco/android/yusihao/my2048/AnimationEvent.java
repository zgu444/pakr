package edu.cmu.stuco.android.yusihao.my2048;

public class AnimationEvent{
    private int startRow = -1;
    private int startCol= -1;
    private int endRow=-1;
    private int endCol=-1;
    private AnimationType type;
    public enum AnimationType{
        Float,
        Scale;
    }
    public AnimationEvent(AnimationType type){
        this.type = type;
    }
    public AnimationEvent setStartPos(int sR, int sC){
        startRow = sR;
        startCol = sC;
        return this;
    }
    public AnimationEvent setEndPos(int eR,int eC){
        endRow = eR;
        endCol = eC;
        return this;
    }

    public int getEndCol() {
        return type == AnimationType.Scale ? -1 : endCol;
    }

    public int getEndRow() {
        return type == AnimationType.Scale ? -1 : endRow;
    }

    public int getStartCol() {
        return startCol;
    }

    public int getStartRow() {
        return startRow;
    }

    private AnimationEvent rotate(int angle) {

        int offsetX = 3, offsetY = 3;
        if (angle == 90) {
            offsetY = 0;
        } else if (angle == 270) {
            offsetX = 0;
        }

        double rad = Math.toRadians(angle);
        int cos = (int) Math.cos(rad);
        int sin = (int) Math.sin(rad);
        int x = startCol;
        int y = startRow;
        startCol = (x * sin) + (y * cos) + offsetY;
        startRow = (x * cos) - (y * sin) + offsetX;
        x = endCol;
        y = endRow;
        endCol = (x * sin) + (y * cos) + offsetY;
        endRow = (x * cos) - (y * sin) + offsetX;
        return this;
    }
}
