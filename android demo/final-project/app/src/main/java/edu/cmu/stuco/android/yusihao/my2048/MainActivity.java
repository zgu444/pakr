package edu.cmu.stuco.android.yusihao.my2048;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView[][] textViews;
    private TextView scoreBar;
    private OnSwipeListener mDetector;
    private GestureDetectorCompat gDetector;
    private List<AnimationEvent> animationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViews = new TextView[4][4];
        inflateViewArr(textViews);
        scoreBar = (TextView)findViewById(R.id.scoreDisplay);
        findViewById(R.id.restart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
                repaint();
            }
        });
        mDetector = new OnSwipeListener(){
            @Override
            public boolean onSwipe(Direction direction){
                Log.d("onSwipe","received");
                if (!canMove()) {
                    myLose = true;
                }

                if (!myWin && !myLose) {
                    switch (direction) {
                        case left:
                            Log.d("onSwipe","left");
                            left();
                            break;
                        case right:
                            Log.d("onSwipe","right");
                            right();
                            break;
                        case down:
                            Log.d("onSwipe","down");
                            down();
                            break;
                        case up:
                            Log.d("onSwipe","up");
                            up();
                            break;
                    }
                }

                if (!myWin && !canMove()) {
                    myLose = true;
                }
                //TODO render animation
                int moveDist = findViewById(R.id.r0c0).getWidth();
                List<ObjectAnimator> animators = new ArrayList<>();
                List<ViewPropertyAnimator> scale = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        Tile t = myTiles[i*4 + j];
                        final TextView animateView = textViews[i][j];
                        int prevRow = t.oldRow;
                        int prevCol = t.oldCol;
                        Tile prevT = myTiles[prevRow*4 + prevCol];
                        final TextView prevView = textViews[prevRow][prevCol];
                        if (t.oldValue != 0 && t.oldRow != t.row){
                            ObjectAnimator slideHorizontal = ObjectAnimator.ofFloat(prevView,
                                    "translationY", moveDist*(t.row-t.oldRow));
                            slideHorizontal.setDuration(250);
                            slideHorizontal.addListener(new Animator.AnimatorListener()
                            {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    ObjectAnimator slideLeft = ObjectAnimator.ofFloat(prevView, "translationY", 0);
                                    slideLeft.setDuration(0);
                                    slideLeft.start();
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });
                            slideHorizontal.setInterpolator(new AccelerateDecelerateInterpolator());
                            animators.add(slideHorizontal);
                        }
                        if (t.oldValue != 0 && t.oldCol != t.col) {
                            ObjectAnimator slideVertical = ObjectAnimator.ofFloat(prevView,
                                    "translationX", moveDist*(t.col-t.oldCol));
                            slideVertical.setDuration(200);
                            slideVertical.addListener(new Animator.AnimatorListener()
                            {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    ObjectAnimator slideLeft = ObjectAnimator.ofFloat(prevView, "translationX", 0);
                                    slideLeft.setDuration(0);
                                    slideLeft.start();
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });
                            slideVertical.setInterpolator(new AccelerateDecelerateInterpolator());
                            animators.add(slideVertical);
                        }
                        if (t.oldValue != 0 &&
                                (t.oldRow!=t.row || t.oldCol != t.col) && t.oldValue != t.value){
                            scale.add(animateView.animate()
                                    .scaleX(2)
                                    .scaleY(2)
                                    .setDuration(100)
                                    .setInterpolator(new BounceInterpolator())
                                    .setListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            animateView.animate()
                                                    .scaleX(1)
                                                    .scaleY(1)
                                                    .setDuration(100)
                                                    .setInterpolator(new BounceInterpolator())
                                                    .start();
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animation) {

                                        }
                                    }));
                        }
                        t.oldCol = t.col;
                        t.oldValue = t.value;
                        t.oldRow = t.row;
                    }
                }
                for (ObjectAnimator objectAnimator: animators){
//                    objectAnimator.start();
                }
                for (ViewPropertyAnimator viewPropertyAnimator : scale) {
                    viewPropertyAnimator.start();
                }
                repaint();
                return false;
            }
        };
        gDetector = new GestureDetectorCompat(this,mDetector);
        resetGame();
        repaint();
        Log.d("onCreate","Inited");
    }
    private void inflateViewArr(TextView[][] tArr){
        tArr[0][0] = (TextView)findViewById(R.id.r0c0);
        tArr[0][1] = (TextView)findViewById(R.id.r0c1);
        tArr[0][2] = (TextView)findViewById(R.id.r0c2);
        tArr[0][3] = (TextView)findViewById(R.id.r0c3);
        tArr[1][0] = (TextView)findViewById(R.id.r1c0);
        tArr[1][1] = (TextView)findViewById(R.id.r1c1);
        tArr[1][2] = (TextView)findViewById(R.id.r1c2);
        tArr[1][3] = (TextView)findViewById(R.id.r1c3);
        tArr[2][0] = (TextView)findViewById(R.id.r2c0);
        tArr[2][1] = (TextView)findViewById(R.id.r2c1);
        tArr[2][2] = (TextView)findViewById(R.id.r2c2);
        tArr[2][3] = (TextView)findViewById(R.id.r2c3);
        tArr[3][0] = (TextView)findViewById(R.id.r3c0);
        tArr[3][1] = (TextView)findViewById(R.id.r3c1);
        tArr[3][2] = (TextView)findViewById(R.id.r3c2);
        tArr[3][3] = (TextView)findViewById(R.id.r3c3);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tArr[i][j].setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        gDetector.onTouchEvent(event);
                        return false;
                    }
                });
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent me){
        Log.d("onTouchEvent","received");
        gDetector.onTouchEvent(me);
        return super.onTouchEvent(me);
    }

    /**
     * Adapted from https://github.com/bulenkov/2048
     */
    class Tile{
        int value;
        int oldRow;
        int oldCol;
        int oldValue;
        int row;
        int col;
        public Tile setOldTile(Tile t){
            oldRow = t.row;
            oldCol = t.col;
            oldValue = t.value;
            return this;
        }
        public Tile() {
            this(0);
        }

        public Tile(int num) {
            value = num;
        }

        public boolean isEmpty() {
            return value == 0;
        }

        public int getForeground() {
            return value < 16 ? 0xff776e65 :  0xfff9f6f2;
        }

        public int getBackground() {
            switch (value) {
                case 2:    return 0xffeee4da;
                case 4:    return 0xffede0c8;
                case 8:    return 0xfff2b179;
                case 16:   return 0xfff59563;
                case 32:   return 0xfff67c5f;
                case 64:   return 0xfff65e3b;
                case 128:  return 0xffedcf72;
                case 256:  return 0xffedcc61;
                case 512:  return 0xffedc850;
                case 1024: return 0xffedc53f;
                case 2048: return 0xffedc22e;
                default: return 0xffcdc1b4;
            }
        }
    }
    private final int BG_COLOR = 0xffbbada0;

    private Tile[] myTiles;
    boolean myWin = false;
    boolean myLose = false;

    int myScore = 0;
    private void updateScore(int score){
        myScore = score;
        scoreBar.setText("Score: "+Integer.toString(score));
    }


    public void resetGame() {
        updateScore(0);
        myWin = false;
        myLose = false;
        myTiles = new Tile[4 * 4];
        for (int i = 0; i < myTiles.length; i++) {
            myTiles[i] = new Tile();
        }
        setTileCoord();
        addTile();
        addTile();
    }
    private void setTileCoord(){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                myTiles[i*4 + j].row = i;
                myTiles[i*4 + j].col = j;
            }
        }
    }
    public void left() {
        boolean moved = false;
        for (int i = 0; i < 4; i++) {
            Tile[] line = getLine(i);
            Tile[] merged = mergeLine(moveLine(line));
            setLine(i, merged);
            if (!moved && !compare(line, merged)) {
                moved = true;
            }
        }

        if (moved) {
            addTile();
        }
        setTileCoord();
    }

    public void right() {
        myTiles = rotate(180);
        left();
        myTiles = rotate(180);
    }

    public void up() {
        myTiles = rotate(270);
        left();
        myTiles = rotate(90);
    }

    public void down() {
        myTiles = rotate(90);
        left();
        myTiles = rotate(270);
    }

    private Tile tileAt(int x, int y) {
        Tile t = myTiles[x + y * 4];
        t.row = y;
        t.col = x;
        return t;
    }

    private void addTile() {
        List<Tile> list = availableSpace();
        if (!availableSpace().isEmpty()) {
            int index = (int) (Math.random() * list.size()) % list.size();
            Tile emptyTime = list.get(index);
            emptyTime.value = Math.random() < 0.9 ? 2 : 4;
        }
    }

    private List<Tile> availableSpace() {
        final List<Tile> list = new ArrayList<Tile>(16);
        for (Tile t : myTiles) {
            if (t.isEmpty()) {
                list.add(t);
            }
        }
        return list;
    }

    private boolean isFull() {
        return availableSpace().size() == 0;
    }

    boolean canMove() {
        if (!isFull()) {
            return true;
        }
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                Tile t = tileAt(x, y);
                if ((x < 3 && t.value == tileAt(x + 1, y).value)
                        || ((y < 3) && t.value == tileAt(x, y + 1).value)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean compare(Tile[] line1, Tile[] line2) {
        if (line1 == line2) {
            return true;
        } else if (line1.length != line2.length) {
            return false;
        }

        for (int i = 0; i < line1.length; i++) {
            if (line1[i].value != line2[i].value) {
                return false;
            }
        }
        return true;
    }

    private Tile[] rotate(int angle) {
        Tile[] newTiles = new Tile[4 * 4];
        int offsetX = 3, offsetY = 3;
        if (angle == 90) {
            offsetY = 0;
        } else if (angle == 270) {
            offsetX = 0;
        }

        double rad = Math.toRadians(angle);
        int cos = (int) Math.cos(rad);
        int sin = (int) Math.sin(rad);
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                int newX = (x * cos) - (y * sin) + offsetX;
                int newY = (x * sin) + (y * cos) + offsetY;
                newTiles[(newX) + (newY) * 4] = tileAt(x, y);
            }
        }
        return newTiles;
    }

    private Tile[] moveLine(Tile[] oldLine) {
        LinkedList<Tile> l = new LinkedList<Tile>();
        for (int i = 0; i < 4; i++) {
            if (!oldLine[i].isEmpty())
                l.addLast(oldLine[i].setOldTile(oldLine[i]));
        }
        if (l.size() == 0) {
            return oldLine;
        } else {
            Tile[] newLine = new Tile[4];
            ensureSize(l, 4);
            for (int i = 0; i < 4; i++) {
                newLine[i] = l.removeFirst();
            }
            return newLine;
        }
    }

    private Tile[] mergeLine(Tile[] oldLine) {
        LinkedList<Tile> list = new LinkedList<Tile>();
        for (int i = 0; i < 4 && !oldLine[i].isEmpty(); i++) {
            int num = oldLine[i].value;
            Tile oldTile = null;
            if (i < 3 && oldLine[i].value == oldLine[i + 1].value) {
                num *= 2;
                updateScore(myScore + num);
                oldTile = oldLine[i+1];
                int ourTarget = 2048;
                if (num == ourTarget) {
                    myWin = true;
                }
                i++;
            }
            Tile newT = new Tile(num);
            if (oldTile != null){
                newT.setOldTile(oldTile);
            }
            list.add(newT);
        }
        if (list.size() == 0) {
            return oldLine;
        } else {
            ensureSize(list, 4);
            return list.toArray(new Tile[4]);
        }
    }

    private void ensureSize(java.util.List<Tile> l, int s) {
        while (l.size() != s) {
            l.add(new Tile());
        }
    }

    private Tile[] getLine(int index) {
        Tile[] result = new Tile[4];
        for (int i = 0; i < 4; i++) {
            result[i] = tileAt(i, index);
        }
        return result;
    }

    private void setLine(int index, Tile[] re) {
        System.arraycopy(re, 0, myTiles, index * 4, 4);
    }

    public void repaint() {
//        g.setColor(BG_COLOR);
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                drawTile(myTiles[x + y * 4], x, y);
            }
        }
    }

    private void drawTile( Tile tile, int x, int y) {
        TextView thisView = textViews[y][x];
        int value = tile.value;
        thisView.setBackgroundColor(tile.getBackground());

        if (value != 0) {
            String s = String.valueOf(value);
            thisView.setText(s);
        }
        else{
            thisView.setText("");
        }
        if (myWin || myLose) {
            if (myWin) {
                scoreBar.setText("You won!");
            }
            if (myLose) {
                scoreBar.setText("Game over!\nYou lose!");
            }
        }

    }

    /**
     * http://stackoverflow.com/questions/13095494/how-to-detect-swipe-direction-between-left-right-and-up-down
     * by fernandohur
     */
    public class OnSwipeListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            // Grab two events located on the plane at e1=(x1, y1) and e2=(x2, y2)
            // Let e1 be the initial event
            // e2 can be located at 4 different positions, consider the following diagram
            // (Assume that lines are separated by 90 degrees.)
            //
            //
            //         \ A  /
            //          \  /
            //       D   e1   B
            //          /  \
            //         / C  \
            //
            // So if (x2,y2) falls in region:
            //  A => it's an UP swipe
            //  B => it's a RIGHT swipe
            //  C => it's a DOWN swipe
            //  D => it's a LEFT swipe
            //

            float x1 = e1.getX();
            float y1 = e1.getY();

            float x2 = e2.getX();
            float y2 = e2.getY();

            Direction direction = getDirection(x1,y1,x2,y2);
            return onSwipe(direction);
        }

        /** Override this method. The Direction enum will tell you how the user swiped. */
        public boolean onSwipe(Direction direction){
            return false;
        }

        /**
         * Given two points in the plane p1=(x1, x2) and p2=(y1, y1), this method
         * returns the direction that an arrow pointing from p1 to p2 would have.
         * @param x1 the x position of the first point
         * @param y1 the y position of the first point
         * @param x2 the x position of the second point
         * @param y2 the y position of the second point
         * @return the direction
         */
        public Direction getDirection(float x1, float y1, float x2, float y2){
            double angle = getAngle(x1, y1, x2, y2);
            return Direction.get(angle);
        }

        /**
         *
         * Finds the angle between two points in the plane (x1,y1) and (x2, y2)
         * The angle is measured with 0/360 being the X-axis to the right, angles
         * increase counter clockwise.
         *
         * @param x1 the x position of the first point
         * @param y1 the y position of the first point
         * @param x2 the x position of the second point
         * @param y2 the y position of the second point
         * @return the angle between two points
         */
        public double getAngle(float x1, float y1, float x2, float y2) {

            double rad = Math.atan2(y1-y2,x2-x1) + Math.PI;
            return (rad*180/Math.PI + 180)%360;
        }
    }

    public enum Direction{
        up,
        down,
        left,
        right;

        /**
         * Returns a direction given an angle.
         * Directions are defined as follows:
         *
         * Up: [45, 135]
         * Right: [0,45] and [315, 360]
         * Down: [225, 315]
         * Left: [135, 225]
         *
         * @param angle an angle from 0 to 360 - e
         * @return the direction of an angle
         */
        public static Direction get(double angle){
            if(inRange(angle, 45, 135)){
                return Direction.up;
            }
            else if(inRange(angle, 0,45) || inRange(angle, 315, 360)){
                return Direction.right;
            }
            else if(inRange(angle, 225, 315)){
                return Direction.down;
            }
            else{
                return Direction.left;
            }

        }

        /**
         * @param angle an angle
         * @param init the initial bound
         * @param end the final bound
         * @return returns true if the given angle is in the interval [init, end).
         */
        private static boolean inRange(double angle, float init, float end){
            return (angle >= init) && (angle < end);
        }
    }
}
