package ipn.escom.ea.concatena.sensordetemperatura;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

public class verTemperatura extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_temperatura);

        final Button btnKelvin = findViewById(R.id.btnKelvin);
        final Button btnCelsius = findViewById(R.id.btnCelsius);
        final Button btnFarenheit = findViewById(R.id.btnFarenheit);



        btnCelsius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCelsius.setBackgroundResource(R.drawable.round_button_selected);
                btnFarenheit.setBackgroundResource(R.drawable.round_button);
                btnKelvin.setBackgroundResource(R.drawable.round_button);
            }
        });

        btnFarenheit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCelsius.setBackgroundResource(R.drawable.round_button);
                btnFarenheit.setBackgroundResource(R.drawable.round_button_selected);
                btnKelvin.setBackgroundResource(R.drawable.round_button);
            }
        });

        btnKelvin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCelsius.setBackgroundResource(R.drawable.round_button);
                btnFarenheit.setBackgroundResource(R.drawable.round_button);
                btnKelvin.setBackgroundResource(R.drawable.round_button_selected);
            }
        });
    }
}


class Circle extends View {

    private Paint mCircleYellow;
    private Paint mCircleGray;

    private float mRadius;
    private RectF mArcBounds = new RectF();

    public Circle(Context context) {
        super(context);

        // create the Paint and set its color

    }

    public Circle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initPaints();
    }

    public Circle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private void initPaints() {
        mCircleYellow = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleYellow.setStyle(Paint.Style.FILL);
        mCircleYellow.setColor(Color.YELLOW);
        mCircleYellow.setStyle(Paint.Style.STROKE);
        mCircleYellow.setStrokeWidth(15 * getResources().getDisplayMetrics().density);
        mCircleYellow.setStrokeCap(Paint.Cap.SQUARE);
        // mEyeAndMouthPaint.setColor(getResources().getColor(R.color.colorAccent));
        mCircleYellow.setColor(Color.parseColor("#F9A61A"));

        mCircleGray = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleGray.setStyle(Paint.Style.FILL);
        mCircleGray.setColor(Color.GRAY);
        mCircleGray.setStyle(Paint.Style.STROKE);
        mCircleGray.setStrokeWidth(15 * getResources().getDisplayMetrics().density);
        mCircleGray.setStrokeCap(Paint.Cap.SQUARE);
        // mEyeAndMouthPaint.setColor(getResources().getColor(R.color.colorAccent));
        mCircleGray.setColor(Color.parseColor("#76787a"));

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mRadius = Math.min(w, h) / 2f;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);

        int size = Math.min(w, h);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Float drawUpto = 46f;


        float mouthInset = mRadius / 3f;
        mArcBounds.set(mouthInset, mouthInset, mRadius * 2 - mouthInset, mRadius * 2 - mouthInset);
        canvas.drawArc(mArcBounds, 0f, 360f, false, mCircleGray);

        canvas.drawArc(mArcBounds, 270f, drawUpto, false, mCircleYellow);


    }
}
