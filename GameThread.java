package dm.com.realblacksmith;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread
{
    private double debugFPSavg;
    private SurfaceHolder holder;
    private Game game;
    private boolean running;
    public static Canvas canvas;

    public GameThread(SurfaceHolder surfaceHolder, Game ngame)
    {
        super();
        holder = surfaceHolder;
        game = ngame;
    }

    @Override
    public void run()
    {
        long start;
        long total = 0;
        int count = 0;

        while(running)
        {
            if (!game.getPaused())
            {
                start = System.nanoTime();
                canvas = null;

                try {
                    canvas = holder.lockCanvas();
                    synchronized (holder) {
                        game.update();
                        game.draw(canvas);
                    }
                } catch (Exception e) {
                } finally {
                    holder.unlockCanvasAndPost(canvas);
                }
                rest(1000/Game.FPS-(System.nanoTime()-start)/1000000);

                total += System.nanoTime()-start;
                count++;
                if (count >= Game.FPS)
                {
                    debugFPSavg = 1000000000/(total/Game.FPS);
                    count = 0;
                    total = 0;
                }

            }
        }
    }

    public void setRunning(boolean bRun)
    {
        running = bRun;
    }

    public void rest(long time)
    {
        if (time > 0)
        {
            try {
                this.sleep(time);
            } catch (InterruptedException ex) {

            }
        }
    }
}
