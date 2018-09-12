package com.andy.note.Game1.View;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.andy.note.R;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by andy on 2018/8/21.
 */

public class Game1ViewGroup extends RelativeLayout {
    //游戏元素数组
    private MyImageView gameItem[][];
    //当局游戏所有元素中的图片资源集合
    private int itemRes[];
    //子view宽度/长度
    private int childWidth;
    //子view边距
    private int margin = 5;
    //第一列的起始X坐标
    private float startX;
    //第一行的起始Y坐标
    private float startY;
    //是否点击第一个view
    private boolean isFirst = true;
    //down事件时对应的行号
    private int downRow;
    //down事件时对应的列号
    private int downLine;
    //up事件对应的行号
    private int upRow;
    //up事件对应的列号
    private int upLine;
    //第一次点击成功后对应的行号
    private int firstRow;
    //第一次点击成功后对应的列号
    private int firstLine;
    //路径矩阵 1表示该位置存在元素 0表示不存在元素
    private int road[][];
    //第一拐点x
    private int x1 = -1;
    //第一拐点y
    private int y1 = -1;
    //第二拐点x
    private int x2 = -1;
    //第二拐点y
    private int y2 = -1;
    //是否第一次创建
    private boolean isOnce = true;
    //连线点之间的路径的点集合
    private ArrayList<Point> pointsList;
    //画笔
    private Paint paint;
    //是否顺序添加拐点
    private boolean b = false;
    //每一个元素对应位置图片资源的数组
    private int gameRes[][];
    //时间进度条
    private TimeView timeView;
    //计时器
    private Timer timer;
    //计时器是否暂停
    private boolean isPause = false;
    //游戏难度图片资源数组  从三个难度资源数组中选取而得 为itemRes提供随机资源的源材料
    private int picRes[];
    //游戏难度
    private int range = 1;
    //图片总数
    private int picNum[] = new int[]{12, 24, 36};
    //消除数
    private int deleteCount = 0;
    //设置分数、难度文字的外边距接口的引用
    private ITextMargin iTextMargin;
    //游戏控制接口
    private IGameControl gameControl;
    //分数
    private int score = 0;
    //难度一图片集
    private int rangeOnePic[] = new int[]{R.drawable.img1_1, R.drawable.img1_2, R.drawable.img1_3,
            R.drawable.img1_4, R.drawable.img1_5, R.drawable.img1_6, R.drawable.img1_7,
            R.drawable.img1_8, R.drawable.img1_9, R.drawable.img1_10, R.drawable.img1_11,
            R.drawable.img1_12};
    //难度二图片集
    private int rangeTwoPic[] = new int[]{R.drawable.img2_1, R.drawable.img2_2, R.drawable.img2_3,
            R.drawable.img2_4, R.drawable.img2_5, R.drawable.img2_6, R.drawable.img2_7,
            R.drawable.img2_8, R.drawable.img2_9, R.drawable.img2_10, R.drawable.img2_11,
            R.drawable.img2_12, R.drawable.img2_13, R.drawable.img2_14, R.drawable.img2_15,
            R.drawable.img2_16, R.drawable.img2_17, R.drawable.img2_18, R.drawable.img2_19,
            R.drawable.img2_20, R.drawable.img2_21, R.drawable.img2_22, R.drawable.img2_23,
            R.drawable.img2_24};
    //难度三图片集
    private int rangeThreePic[] = new int[]{R.drawable.img3_1, R.drawable.img3_2, R.drawable.img3_3,
            R.drawable.img3_4, R.drawable.img3_5, R.drawable.img3_6, R.drawable.img3_7,
            R.drawable.img3_8, R.drawable.img3_9, R.drawable.img3_10, R.drawable.img3_11,
            R.drawable.img3_12, R.drawable.img3_13, R.drawable.img3_14, R.drawable.img3_15,
            R.drawable.img3_16, R.drawable.img3_17, R.drawable.img3_18, R.drawable.img3_19,
            R.drawable.img3_20, R.drawable.img3_21, R.drawable.img3_22, R.drawable.img3_23,
            R.drawable.img3_24, R.drawable.img3_25, R.drawable.img3_26, R.drawable.img3_27,
            R.drawable.img3_28, R.drawable.img3_29, R.drawable.img3_30, R.drawable.img3_31,
            R.drawable.img3_32, R.drawable.img3_33, R.drawable.img3_34, R.drawable.img3_35,
            R.drawable.img3_36};
    private static String LOG_TAG = "Game1ViewGroup";
    //handler
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    road[firstRow][firstLine] = 0;
                    road[upRow][upLine] = 0;
                    gameItem[firstRow][firstLine].setIsDrawBorder(false);
                    gameItem[upRow][upLine].setIsDrawBorder(false);
                    gameItem[firstRow][firstLine].setVisibility(View.INVISIBLE);
                    gameItem[upRow][upLine].setVisibility(View.INVISIBLE);
                    timeView.addTime();
                    deleteCount++;
                    score += 100;
                    gameControl.scoreChanged(score);
                    Log.e(LOG_TAG, "deleteCount = " + deleteCount);
                    //如果消除数达到72 即全部消除 则进入下一等级难度 重新生成布局
                    //计时器时间不变 继续计时
                    if (deleteCount == 72) {
                        if (range != 3) {
                            range++;
                            gameControl.rangeChanged(range);
                            timeView.setRange(range);
                        }
                        init();
                        reDraw();
                        deleteCount = 0;
                    }
                    startTimer();
                    break;
                case 1:
                    timeView.invalidate();
                    //游戏结束
                    if (timeView.getTime() <= 0) {
                        gameControl.gameOver();
                        pauseTimer();
                    }
                    break;
            }
        }
    };

    public Game1ViewGroup(Context context) {
        this(context, null);
        Log.e(LOG_TAG, "7777");
    }

    public Game1ViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        Log.e(LOG_TAG, "8888");
    }

    public Game1ViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setBackgroundColor(Color.parseColor("#00000000"));

        gameItem = new MyImageView[14][14];

        timeView = new TimeView(getContext());
        init();
    }

    private void init() {
        //初始化游戏项目
        switch (range) {
            case 1:
                picRes = rangeOnePic;
                break;
            case 2:
                picRes = rangeTwoPic;
                break;
            case 3:
                picRes = rangeThreePic;
                break;
        }

        itemRes = new int[144];
        for (int i = 0; i < 144; i = i + 2) {
            Random rand = new Random();
            int temp = rand.nextInt(picNum[range - 1]);
            //图片成对出现以便消除
            itemRes[i] = picRes[temp];
            itemRes[i + 1] = picRes[temp];
        }
        pointsList = new ArrayList<>();
        road = new int[14][14];
        for (int i = 0; i < 14; i++) {
            for (int j = 0; j < 14; j++) {
                if (i == 0 || j == 0 || i == 13 || j == 13) {
                    road[i][j] = 0;
                } else {
                    road[i][j] = 1;
                }
            }
        }
        gameRes = new int[14][14];
        for (int i = 1; i < 13; i++) {
            for (int j = 1; j < 13; j++) {
                if (gameItem[i][j] == null) {
                    gameItem[i][j] = new MyImageView(getContext());
                }
                Random rand = new Random();
                gameItem[i][j].setVisibility(View.VISIBLE);
                //将所有元素都使用完
                int t = rand.nextInt(144);
                while (itemRes[t] == 99999) {
                    t = rand.nextInt(144);
                }
                gameItem[i][j].setImageBitmap(BitmapFactory.decodeResource(getResources(), itemRes[t]));
                gameRes[i][j] = itemRes[t];
                gameItem[i][j].setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                //使用后将其值设置为99999,避免再次被使用
                itemRes[t] = 99999;
            }
        }
    }

    /**
     * 开始计时
     */
    private void startTimer() {
        timer = new Timer();
        isPause = false;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timeView.subTime();
                handler.sendEmptyMessage(1);
            }
        }, 0, 1000);
    }

    /**
     * 暂停计时
     */
    private void pauseTimer() {
        if (!isPause) {
            timer.cancel();
            isPause = true;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //Log.e(LOG_TAG, "width = " + getMeasuredWidth() + "," + getWidth() + ",height =" + getMeasuredHeight() + "," + getHeight());
        //保存子view的宽度
        childWidth = (getMeasuredWidth() - 5 * 13) / 14;
        if (isOnce) {
            //添加子view
            iTextMargin.setTextMargin(childWidth + margin);
            for (int i = 1; i < 13; i++) {
                for (int j = 1; j < 13; j++) {
                    gameItem[i][j].setId(i * 12 + j + 1);
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(childWidth, childWidth);
                    //不是第一行
                    if (i != 1) {
                        lp.topMargin = margin;
                        lp.addRule(RelativeLayout.BELOW, gameItem[i - 1][j].getId());
                    } else {
                        lp.topMargin = margin + childWidth;
                    }
                    if (i == 12) {
                        lp.bottomMargin = margin + childWidth;
                    }
                    //不是最后一列
                    if (j != 12) {
                        lp.rightMargin = margin;
                    }
                    //不是第一列
                    if (j != 1) {
                        lp.addRule(RelativeLayout.RIGHT_OF, gameItem[i][j - 1].getId());
                    } else {
                        lp.leftMargin = childWidth + margin;
                    }
                    addView(gameItem[i][j], lp);
                }
            }
            startX = margin + childWidth;
            startY = margin + childWidth;

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(childWidth * 12 + 11 * margin, margin * 4);
            params.addRule(RelativeLayout.BELOW, gameItem[12][1].getId());
            params.topMargin = margin;
            params.leftMargin = childWidth + margin;
            addView(timeView, params);
            isOnce = false;
            //第一次测量结束后开始计时
            startTimer();
        }
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //记录down时的坐标，判断出是点击的哪个view
                //保存此view的坐标
                downRow = calculate(event.getY());
                downLine = calculate(event.getX());
                //Log.e(LOG_TAG, "---Action-Down:" + downRow + "," + downLine);
                break;
            case MotionEvent.ACTION_UP:
                //首先记录up时的坐标，判断是哪个view
                //将此view与down中保存view的坐标进行对比，判断是否两个事件处于同一个view中
                //若处于同一个view中，则说明在该view中发生了点击事件
                //反之，则说明在一个view按下后，滑动至另一个view才松开，不算点击事件
                //判断所点击的view是否处于可视状态，若可视则继续操作
                //若点击第一个view，则保存其坐标位置，重置标志位为false
                //若点击第二个view，则开始判断是否能与第一个view相消除
                upRow = calculate(event.getY());
                upLine = calculate(event.getX());
                if (upRow != -1 && upLine != -1) {
                    //Log.e(LOG_TAG, "---Action-Up:" + upRow + "," + upLine);
                    //若发生点击
                    if (upRow == downRow && upLine == downLine && gameItem[upRow][upLine].getVisibility() == View.VISIBLE) {
                        gameItem[upRow][upLine].setIsDrawBorder(true);
                        gameItem[upRow][upLine].invalidate();
                        if (isFirst) {
                            firstLine = upLine;
                            firstRow = upRow;
                        } else if (!(firstRow == upRow && firstLine == upLine)) {
                            if (gameRes[firstRow][firstLine] == gameRes[upRow][upLine]) {
                                //判断是否能消除
                                if (oneLine(firstRow, firstLine, upRow, upLine)) {
                                    //Log.e(LOG_TAG, "能一线消除");
                                    pointsList = new ArrayList<>();
                                    deleteOneLine(firstRow, firstLine, upRow, upLine);
                                    //强制重绘
                                    reDraw();
                                    //暂停计时器
                                    pauseTimer();
                                    //延时0.3秒后取消画线和选中状态，并开始计时器
                                    handler.sendEmptyMessageDelayed(0, 300);
                                } else if (twoLines(firstRow, firstLine, upRow, upLine)) {
                                    // Log.e(LOG_TAG, "能两线消除，拐点坐标为：x=" + this.x1 + ",y=" + this.y1);
                                    pointsList = new ArrayList<>();
                                    deleteTwoLine(firstRow, firstLine, upRow, upLine, this.x1, this.y1);
                                    reDraw();
                                    pauseTimer();
                                    handler.sendEmptyMessageDelayed(0, 300);
                                } else if (threeLines(firstRow, firstLine, upRow, upLine)) {
                                    //Log.e(LOG_TAG, "能三线消除，拐点坐标为：x1=" + this.x1 + ",y1=" + this.y1 + ",x2=" + this.x2 + ",y2=" + this.y2);
                                    pointsList = new ArrayList<>();
                                    deleteThreeLine(firstRow, firstLine, upRow, upLine, this.x1, this.y1, this.x2, this.y2);
                                    reDraw();
                                    pauseTimer();
                                    handler.sendEmptyMessageDelayed(0, 300);
                                } else {
                                    //Log.e(LOG_TAG, "不能消除");
                                    gameItem[upRow][upLine].setIsDrawBorder(false);
                                    gameItem[firstRow][firstLine].setIsDrawBorder(false);
                                    gameItem[upRow][upLine].invalidate();
                                    gameItem[firstRow][firstLine].invalidate();
                                }
                            } else {
                                //两张图片不同，无法消除，则取消选中
                                gameItem[upRow][upLine].setIsDrawBorder(false);
                                gameItem[firstRow][firstLine].setIsDrawBorder(false);
                                gameItem[upRow][upLine].invalidate();
                                gameItem[firstRow][firstLine].invalidate();
                            }
                        } else {
                            //则取消选定
                            gameItem[upRow][upLine].setIsDrawBorder(false);
                            gameItem[upRow][upLine].invalidate();
                        }
                        isFirst = !isFirst;
                    }
                }
                break;
        }

        return true;
    }

    /**
     * 强制重绘
     */
    private void reDraw() {
        invalidate();
        forceLayout();
        requestLayout();
    }

    /**
     * 添加一线消除中的拐点起点与终点
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    private void deleteOneLine(int x1, int y1, int x2, int y2) {
        //添加两个待消除的点
        pointsList.add(new Point(x1, y1));
        pointsList.add(new Point(x2, y2));
    }

    /**
     * 添加二线消除中的拐点终点与起点
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3 拐点
     * @param y3
     */
    private void deleteTwoLine(int x1, int y1, int x2, int y2, int x3, int y3) {
        pointsList.add(new Point(x1, y1));
        pointsList.add(new Point(x3, y3));
        pointsList.add(new Point(x2, y2));
    }

    /**
     * 添加三线消除中的点
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @param x4
     * @param y4
     */
    private void deleteThreeLine(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        pointsList.add(new Point(x1, y1));
        if (b) {
            pointsList.add(new Point(x3, y3));
            pointsList.add(new Point(x4, y4));
        } else {
            pointsList.add(new Point(x4, y4));
            pointsList.add(new Point(x3, y3));
        }
        pointsList.add(new Point(x2, y2));
    }

    /**
     * 是否能三线联通
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    private boolean threeLines(int x1, int y1, int x2, int y2) {
        boolean res = false;
        for (int x3 = 0; x3 < 14; x3++) {
            for (int y3 = 0; y3 < 14; y3++) {
                //循环遍历 找到能三线联通的点
                //排除目标两个点
                if ((x3 != x1 && y3 != y1) || (x3 != x2 && y3 != y2)) {
                    //首先要求拐点能通
                    if (road[x3][y3] == 0 && twoLines(x1, y1, x3, y3) && oneLine(x2, y2, x3, y3)) {
                        this.x2 = x3;
                        this.y2 = y3;
                        res = true;
                        b = true;
                        return res;
                    }
                    if (road[x3][y3] == 0 && twoLines(x2, y2, x3, y3) && oneLine(x1, y1, x3, y3)) {
                        this.x2 = x3;
                        this.y2 = y3;
                        res = true;
                        b = false;
                        return res;
                    }
                }
            }
        }
        return res;
    }

    /**
     * 判断是否可以两线联通
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    private boolean twoLines(int x1, int y1, int x2, int y2) {
        boolean res = true;
        //两个拐点
        int x3 = x1, y3 = y2;
        int x4 = x2, y4 = y1;
        //默认点击的两个点都是能通的
        //先确定拐点上没有元素 再判断是否能通过拐点完成一线联通
        if (road[x3][y3] == 0 && oneLine(x1, y1, x3, y3) && oneLine(x2, y2, x3, y3)) {
            this.x1 = x3;
            this.y1 = y3;
        } else if (road[x4][y4] == 0 && oneLine(x1, y1, x4, y4) && oneLine(x2, y2, x4, y4)) {
            this.x1 = x4;
            this.y1 = y4;
        } else {//若两个拐点都不能一线联通，则无法完成两线联通
            res = false;
        }
        return res;
    }

    /**
     * 判断是否能一线联通 不包括两端的点 只判断两点连线之间的点是否被占据
     *
     * @param x1 点1的x坐标
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    private boolean oneLine(int x1, int y1, int x2, int y2) {
        boolean res = true;
        // Log.e(LOG_TAG,"in oneLine x1 = "+x1+",y1 = "+y1+",x2 = "+x2+",y2= "+y2);
        if (x1 == x2) {
            if (Math.abs(y1 - y2) == 1) {
                res = true;
            } else {
                for (int t = Math.min(y1, y2) + 1; t < Math.max(y1, y2); t++) {
                    if (road[x1][t] == 1) {
                        //如果连线中出现障碍，则返回false
                        res = false;
                        break;
                    }
                }
            }
        } else if (y1 == y2) {
            if (Math.abs(x1 - x2) == 1) {
                res = true;
            } else {
                for (int t = Math.min(x1, x2) + 1; t < Math.max(x1, x2); t++) {
                    if (road[t][y1] == 1) {
                        //如果连线中出现障碍，则返回false
                        res = false;
                        break;
                    }
                }
            }
        } else {
            res = false;
        }
        return res;
    }

    /**
     * 返回此坐标所在行或者所在列
     *
     * @param x 触摸点的x坐标或者y坐标
     * @return
     */
    private int calculate(float x) {
        int t;
        //Log.e(LOG_TAG,"---calculate: x="+x+"---startX ="+startX+",childWidth = "+childWidth+",margin = "+margin+"==="+((int) (x - startX))+"++++"+34%77);
        t = ((int) (x - startX)) / (childWidth + margin);
        int p = ((int) (x - startX)) % (childWidth + margin);
        //判断是点击在view内还是在view的右/下边距上
        if (p <= childWidth) {
            return t + 1;
        } else {
            return -1;
        }
    }

    /**
     * 拦截子view的事件
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    public void setViewsImage() {
        for (int i = 1; i < 13; i++) {
            for (int j = 1; j < 13; j++) {
                if (road[i][j] == 0) {
                    gameItem[i][j].setVisibility(View.INVISIBLE);
                }
                gameItem[i][j].setImageResource(gameRes[i][j]);
            }
        }
        reDraw();
    }

    class Point {
        public int x;
        public int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5.0f);
        for (int i = 0; i < pointsList.size() - 1; i++) {
            int x1 = (pointsList.get(i).y) * (childWidth + margin) + childWidth / 2;
            int y1 = (pointsList.get(i).x) * (childWidth + margin) + childWidth / 2;
            int x2 = (pointsList.get(i + 1).y) * (childWidth + margin) + childWidth / 2;
            int y2 = (pointsList.get(i + 1).x) * (childWidth + margin) + childWidth / 2;
            //Log.e(LOG_TAG, "x1=" + x1 + ",y1=" + y2 + ",x2=" + x2 + ",y2=" + y2);
            canvas.drawLine(x1, y1, x2, y2, paint);
        }
        pointsList.clear();
    }

    public void setITextMargin(ITextMargin iTextMargin) {
        this.iTextMargin = iTextMargin;
    }

    public interface ITextMargin {
        void setTextMargin(int margin);
    }

    public interface IGameControl {
        void scoreChanged(int score);

        void gameOver();

        void rangeChanged(int range);
    }

    public void setIGameControl(IGameControl gameControl) {
        this.gameControl = gameControl;
    }

    public int getScore() {
        return score;
    }

    public int[][] getRoad() {
        return road;
    }

    public int[][] getGameRes() {
        return gameRes;
    }

    public int getRange() {
        return range;
    }

    public int getDeleteCount() {
        return deleteCount;
    }

    public int getTime() {
        return timeView.getTime();
    }

    public void setRoad(int[][] road) {
        this.road = road;
    }

    public void setGameRes(int[][] gameRes) {
        this.gameRes = gameRes;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public void setDeleteCount(int deleteCount) {
        this.deleteCount = deleteCount;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setTime(int time) {
        timeView.setTime(time);
    }
}
