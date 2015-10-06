package com.example.kk070.accelerometer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String LOG_TAG = "AccelDemo";
    private SensorManager sensorManager;
    private SensorView sensorView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorView = new SensorView(this);
        setContentView(sensorView);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensorView.setBackgroundResource(R.drawable.background);
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if(sensors.size()>0){
            sensorManager.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(final Sensor sensor, int accuracy){

    }

    public void onSensorChanged(final SensorEvent event){
        Sensor sensor = event.sensor;

        switch(sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                sensorView.move(event.values[0], event.values[1]);
                break;
            default:
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class SensorView extends View {

        private static final int ROCKET_SIZE = 50;
        private Bitmap rocket;
        private int w;
        private int h;
        private float x;
        private float y;

        public SensorView(Context context){
            super(context) ;
            //requestWindowFeature(Window.FEATURE_NO_TITLE);
            rocket = BitmapFactory.decodeResource(context.getResources(), R.drawable.rocket);
        }


        public void move(float mx, float my){
            this.x -= (mx * 4f);
            this.y += (my * 4f);
            if(this.x <0){
                this.x = 0;
            }else if(this.x + ROCKET_SIZE > this.w){
                this.x = this.w - ROCKET_SIZE;
            }

            if(this.y <0){
                this.y = 0;
            }else if(this.y + ROCKET_SIZE >this.h){
                this.y = this.h - ROCKET_SIZE;
            }
            invalidate();
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh){
            this.w = w;
            this.h = h;
            this.x = (w- ROCKET_SIZE)/2f;
            this.y=(h-ROCKET_SIZE)/2f;
        }

        @Override
        protected void onDraw(Canvas canvas){
            canvas.drawBitmap(rocket, x, y, null);
        }
    }
}
