package com.test.coneve;

import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Collection<EventsDataModel> eventSet;
    Comparator<EventsDataModel> discriminator;
    EventsRVAdapter adapter;
    EventChangeHandler changeHandler;
    servicedataInterface serviceInterface;
    public EventFragment() {
        // Required empty public constructor
        discriminator = new EventsDataModel.ComparatorList.TimeCmp();
        eventSet = null;
        changeHandler = new EventChangeHandler();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventFragment newInstance(String param1, String param2) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        getActivity().setTitle("Events");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        View view = getActivity().findViewById(R.id.events_recycler_view).getRootView();
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

         adapter = new EventsRVAdapter(metrics.widthPixels, metrics.heightPixels, this);

        RecyclerView eventsRV = view.findViewById(R.id.events_recycler_view);
        eventsRV.setAdapter(adapter);
        eventsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        Intent mservice = new Intent(getContext(),maintainerService.class);
        serviceInterface = new servicedataInterface(null);
        getContext().bindService(mservice,(ServiceConnection) serviceInterface,0);
        serviceInterface.getEvetsObserver().observe(this, changeHandler);
    }

    @Override
    public void onStop() {
        super.onStop();
        serviceInterface.getEvetsObserver().removeObserver(changeHandler);
    }

    class EventChangeHandler implements Observer<HashMap<String,EventsDataModel>>{

        @Override
        public void onChanged(HashMap<String, EventsDataModel> stringEventsDataModelHashMap) {
            Collection<EventsDataModel> set = stringEventsDataModelHashMap.values();
            eventSet = set;
            Collection<EventsDataModel> temp = filterEvents(eventSet);
            temp = sortEvents(temp,discriminator);
            adapter.setEventSet(temp);
        }
    }

    public Collection<EventsDataModel> filterEvents(Collection<EventsDataModel> inputEventsList){
        // TODO: apply filters on the input list and return it
        return inputEventsList;
    }

    public Collection<EventsDataModel> sortEvents(Collection<EventsDataModel> inputEventsList, Comparator<EventsDataModel> compFunction){

        TreeSet<EventsDataModel> sortEvents = new TreeSet<EventsDataModel>(compFunction);
        sortEvents.addAll(inputEventsList);
        return sortEvents;
    }
}


