package com.test.coneve;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

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
    EventsRVAdapter adapter; //event recycler view
    TagsRVAdapter tagAdapter;
    EventChangeHandler changeHandler;
    TagWord current_selected;

    servicedataInterface serviceInterface;
    RecyclerView tagsRV;
    TagHandler tagChangeHandler;
    public EventFragment() {
        // Required empty public constructor
        discriminator = new EventsDataModel.ComparatorList.TimeCmp();
        eventSet = null;
        changeHandler = new EventChangeHandler();
        tagChangeHandler = new TagHandler();
        current_selected = new TagWord();
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
        tagAdapter = new TagsRVAdapter((AppCompatActivity) getActivity(), new Callback<Tag>() {
            @Override
            public void callback(Tag tag) {
                current_selected.addTag(tag);
                Collection<EventsDataModel> temp = filterEvents(eventSet,current_selected);
                temp = sortEvents(temp,discriminator);
                adapter.setEventSet(temp);
            }
        }, new Callback<Tag>() {
            @Override
            public void callback(Tag tag) {
                current_selected.removeTag(tag);
                Collection<EventsDataModel> temp = filterEvents(eventSet,current_selected);
                temp = sortEvents(temp,discriminator);
                adapter.setEventSet(temp);

            }
        });
        RecyclerView eventsRV = view.findViewById(R.id.events_recycler_view);
        eventsRV.setAdapter(adapter);
        eventsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        tagsRV = (RecyclerView)getActivity().findViewById(R.id.tagList);
        tagsRV.setAdapter(tagAdapter);
        tagsRV.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,true));
        FloatingActionButton addevent = getActivity().findViewById(R.id.add_event_button);
        addevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addeventIntent = new Intent(getContext(),OrganizerActivity.class);
                startActivity(addeventIntent);
            }
        });
        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                serviceInterface = (servicedataInterface) iBinder;
                serviceInterface.getEvetsObserver().observe(getActivity(), changeHandler);
                serviceInterface.getTagList().observe(getActivity(),tagChangeHandler);
                serviceInterface.getProfileData().observe(getActivity(), new Observer<ProfileData>() {
                    @Override
                    public void onChanged(ProfileData profileData) {
                        if(profileData.getOrganiser())
                            addevent.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        Intent mservice = new Intent(this.getContext(),maintainerService.class);
        getContext().bindService(mservice,connection,0);
    }



    @Override
    public void onStop() {
        super.onStop();
        serviceInterface.getEvetsObserver().removeObserver(changeHandler);
        serviceInterface.getTagList().removeObserver(tagChangeHandler);
    }

    class TagHandler implements Observer<HashMap<String,Tag>>{

        @Override
        public void onChanged(HashMap<String, Tag> stringTagHashMap) {
            Collection<Tag> set = stringTagHashMap.values();
            tagAdapter.setData(set);
        }
    }
    class tagset implements View.OnClickListener{

        @Override
        public void onClick(View view) {

        }
    }

    class EventChangeHandler implements Observer<HashMap<String,EventsDataModel>>{

        @Override
        public void onChanged(HashMap<String, EventsDataModel> stringEventsDataModelHashMap) {
            Collection<EventsDataModel> set = stringEventsDataModelHashMap.values();
            eventSet = set;
            Collection<EventsDataModel> temp = filterEvents(eventSet,current_selected);
            temp = sortEvents(temp,discriminator);
            adapter.setEventSet(temp);
        }
    }


    public Collection<EventsDataModel> filterEvents(Collection<EventsDataModel> inputEventsList,TagWord word){
        current_selected = word;
        ArrayList<EventsDataModel> temp = new ArrayList<EventsDataModel>();
        temp.addAll(inputEventsList);
        boolean empty_selection =true;
        for (int word1:
             current_selected.getArr()) {
            if(word1!=0)
                empty_selection = false;
        }
        if(empty_selection)
            return inputEventsList;
        temp.removeIf(new Predicate<EventsDataModel>() {
            @Override
            public boolean test(EventsDataModel eventsDataModel) {
                if(eventsDataModel.getTags().hasAny(current_selected))
                    return false;
                return true;
            }
        });

        return temp;
    }

    public Collection<EventsDataModel> sortEvents(Collection<EventsDataModel> inputEventsList, Comparator<EventsDataModel> compFunction){


        TreeSet<EventsDataModel> sortEvents = new TreeSet<EventsDataModel>(compFunction);
        sortEvents.addAll(inputEventsList);
        return sortEvents;
    }
}


