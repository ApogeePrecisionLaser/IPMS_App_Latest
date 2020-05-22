package com.jpss.ipmsintegratedpandemicmanagementsystem.Other;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jpss.ipmsintegratedpandemicmanagementsystem.R;

public class FAQ extends AppCompatActivity implements View.OnClickListener{
RelativeLayout card1,card2,card3,card4,card5,card6,card7,card8;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        card1=findViewById(R.id.Cardview1);
        card2=findViewById(R.id.Cardview2);
        card3=findViewById(R.id.cardview3);
        card4=findViewById(R.id.Cardview4);
        card5=findViewById(R.id.cardview5);
        card6=findViewById(R.id.Cardview6);
        card7=findViewById(R.id.cardview7);
        card8=findViewById(R.id.cardview8);
        card1.setOnClickListener(this);
        card4.setOnClickListener(this);
        card3.setOnClickListener(this);
        card2.setOnClickListener(this);
        card5.setOnClickListener(this);
        card6.setOnClickListener(this);
        card7.setOnClickListener(this);
        card8.setOnClickListener(this);

    }
   @Override
   public void onClick(View v){
       switch (v.getId()){


           case R.id.Cardview1:
               demo();
               break;
           case R.id.Cardview2:
               demo1();
               break;
           case R.id.cardview3:
               demo2();
               break;
           case R.id.Cardview4:
               demo3();
               break;
           case R.id.cardview5:
               demo4();
               break;
           case R.id.Cardview6:
               demo5();
               break;
           case R.id.cardview7:
               demo6();
               break;
           case R.id.cardview8:
               demo7();
               break;
       }
   }
    private void demo() {
        final Dialog dialog;

        dialog = new Dialog(FAQ.this);
        dialog.setContentView(R.layout.activity_questionanswer);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setAttributes(layoutParams);
        TextView txt=dialog.findViewById(R.id.one);
        TextView txt1=dialog.findViewById(R.id.question);
        txt1.setText("What is Corona Virus?");
        txt.setText("Coronaviruses are a large family of viruses which may cause illness in animals or humans. In humans, several coronaviruses are known to cause respiratory\n" +
                " infections ranging from the common cold to more severe diseases such as Middle East Respiratory Syndrome (MERS) and Severe Acute Respiratory Syndrome (SARS)\n" +
                ". The most recently discovered coronavirus causes coronavirus disease COVID-19.\n");
//        Button cancel = dialog.findViewById(R.id.cancel1);
//
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });

        dialog.show();
    }
    private void demo1() {
        final Dialog dialog;

        dialog = new Dialog(FAQ.this);
        dialog.setContentView(R.layout.activity_questionanswer);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setAttributes(layoutParams);
        TextView txt=dialog.findViewById(R.id.one);
        TextView txt1=dialog.findViewById(R.id.question);
        txt1.setText("2.What is COVID-19?");
        txt.setText("COVID-19 is the infectious disease caused by the most recently discovered coronavirus. This new virus and disease were unknown before the outbreak began\n" +
                " in Wuhan, China, in December 2019. COVID-19 is now a pandemic affecting many countries globally.\n. ");
//        Button cancel = dialog.findViewById(R.id.cancel1);
//
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });

        dialog.show();
    }
    private void demo2() {
        final Dialog dialog;

        dialog = new Dialog(FAQ.this);
        dialog.setContentView(R.layout.activity_questionanswer);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setAttributes(layoutParams);
        TextView txt=dialog.findViewById(R.id.one);
        TextView txt1=dialog.findViewById(R.id.question);
        txt1.setText("What are the symptoms of COVID-19?");
        txt.setText("The most common symptoms of COVID-19 are fever, dry cough, and tiredness. Some patients may have aches and pains, nasal congestion, sore throat or diarrhea.\n" +
                " These symptoms are usually mild and begin gradually. Some people become infected but only have very mild symptoms. Most people (about 80%) recover from the \n" +
                "disease without needing hospital treatment. Around 1 out of every 5 people who gets COVID-19 becomes seriously ill and develops difficulty breathing. Older\n" +
                "people, and those with underlying medical problems like high blood pressure, heart and lung problems, diabetes, or cancer , are at higher risk of developing\n" +
                "serious illness. However anyone can catch COVID-19 and become seriously ill. Even people with very mild symptoms of COVID-19 can transmit the virus. People\n" +
                "of all ages who experience fever, cough and difficulty breathing should seek medical attention.");
//
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });

        dialog.show();
    }
    private void demo3() {
        final Dialog dialog;

        dialog = new Dialog(FAQ.this);
        dialog.setContentView(R.layout.activity_questionanswer);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setAttributes(layoutParams);
        TextView txt=dialog.findViewById(R.id.one);
        TextView txt1=dialog.findViewById(R.id.question);
        txt1.setText("What should I do if I have COVID-19 symptoms and when should I seek medical care?");
        txt.setText("If you have minor symptoms, such as a slight cough or a mild fever, there is generally no need to seek medical care. Stay at home, self-isolate and monitor\n" +
                "your symptoms. Follow national guidance on self-isolation.However, if you live in an area with malaria or dengue fever it is important that you do not ignore\n" +
                "symptoms of fever.  Seek medical help.  When you attend the health facility wear a mask if possible, keep at least 1 metre distance from other people and do\n" +
                "not touch surfaces with your hands. If it is a child who is sick help the child stick to this advice.Seek immediate medical care if you have difficulty breathing \n" +
                "or pain/pressure in the chest. If possible, call your health care provider in advance, so he/she can direct you to the right health facility.\n");
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });

        dialog.show();
    }
    private void demo4() {
        final Dialog dialog;

        dialog = new Dialog(FAQ.this);
        dialog.setContentView(R.layout.activity_questionanswer);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setAttributes(layoutParams);
        TextView txt=dialog.findViewById(R.id.one);
        TextView txt1=dialog.findViewById(R.id.question);
        txt1.setText("How does COVID-19 spread?");
        txt.setText("People can catch COVID-19 from others who have the virus. The disease spreads primarily from person to person through small droplets from the nose or mouth,\n" +
                "which are expelled when a person with COVID-19 coughs, sneezes, or speaks. These droplets are relatively heavy, do not travel far and quickly sink to the\n" +
                "ground. People can catch COVID-19 if they breathe in these droplets from a person infected with the virus. This is why it is important to stay at least 1\n" +
                "metre (3 feet) away from others. These droplets can land on objects and surfaces around the person such as tables, doorknobs and handrails. People can become\n" +
                "infected by touching these objects or surfaces, then touching their eyes, nose or mouth. This is why it is important to wash your hands regularly with soap \n" +
                "and water or clean with alcohol-based hand rub.");
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });

        dialog.show();
    }
    private void demo5() {
        final Dialog dialog;

        dialog = new Dialog(FAQ.this);
        dialog.setContentView(R.layout.activity_questionanswer);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setAttributes(layoutParams);
        TextView txt=dialog.findViewById(R.id.one);
        TextView txt1=dialog.findViewById(R.id.question);
        txt1.setText("Can COVID-19 be caught from a person who has no symptoms?");
        txt.setText("COVID-19 is mainly spread through respiratory droplets expelled by someone who is coughing or has other symptoms such as fever or tiredness. Many people\n" +
                "with COVID-19 experience only mild symptoms. This is particularly true in the early stages of the disease. It is possible to catch COVID-19 from someone who has just a mild cough and does not feel ill.\n" +
                "Some reports have indicated that people with no symptoms can transmit the virus. It is not yet known how often it happens. \n");
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });

        dialog.show();
    }
    private void demo6() {
        final Dialog dialog;

        dialog = new Dialog(FAQ.this);
        dialog.setContentView(R.layout.activity_questionanswer);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setAttributes(layoutParams);
        TextView txt=dialog.findViewById(R.id.one);
        TextView txt1=dialog.findViewById(R.id.question);
        txt1.setText("How can we protect others and ourselves if we don't know who is infected?");
        txt.setText("Practicing hand and respiratory hygiene is important at ALL times and is the best way to protect others and yourself.When possible maintain at least a 1 metre (3 feet) " +
                "distance between yourself and others. This is especially important if you are standing by someone who is coughing or sneezing. Since some infected persons may not yet be exhibiting " +
                "symptoms or their symptoms may be mild, maintaining a physical distance with everyone is a good idea if you are in an area where COVID-19 is circulating.");
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });

        dialog.show();
    }
    private void demo7() {
        final Dialog dialog;

        dialog = new Dialog(FAQ.this);
        dialog.setContentView(R.layout.activity_questionanswer);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setAttributes(layoutParams);
        TextView txt=dialog.findViewById(R.id.one);
        TextView txt1=dialog.findViewById(R.id.question);
        txt1.setText("What should I do if I have come in close contact with someone who has COVID-19?");
        txt.setText("If you have been in close contact with someone with COVID-19, you may be infected.Close contact means that you live with or have been in settings of less \n" +
                "than 1 metre from those who have the disease. In these cases, it is best to stay at home.However, if you live in an area with malaria or dengue fever it is \n" +
                "important that you do not ignore symptoms of fever. Seek medical help. When you attend the health facility wear a mask if possible, keep at least 1 metre \n" +
                "distant from other people and do not touch surfaces with your hands. If it is a child who is sick help the child stick to this advice.\n");


        dialog.show();
    }
}
