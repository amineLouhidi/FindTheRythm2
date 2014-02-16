package com.mmm.findtherythm;

//import java.util.Timer;



import java.util.ArrayList;
import java.util.Timer;
import com.mmm.findtherythm.controller.Controller;
import com.mmm.findtherythm.model.ButtonRythm;
import com.mmm.findtherythm.model.Model;
import com.mmm.findtherythm.model.Observer;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class GameActivity extends Activity implements Observer{
	private static final String TAG = "GameActivity";
	private ImageView background;
	AnimationDrawable danceAnimation;	
	ArrayList<ImageView> push;
	int score;
	Controller controlleur;
	Timer timeout1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "start GameActivity");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
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
		//Initialisation du score 
		//score = 0;
		
		//cr�ation des timeout
		//timeout1 = new Timer();
		controlleur.startGameAction();
		
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
		public void onClick(View push) {
			// TODO Auto-generated method stub
			Log.i(TAG, "onClick button");
			controlleur.clickSuccessAction();
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
		
	}

	private void updatePush(Model model) {
		ArrayList<ButtonRythm> listBouton= model.getButtonRythm();
		for(int i=0; i<listBouton.size(); i++){
			Log.i(TAG, "push("+i+") = "+listBouton.get(i).getState());
			if(listBouton.get(i).getState() == true)
				push.get(i).setImageResource(R.drawable.button_green);
				//push.get(i).setBackgroundResource(R.drawable.button_green);
			else
				push.get(i).setImageResource(R.drawable.button_red);
				//push.get(i).setBackgroundResource(R.drawable.button_red);
		}
		score = model.getScore();
	}

}
