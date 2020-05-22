package com.jpss.ipmsintegratedpandemicmanagementsystem.Other;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.jpss.ipmsintegratedpandemicmanagementsystem.Adapter.MyAdapter;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Bean.Bean;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Database.DatabaseOperation;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Model.GenericModel;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Model.ObjectModel;
import com.jpss.ipmsintegratedpandemicmanagementsystem.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HealthUpdateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HealthUpdateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HealthUpdateFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    AlarmManager alarmManager;
    ArrayList<String>getreason=new ArrayList<>();
    ArrayList<String>select_items =new ArrayList<>();
    ArrayList<String>arraylist =new ArrayList<>();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String ALARM_TYPE_REPEAT = "ALARM\n_TYPE_REPEAT";
    public static final String ALARM_DESCRIPTION = "ALARM_DESCRIPTION";
    private PendingIntent pendingIntent;
    RecyclerView recyclerView;
    ImageView imageView,image1View;
    public static final String ALARM_TYPE = "ALARM_TYPE";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public MyAdapter myAdapter;
    Spinner reason,symptons;
    Button btn;
    ArrayList<String> quarantinereason=new ArrayList();
    ArrayList<String>symptoms=new ArrayList();
    private OnFragmentInteractionListener mListener;
    EditText temp,bp;
    RadioButton yes,no;
    DatabaseOperation databaseOperation =new DatabaseOperation(getActivity());
    RadioGroup radioGroup;
    public HealthUpdateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HealthUpdateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HealthUpdateFragment newInstance(String param1, String param2) {
        HealthUpdateFragment fragment = new HealthUpdateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseOperation = new DatabaseOperation(getActivity());
        alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);

        databaseOperation.open();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_health_update, container, false);
        //   reason = rootView.findViewById(R.id.reason_quarantine);
//        imageView=rootView.findViewById(R.id.imgView);
//        image1View=rootView.findViewById(R.id.img1View);
        recyclerView=rootView.findViewById(R.id.rcView);
        temp=rootView.findViewById(R.id.temp);
        bp=rootView.findViewById(R.id.bp);
        btn=rootView.findViewById(R.id.btnsubmit);
        yes=rootView.findViewById(R.id.rbYes);
        no=rootView.findViewById(R.id.rbNo);
        radioGroup=rootView.findViewById(R.id.rgGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup arg0, int id) {
                switch (id) {

                    case R.id.rbYes:
                        recyclerView.setVisibility(View.VISIBLE);
                        ArrayList<ObjectModel> listVOs = new ArrayList<>();

                        for (int i = 0; i < select_items.size(); i++) {
                            ObjectModel object = new ObjectModel();
                            object.setTitle(select_items.get(i));
                            object.setSelected(false);
                            listVOs.add(object);
                        }
                        myAdapter = new MyAdapter(getContext(),listVOs);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(myAdapter);

                        break;
                    case R.id.rbNo:
                        recyclerView.setVisibility(View.GONE);

                        break;

                }
            }
        });

       /* yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                recyclerView.setVisibility(View.VISIBLE);
                ArrayList<ObjectModel> listVOs = new ArrayList<>();

                for (int i = 0; i < select_items.size(); i++) {
                    ObjectModel object = new ObjectModel();
                    object.setTitle(select_items.get(i));
                    object.setSelected(false);
                    listVOs.add(object);
                }
                myAdapter = new MyAdapter(getContext(),listVOs);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(myAdapter);

            }
        });

       no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               recyclerView.setVisibility(View.GONE);

           }
       });*/
        database();
        getreason.add("travel from china");
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, getreason);
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        reason.setAdapter(arrayAdapter);
//        reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String text=parent.getItemAtPosition(position).toString();
//
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
        //      });

//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                recyclerView.setVisibility(View.VISIBLE);
//                imageView.setVisibility(View.GONE);
//                image1View.setVisibility(View.VISIBLE);
//
//            }
//        });
//        image1View.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                recyclerView.setVisibility(View.GONE);
//                image1View.setVisibility(View.GONE);
//                imageView.setVisibility(View.VISIBLE);
//
//            }
//        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendmedicaldetails sendmedicaldetails=new sendmedicaldetails();
                sendmedicaldetails.execute();

            }
        });
        //   final String[] select_items = {"cough","cold","fever"};

//
        repeattask();


        return rootView;

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public void repeattask() {
        Intent intent = new Intent(getActivity(), MyBroadcastReceiver.class);
        intent.putExtra(ALARM_TYPE, ALARM_TYPE_REPEAT);
        intent.putExtra(ALARM_DESCRIPTION, "give health details alarm activated");
        pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long alarmStartTime = System.currentTimeMillis();
        // This is too short, it will be expanded by android os to 60 seconds by default.
        long alarmExecuteInterval = 1* 10000;
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime, alarmExecuteInterval, pendingIntent);
    }
    private void database(){
        DatabaseOperation databaseOperation=new DatabaseOperation(getActivity());
        databaseOperation.open();
        getreason = databaseOperation.getreasonquarantine();
        select_items = databaseOperation.getsymptoms();

    }
    public class sendmedicaldetails extends AsyncTask<String, String, String> {

        ProgressDialog dialog;

        @Override
        protected String doInBackground(String... params) {
            String res=null;
            GenericModel prepaidDocModel = new GenericModel(getActivity());
            JSONObject jsonObject=  getvalues();

            try {
                res = prepaidDocModel.sendmedicalrecord(jsonObject);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            try {
                dialog.dismiss();
                if(result.contains("data inserted")){
                    Toast.makeText(getActivity(), "medical record send....", Toast.LENGTH_LONG).show();
                    radioGroup.clearCheck();
                    temp.setText("");
                    bp.setText("");
                }
                else{
                    Toast.makeText(getActivity(), "Fail to insert data....", Toast.LENGTH_LONG).show();

                }
//                    if (result > 0) {
//                        Toast.makeText(getActivity(), "Data  recieved", Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(getActivity(), " no updation  Found", Toast.LENGTH_LONG).show();
//                    }

            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(getActivity(), "", "Proccessing....Please wait");
            dialog.show();
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog

        }

        @Override
        protected void onProgressUpdate(String... text) {
            //firstBar.
            // Things to be done while execution of long running operation is in
            // progress. For example updating ProgessDialog
        }

    }
    private  JSONObject getvalues(){
        int result;
        
        JSONArray jsonArray=new JSONArray();
        JSONObject jsonObject=new JSONObject();
        ArrayList<String>val=new ArrayList<>();
        List<Bean>list=new ArrayList<Bean>();
        List<Bean>map=new ArrayList<Bean>();
        Bean bean =new Bean();
        String temperature=temp.getText().toString();
        String BP=bp.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateandTime1 = sdf.format(new Date());

        databaseOperation.open();
        arraylist =myAdapter.getList();
        val =databaseOperation.getsymtomsid(arraylist);
        System.out.println(val);

        bean.setTemprature(temperature);
        bean.setBp(BP);
        databaseOperation.open();
        String id=  databaseOperation.getquarntineid();
        bean.setQuarantime_id(id);
        bean.setTimestamp(currentDateandTime1);
        list.add(bean);
        databaseOperation.insertmedicalrecord(list);
//
        try {
            jsonObject.put("temperature",temperature);
            jsonObject.put("BP",BP);
            jsonObject.put("quarantineid",id);
            jsonObject.put("medicalrecordremark","health");
            jsonObject.put("mappingremark","good");
            JSONObject object = new JSONObject();
            object.put("symptomsid", val);
            jsonArray.put(object);
            jsonObject.put("s_id",jsonArray);
            result= databaseOperation.getmedid(currentDateandTime1);
            databaseOperation.insertmap(val,result);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        databaseOperation.close();

        return jsonObject;
    }


}
