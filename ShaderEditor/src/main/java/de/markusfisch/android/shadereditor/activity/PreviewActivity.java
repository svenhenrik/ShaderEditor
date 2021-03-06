package de.markusfisch.android.shadereditor.activity;

import de.markusfisch.android.shadereditor.widget.ShaderView;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

public class PreviewActivity extends AppCompatActivity
{
	public static final String FRAGMENT_SHADER = "fragment_shader";
	public static byte thumbnail[];

	private ShaderView shaderView;
	private Runnable thumbnailRunnable =
		new Runnable()
		{
			@Override
			public void run()
			{
				if( shaderView == null )
					return;

				thumbnail = shaderView
					.getRenderer()
					.getThumbnail();
			}
		};

	@Override
	public void onCreate( Bundle state )
	{
		super.onCreate( state );

		String fragmentShader = getIntent().getStringExtra(
			FRAGMENT_SHADER );

		if( fragmentShader == null )
		{
			finish();
			return;
		}

		shaderView = new ShaderView( this );
		shaderView.setFragmentShader( fragmentShader );

		setContentView( shaderView );

		initStatusBar();
	}

	@Override
	public void onResume()
	{
		super.onResume();

		shaderView.onResume();

		thumbnail = null;
		shaderView.postDelayed(
			thumbnailRunnable,
			500 );
	}

	@Override
	public void onPause()
	{
		super.onPause();

		shaderView.onPause();
	}

	@TargetApi( 22 )
	private void initStatusBar()
	{
		// below status bar settings are available
		// from Lollipop on only
		if( Build.VERSION.SDK_INT <
			Build.VERSION_CODES.LOLLIPOP )
			return;

		Window window = getWindow();

		window.getDecorView().setSystemUiVisibility(
			View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
			View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN );
		window.setStatusBarColor( 0 );
	}
}
