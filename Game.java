package dm.com.realblacksmith;

import android.content.Context;
import android.graphics.Canvas;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Game extends SurfaceView implements SurfaceHolder.Callback
{
    private boolean isPaused;
    public Scene scene;

    public GameThread thread;
    public static final int FPS = 30;

    public static final Vector2D friction = new Vector2D(new double[] {25, 25});

    public static final int[] FITTED_SIZE = {178,100};
    public static double[] size = {1920.0f, 1080.0f};
    public static double[] ratio;
    public static Material[] materials;

    public long refCount = 0;

    public void changeScene(Scene s)
    {
        scene = s;
    }

    public float[] getDimensions()
    {
        return new float[]{ getWidth(), getHeight() };
    }

    public Game(Context context)
    {
        super(context);

        getHolder().addCallback(this);
        thread = new GameThread(getHolder(), this);
        setFocusable(true);
    }

    public void setPaused(boolean bPaused)
    {
        isPaused = bPaused;
    }

    public boolean getPaused()
    {
        return isPaused;
    }

    @Override
    public boolean onDragEvent(DragEvent event)
    {
        scene.dragEvent(event);
        return true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        boolean retry = true;
        while (retry)
        {
            try
            {
                thread.setRunning(false);
                thread.join();
            }
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        // RUN BEFORE ANYTHING ELSE
        size = new double[] {getWidth(), getHeight()};
        ratio = new double[] {size[0]/(double)FITTED_SIZE[0], size[1]/(double)FITTED_SIZE[1]};
        /*
        for (int i = 0; i < Material.MAT_LENGTH; i++)
            materials[i] = Material.parseResource(i);
        //*/
        /*
        <color name="steel1040">#696C73</color>
        <color name="steel1050">#64656B</color>
        <color name="steel1060">#5C5E65</color>
        <color name="steel1070">#4D4F56</color>
        <color name="steel1080">#44464C</color>
        <color name="steel_300">#651100</color>
        <color name="steel_400">#AF5505</color>
        <color name="steel_500">#EA6E00</color>
        <color name="steel_600">#FFA812</color>
        <color name="steel_700">#FFCC20</color>
        <color name="steel_800">#FEFBE7</color>
         */
        materials = new Material[Material.MAT_LENGTH];
        materials[0] = new Material();
        materials[0].setHardness(201);
        materials[0].setToughness(100);
        materials[0].setMatTempPatterns(new int[] {
                0xFF696c73, //0
                0xFF696c73, //100
                0xFF696c73, //200
                0xFF651100, //300
                0xFFAF5505, //400
                0xFFEA6E00, //500
                0xFFFFA812, //600
                0xFFFFCC20, //700
                0xFFFEFBE7  //800
        });

        // Test scene
        changeScene(new TestScene());

        // Start the game loop
        thread.setRunning(true);
        thread.start();
        isPaused = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        scene.touchEvent(event);
        return true;
    }

    public void update()
    {
        if (scene != null)
            scene.update();
    }

    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        if (canvas != null && !isPaused)
        {
            if (scene != null)
                scene.draw(canvas);
        }
    }

    public static int[] blockCoordinates(double[] point)
    {
        if (point != null && point.length == 2)
            return new int[] {(int)(Math.round(point[0]/ratio[0])), (int)(Math.round(point[1]/ratio[1]))};
        else
            return new int[2];
    }

    public static double[] pixelCoordinates(int[] point)
    {
        if (point != null && point.length == 2)
            return new double[] {(double)point[0]*ratio[0], (double)point[1]*ratio[1]};
        else
            return new double[2];
    }

    // TODO Create static materials
}
