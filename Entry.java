package dm.com.realblacksmith;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Entry extends AppCompatActivity {

    public static Game game;
    public static boolean debug = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the screen to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Turn the title off
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_entry);

        game = new Game(this);
        setContentView(game);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        game.setPaused(true);
        game.setFocusable(false);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        game.setPaused(false);
        game.setFocusable(true);
    }
}
