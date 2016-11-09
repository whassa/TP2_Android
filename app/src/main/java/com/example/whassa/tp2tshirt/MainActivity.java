
/**
 * Auteurs : Rémi
 * Fichier : MainActivity.java
 * Date    : 20 octobre 2016
 * Cours   : 420-254-MO (TP2 Android)
 */

/**
 * La classe principal du tp.
 */
package com.example.whassa.tp2tshirt;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.example.whassa.tp2tshirt.R;
import com.example.whassa.tp2tshirt.TShirt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class MainActivity extends AppCompatActivity {
    private TShirt shirt;
    private RadioGroup sexe;
    private RadioGroup couleur;
    private RadioGroup size;
    private CheckBox text;
    private CheckBox logo;
    private Switch back;
    private Button save;
    private Button restore;
    private Button cancel;
    private Resources rec;
    private ImageView imgChandail;
    private ImageView imgLogoAndroid;
    private ImageView imgTexte;

    /*Methode principale lors de l'exécution du programme.*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shirt = new TShirt();
        setContentView(R.layout.activity_main);
        classCreator();

    }

    /* Methode qui permet de mettre en place le layout quand il est changé */
    public void normalSetUp(){
        text.setChecked(shirt.isTexteTshirt());
        logo.setChecked(shirt.isLogoTshirt());
        back.setChecked(shirt.isArriereTshirt());
        sexe.check(sexe.getChildAt(shirt.getIndexSexe()).getId());
        couleur.check(couleur.getChildAt(shirt.getIndexCouleur()).getId());
        size.check(size.getChildAt(shirt.getIndexTaille()).getId());

    }

    /* Methode qui instancie les variables du layout */
    public void classCreator() {
        sexe = (RadioGroup) findViewById(R.id.Sexe);
        couleur = (RadioGroup) findViewById(R.id.Couleur);
        size = (RadioGroup) findViewById(R.id.Size);
        text = (CheckBox) findViewById(R.id.checkboxImage);
        logo = (CheckBox) findViewById(R.id.logo);
        back = (Switch) findViewById(R.id.switchBack);
        save = (Button) findViewById(R.id.Save);
        restore = (Button) findViewById(R.id.Restore);
        cancel = (Button) findViewById(R.id.Cancel);
        rec = getResources();
        imgChandail = (ImageView)findViewById(R.id.image_tshirt);
        imgLogoAndroid = (ImageView)findViewById(R.id.image_logo);
        imgTexte = (ImageView)findViewById(R.id.image_texte);
        listeners();

    }
    /*  Methode pour configurer les listeners de la classe"*/
    public void listeners(){

        save.setOnClickListener(boutonListener);
        restore.setOnClickListener(boutonListener);
        cancel.setOnClickListener(boutonListener);
        logo.setOnCheckedChangeListener(checkBoxListener);
        text.setOnCheckedChangeListener(checkBoxListener);
        back.setOnCheckedChangeListener(checkBoxListener);
        sexe.setOnCheckedChangeListener(radioGroupListener);
        couleur.setOnCheckedChangeListener(radioGroupListener);
        size.setOnCheckedChangeListener(radioGroupListener);
        normalSetUp();
    }
    /* Methode qui permet de changer les données de l'objet TShirt*/
    public void actualiserTshirt(){
        imgChandail.setImageResource(shirt.obtenirImageTshirt());
        imgChandail.setScaleX(shirt.obtenirEchelleTshirt());
        imgLogoAndroid.setImageResource(shirt.obtenirImageLogo());
        imgLogoAndroid.setScaleX(shirt.obtenirEchelleLogoTexte());
        imgLogoAndroid.setScaleY(shirt.obtenirEchelleLogoTexte());
        imgTexte.setImageResource(shirt.obtenirImageTexte());
        imgTexte.setScaleX(shirt.obtenirEchelleLogoTexte());
        imgTexte.setScaleY(shirt.obtenirEchelleLogoTexte());
        if(shirt.obtenirCouleurTshirt() !=  Color.WHITE){
            imgChandail.setColorFilter(shirt.obtenirCouleurTshirt());
        } else {
            imgChandail.clearColorFilter();
        }


    }

    /*Listener des boutons radio*/
    private RadioGroup.OnCheckedChangeListener radioGroupListener = new RadioGroup.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(RadioGroup group,int checkedId) {

            if(checkedId != -1){
                RadioButton boutonRadio = (RadioButton) group.findViewById(checkedId);
                int index = group.indexOfChild(boutonRadio);
                if(group == sexe){
                    shirt.setIndexSexe(index);
                } else if(group == couleur) {
                    shirt.setIndexCouleur(index);
                } else if(group == size){
                    shirt.setIndexTaille(index);
                }
                actualiserTshirt();
            }
        }

    };
    /*Listener des checkboxs et des switchs */
    private CompoundButton.OnCheckedChangeListener checkBoxListener =  new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged( CompoundButton buttonView,boolean isChecked) {

            if(buttonView == logo){
                if (logo.isChecked() ) {
                    shirt.setLogoTshirt(true);
                } else {
                    shirt.setLogoTshirt(false);
                }
            }
            if(buttonView == text){
                if (text.isChecked()) {
                    shirt.setTexteTshirt(true);
                } else {
                    shirt.setTexteTshirt(false);
                }
            }
            if(buttonView == back){
                if (back.isChecked()) {
                    shirt.setArriereTshirt(true);
                } else {
                    shirt.setArriereTshirt(false);
                }
            }
            actualiserTshirt();
        }

    };



    /* Listener des boutons */
    private View.OnClickListener boutonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            if (v == save) {
                String nomFichier = "chandail";
                File fichier;
                ObjectOutputStream ficSortie = null;
                try {
                    fichier = new File(getFilesDir(),nomFichier);
                    ficSortie = new ObjectOutputStream( new FileOutputStream(fichier));
                    ficSortie.writeObject(shirt);
                    ficSortie.close();
                    Toast toast = Toast.makeText(getApplicationContext(), rec.getString(R.string.saved), Toast.LENGTH_SHORT);
                    toast.show();
                }catch(IOException e){
                    Toast toast = Toast.makeText(getApplicationContext(), rec.getString(R.string.savedFailed), Toast.LENGTH_SHORT);
                    toast.show();
                }
            } else if (v == restore) {
                String nomFichier = "chandail";
                File fichier;
                ObjectInputStream ficEntree = null;
                try {
                    fichier = new File(getFilesDir(),nomFichier);
                    ficEntree = new ObjectInputStream(new FileInputStream(fichier));
                    shirt = (TShirt) ficEntree.readObject();
                    ficEntree.close();
                    Toast toast = Toast.makeText(getApplicationContext(), rec.getString(R.string.restore), Toast.LENGTH_SHORT);
                    toast.show();
                }catch(IOException e){
                    Toast toast = Toast.makeText(getApplicationContext(), rec.getString(R.string.restoredFailed), Toast.LENGTH_SHORT);
                    toast.show();
                } catch (ClassNotFoundException e) {
                    Toast toast = Toast.makeText(getApplicationContext(), rec.getString(R.string.restoredFailed), Toast.LENGTH_SHORT);
                    toast.show();
                }
                normalSetUp();
            } else if (v == cancel) {

                shirt.mettreValeursDefaut();
                Toast toast = Toast.makeText(getApplicationContext(), rec.getString(R.string.cancel), Toast.LENGTH_SHORT);
                toast.show();
                Log.d("shirt","Marche toujours pas ? "+shirt.isTexteTshirt()+shirt.isLogoTshirt()+shirt.isArriereTshirt());
                normalSetUp();
                Log.d("shirt","Marche toujours pas ? "+shirt.isTexteTshirt()+shirt.isLogoTshirt()+shirt.isArriereTshirt());
            }

        }
    };

}
