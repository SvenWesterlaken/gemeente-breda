package com.svenwesterlaken.gemeentebreda.presentation.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.domain.Location;
import com.svenwesterlaken.gemeentebreda.domain.Media;
import com.svenwesterlaken.gemeentebreda.logic.managers.NewLocationManager;
import com.svenwesterlaken.gemeentebreda.logic.util.ZeroUtil;
import com.svenwesterlaken.gemeentebreda.presentation.activities.NewLocationActivity;
import com.svenwesterlaken.gemeentebreda.presentation.activities.NewReportActivity;

import static android.app.Activity.RESULT_OK;
import static com.svenwesterlaken.gemeentebreda.logic.services.FetchAddressIntentService.CHOOSE_LOCATION;
import static com.svenwesterlaken.gemeentebreda.logic.services.FetchAddressIntentService.CURRENT_LOCATION;
import static com.svenwesterlaken.gemeentebreda.logic.services.FetchAddressIntentService.MEDIA_LOCATION;


public class NewReportLocationFragment extends Fragment implements NewLocationManager.LocationManagerListener {
    private LocationChangedListener mListener;
    private NewLocationManager lManager;
    
    private Location location;
    private Location mediaLocation;
    private Location currentLocation;
    private Location chosenLocation;
    
    private ConstraintLayout chooseBTN;
    private ConstraintLayout metaBTN;
    private ConstraintLayout currentBTN;
    private ConstraintLayout chosenBTN;
    private ConstraintLayout deleteBTN;

    private TextView locationTV;
    private TextView currentMessage;
    private TextView metaMessage;
    private TextView chosenMessage;
    private FloatingActionButton confirmFAB;
    private Animation popupAnimation;
    private Animation popoutAnimation;

    private boolean curLocAvailable;
    private boolean metaLocAvailable;
    private boolean chosenLocAvailable;
    
    private float alphaDisabled;
    private float alphaEnabled = 1.0f;

    private static int NEW_LOCATION_REQUEST = 1;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        popupAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.popup_animation);
        popoutAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.popout_animation);
        lManager = new NewLocationManager(getActivity(), getContext(), this);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_report_location, container, false);
        
        if (ZeroUtil.isZero(alphaDisabled, 0.0000000001)) {
            alphaDisabled = rootView.findViewById(R.id.location_BTN_delete).getAlpha();
        }
        
        chooseBTN = (ConstraintLayout) rootView.findViewById(R.id.location_BTN_choose);
        metaBTN = (ConstraintLayout) rootView.findViewById(R.id.location_BTN_meta);
        currentBTN = (ConstraintLayout) rootView.findViewById(R.id.location_BTN_current);
        chosenBTN = (ConstraintLayout) rootView.findViewById(R.id.location_BTN_chosen);
        deleteBTN = (ConstraintLayout) rootView.findViewById(R.id.location_BTN_delete);
        
        chosenBTN.setEnabled(false);
        deleteBTN.setEnabled(false);
        
        currentMessage = (TextView) rootView.findViewById(R.id.location_TV_currentMessage);
        metaMessage = (TextView) rootView.findViewById(R.id.location_TV_metaMessage);
        chosenMessage = (TextView) rootView.findViewById(R.id.location_TV_chosenMessage);
        
        locationTV = (TextView) rootView.findViewById(R.id.location_TV_location);
        
        
        metaBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAddress(mediaLocation);
                enableConfirmButton();
            }
        });
        currentBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAddress(currentLocation);
                enableConfirmButton();
            }
        });
        chosenBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAddress(chosenLocation);
                enableConfirmButton();
            }
        });
        chooseBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), NewLocationActivity.class);
                startActivityForResult(i, NEW_LOCATION_REQUEST);
            }
            
            
        });
        deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableConfirmButton();
                setAddress(null);
            }
        });
        
        
        confirmFAB = (FloatingActionButton) rootView.findViewById(R.id.location_FAB_confirm);
        confirmFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.setLocation(location);
                confirmFAB.setAnimation(popoutAnimation);
                confirmFAB.setVisibility(View.INVISIBLE);
                ((NewReportActivity) getActivity()).scrollToNext();
            }
        });
        
        return rootView;
    }
    
    @Override
    public void setReceivedLocation(Location l, int type) {
        TextView text = null;
        
        if(type == CURRENT_LOCATION) {
            text = currentMessage;
            currentLocation = l;
        } else if (type == MEDIA_LOCATION) {
            text = metaMessage;
            mediaLocation = l;
        } else if (type == CHOOSE_LOCATION) {
            text = chosenMessage;
            chosenLocation = l;
        }
        
        
        if(text != null && l != null) {
            text.setText(l.getStreet() + ", " + l.getCity());
        }
        
    }

    public void disableLocationButtons() {
        if(curLocAvailable) {
            currentBTN.setEnabled(false);
            currentBTN.setAlpha(alphaDisabled);
        }

        if(metaLocAvailable) {
            metaBTN.setEnabled(false);
            metaBTN.setAlpha(alphaDisabled);
        }

        chosenBTN.setEnabled(false);
        chosenBTN.setAlpha(alphaDisabled);
        chooseBTN.setEnabled(false);
        chooseBTN.setAlpha(alphaDisabled);
        deleteBTN.setEnabled(true);
        deleteBTN.setAlpha(alphaEnabled);
    }

    public void enableLocationButtons() {
        if(curLocAvailable) {
            currentBTN.setEnabled(true);
            currentBTN.setAlpha(alphaEnabled);
        }

        if(metaLocAvailable) {
            metaBTN.setEnabled(true);
            metaBTN.setAlpha(alphaEnabled);
        }

        if(chosenLocAvailable) {
            chosenBTN.setEnabled(true);
            chosenBTN.setAlpha(alphaEnabled);
        }

        chooseBTN.setEnabled(true);
        chooseBTN.setAlpha(alphaEnabled);
        deleteBTN.setEnabled(false);
        deleteBTN.setAlpha(alphaDisabled);
    }

    
    @Override
    public void disableButton(int type) {
        ConstraintLayout btn = null;
        TextView tv = null;
        
        if(type == CURRENT_LOCATION) {
            btn = currentBTN;
            tv = currentMessage;
        } else if (type == MEDIA_LOCATION) {
            btn = metaBTN;
            tv = metaMessage;
        } else if (type == CHOOSE_LOCATION) {
            btn = chosenBTN;
        }
        
        if (btn != null) {
            btn.setEnabled(false);
            btn.setAlpha(alphaDisabled);
            if (tv != null) {
                tv.setText(R.string.location_error);
            }
        }
    }

    @Override
    public void setBoolean(int type, boolean value) {
        if(type == CURRENT_LOCATION) {
            curLocAvailable = value;
        } else if (type == MEDIA_LOCATION) {
            metaLocAvailable = value;
        } else if (type == CHOOSE_LOCATION) {
            chosenLocAvailable = value;
        }
    }

    private void enableConfirmButton() {
        confirmFAB.setEnabled(true);
        confirmFAB.setVisibility(View.VISIBLE);
        confirmFAB.startAnimation(popupAnimation);
    }

    private void disableConfirmButton() {
        confirmFAB.startAnimation(popoutAnimation);
        confirmFAB.setEnabled(false);
        confirmFAB.setVisibility(View.INVISIBLE);
    }
    
    private void enableButton(int type) {
        ConstraintLayout btn = null;
        boolean available = false;
        
        if(type == CURRENT_LOCATION) {
            btn = currentBTN;
            available = curLocAvailable;
        } else if(type == MEDIA_LOCATION) {
            btn = metaBTN;
            available = metaLocAvailable;
        } else if (type == CHOOSE_LOCATION) {
            btn = chosenBTN;
            available = chosenLocAvailable;
        }
        
        if (btn != null) {
            if(available) {
                btn.setEnabled(true);
                btn.setAlpha(1.0f);
            }
        }
    }
    
    
    private void setAddress(Location l) {
        this.location = l;
        
        if (l != null) {
            disableLocationButtons();
            locationTV.setText(l.getStreet() + ", " + l.getCity());
        } else {
            enableLocationButtons();
            locationTV.setText(R.string.location_notselected);
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_LOCATION_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Location temploc = extras.getParcelable("location");
                
                if (temploc != null) {
                    setBoolean(CHOOSE_LOCATION, true);
                    setReceivedLocation(temploc, CHOOSE_LOCATION);
                    enableConfirmButton();
                    setAddress(temploc);
                }
                
            }
        }
        
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        
        Activity a = null;
        
        if (context instanceof Activity) {
            a = (Activity) context;
        }
        try {
            mListener = (LocationChangedListener) a;
        } catch (ClassCastException e) {
            assert a != null;
            throw new ClassCastException(a.toString() + " must implement LocationChangedListener");
        }
    }
    
    public interface LocationChangedListener {
        void setLocation(Location t);
    }
    
    public void getImageLocation(Media m) {
        lManager.getMediaLocation(m);
    }
    
    @Override
    public void onStart() {
        lManager.getApiClient().connect();
        super.onStart();
    }
    
    @Override
    public void onStop() {
        lManager.getApiClient().disconnect();
        super.onStop();
    }
    
    @Override
    public void onResume() {
        if (location != null) {
            setAddress(location);
            disableLocationButtons();
        }
        
        if (chosenLocation != null) {
            setReceivedLocation(chosenLocation, CHOOSE_LOCATION);
        }
        super.onResume();
    }
    
    
}
