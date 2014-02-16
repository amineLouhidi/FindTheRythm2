package com.mmm.findtherythm;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import com.mmm.findtherythm.controller.Controller;
import com.mmm.findtherythm.model.ButtonRythm;
import com.mmm.findtherythm.model.Model;
import com.mmm.findtherythm.model.Observer;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;



public class GameActivity extends Activity implements Observer{
	private static final String TAG = "GameActivity";
	private ImageView background;
	MediaPlayer mMediaPlayer = new MediaPlayer();
	MediaPlayer mMediaPlayer2 = new MediaPlayer();
	MediaPlayer mMediaPlayer3 = new MediaPlayer();
	AnimationDrawable danceAnimation;	
	ArrayList<ImageView> push;
	int score;
	Controller controlleur;
	int timeout1=0;
	Timer T;
	ButtonRythm enable_button;
	private Handler myHandler;
	private Runnable myRunnable = new Runnable() {
	@Override
	public void run() {
	    // Code � �x�cuter de fa�on p�riodique
		controlleur.clickFailAction();
	    myHandler.postDelayed(this,2000);
	    
	    }
	};
	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "start GameActivity");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);	
		configSound();
		mMediaPlayer2.start();
    	//oppaDance();
		
		background = (ImageView) findViewById(R.id.background);
		background.setBackgroundResource(R.drawable.dance_animation);
		danceAnimation = (AnimationDrawable) background.getBackground();
		danceAnimation.setCallback(background);
		danceAnimation.setVisible(true, true);
		
		//cr�ation des boutons
		Log.i(TAG, "cr�ation des boutons");
		push= new ArrayList<ImageView>();
		push.add((ImageView) findViewById(R.id.push1));
		push.add((ImageView) findViewById(R.id.push2));
		push.add((ImageView) findViewById(R.id.push3));
		push.add((ImageView) findViewById(R.id.push4));
		push.add((ImageView) findViewById(R.id.push5));
		
		//Intialisation du controlleur
		Log.i(TAG, "ajout de l'observateur");
		Model m = Factory.getInstance().getModel();
		m.addObserver(this);
		
		
		Log.i(TAG, "ajout du controlleur");
		controlleur = Factory.getInstance().getController();
		if(controlleur == null)
			Log.i(TAG, "controlleur null");
		//affectation des listeners
		for(int i=0; i<push.size(); i++)
			push.get(i).setOnClickListener(pushBouton);
		controlleur.startGameAction();
		
		myHandler = new Handler();
	    myHandler.postDelayed(myRunnable,2000); 
        
	
		}
	
	public void onPause() {
	    super.onPause();
	    if(myHandler != null)
	        myHandler.removeCallbacks(myRunnable); // On arrete le callback
	}
		
	public void oppaDance(){
		final RelativeLayout rl = (RelativeLayout) findViewById(R.id.layoutGame);
        rl.postDelayed(new Runnable() {
            int i = 0;
            public void run() {
            	rl.setBackgroundResource(
                    i++ % 2 == 0 ?
                    		R.drawable.op2 :
                    		R.drawable.op1);
            	rl.postDelayed(this, 500);
            }
        }, 500);
	}
	
	public void configSound(){
		mMediaPlayer = MediaPlayer.create(this, R.raw.error);
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mMediaPlayer.setLooping(false);
		mMediaPlayer.setVolume(100, 100);
		mMediaPlayer3 = MediaPlayer.create(this, R.raw.valid);
		mMediaPlayer3.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mMediaPlayer3.setLooping(false);
		mMediaPlayer3.setVolume(100, 100);
		mMediaPlayer2 = MediaPlayer.create(this, R.raw.sound1);
		mMediaPlayer2.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mMediaPlayer2.setLooping(true);
		mMediaPlayer.setVolume(20, 20);
	}

		
	@Override
	public void onWindowFocusChanged (boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus) {
			danceAnimation.start();
		}
	}

	OnClickListener pushBouton = new OnClickListener() {
	
		@Override
		public void onClick(View boutonPushed) {
			// TODO Auto-generated method stub
			Log.i(TAG, "onClick button");
			for(int i=0; i< push.size(); i++){
				//Je cherche la correpondance du bouton cliqu� dans la liste des views
				if(boutonPushed.getId() == push.get(i).getId())
				{
					//si on clique sur le bon bouton
					if(i == enable_button.getId()){
							controlleur.clickSuccessAction();
						}
					else
							controlleur.clickFailAction();
						
					}
				myHandler.postDelayed(myRunnable,2000);
				break;
			
				}
				
			}
			
					
		

		
	};
	

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}

	@Override
	public void update(Model model) {
		Log.i(TAG, "update");
		updatePush(model);
		updateSound(model);
		updateGraphic(model);
		Log.i(TAG, "fin update");
	}
	
	private void updateGraphic(Model model) {
		
		
	}

	private void updateSound(Model model) {
		// TODO Auto-generated method stub
	
		if (model.getMove())
			mMediaPlayer3.start();
		else
			mMediaPlayer.start();
	}

	private void updatePush(Model model) {
		ArrayList<ButtonRythm> listBouton= model.getButtonRythm();
		for(int i=0; i<listBouton.size(); i++){
			Log.i(TAG, "push("+i+") = "+listBouton.get(i).getState());
			if(listBouton.get(i).getState() == true){
				push.get(i).setImageResource(R.drawable.button_green);
				enable_button = listBouton.get(i);
			}
			else
				push.get(i).setImageResource(R.drawable.button_red);
				//push.get(i).setBackgroundResource(R.drawable.button_red);
			
		}
		score = model.getScore();
	
	}
	
	public void autochange(final ImageView im){
        im.postDelayed(new Runnable() {
            public void run() {
            	im.setImageResource(R.drawable.button_red);
            }
        }, 2000);
	}

}
