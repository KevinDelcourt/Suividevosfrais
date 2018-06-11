package fr.cned.emdsgil.suividevosfrais;


import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.EditText;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Activité de mise à jour de la base de données, après saisie des idnetifiants
 *
 * @author Kevin Delcourt
 */
public class LoginActivity extends AppCompatActivity {

    public static final String ROOT_URL = "http://applifrais.kevin-delcourt.fr/";


    EditText txtUsername, txtPassword;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
        setTitle("GSB : Upload mois en cours");


        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

		cmdValider_clic() ;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_actions, menu);
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals(getString(R.string.retour_accueil))) {
            retourActivityPrincipale() ;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Sur le clic du bouton valider : sérialisation
     */
    private void cmdValider_clic() {
    	findViewById(R.id.cmdLoginValider).setOnClickListener(new Button.OnClickListener() {
    		public void onClick(View v) {

                //On récupère login/mdp
                String login = txtUsername.getText().toString().trim();;
                String mdp = txtPassword.getText().toString().trim();

                HashMap<String, String> params = new HashMap<>();
                params.put("login", login);
                params.put("mdp", mdp);

                Calendar now = Calendar.getInstance();

                int year = now.get(Calendar.YEAR);
                int month = now.get(Calendar.MONTH) + 1;
                int key = year*100 + month;

                String fraisF ="";
                String fraisHF ="";

                if (Global.listFraisMois.containsKey(key)) {
                    //Construction chaine des frais forfaits
                    fraisF += "NUI." +  Global.listFraisMois.get(key).getNuitee() +"/";
                    fraisF += "KM." +  Global.listFraisMois.get(key).getKm() +"/";
                    fraisF += "ETP." +  Global.listFraisMois.get(key).getEtape() +"/";
                    fraisF += "REP." +  Global.listFraisMois.get(key).getRepas() ;


                    //Construction chaine des frais hors forfaits
                    ArrayList<FraisHf> liste = Global.listFraisMois.get(key).getLesFraisHf() ;

                    for(FraisHf frais: liste){
                        fraisHF += frais.toString() + "%";
                    }

                    RestAdapter adapter = new RestAdapter.Builder()
                            .setEndpoint(ROOT_URL) //Setting the Root URL
                            .build(); //Finally building the adapter

                    //Creating object for our interface
                    RegisterAPI api = adapter.create(RegisterAPI.class);

                    //Defining the method insertuser of our interface
                    api.insertUser(

                            //Passing the values by getting it from editTexts
                            login,
                            mdp,
                            fraisF,
                            fraisHF,

                            //Creating an anonymous callback
                            new Callback<Response>() {
                                @Override
                                public void success(Response result, Response response) {
                                    //On success we will read the server's output using bufferedreader
                                    //Creating a bufferedreader object
                                    BufferedReader reader = null;

                                    //An string to store output from the server
                                    String output = "";

                                    try {
                                        //Initializing buffered reader
                                        reader = new BufferedReader(new InputStreamReader(result.getBody().in()));

                                        //Reading the output in the string
                                        output = reader.readLine();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    //Displaying the output as a toast
                                    Toast.makeText(LoginActivity.this, output, Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    //If any error occured displaying the error as toast
                                    Toast.makeText(LoginActivity.this, error.toString(),Toast.LENGTH_LONG).show();
                                }
                            }
                    );



                }

    		}
    	}) ;    	
    }

	/**
	 * Retour à l'activité principale (le menu)
	 */
	private void retourActivityPrincipale() {
		Intent intent = new Intent(LoginActivity.this, MainActivity.class) ;
		startActivity(intent) ;   					
	}


}
