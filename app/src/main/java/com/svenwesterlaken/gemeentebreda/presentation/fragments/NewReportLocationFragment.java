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
import com.svenwesterlaken.gemeentebreda.logic.services.FetchAddressIntentService;
import com.svenwesterlaken.gemeentebreda.presentation.activities.NewLocationActivity;
import com.svenwesterlaken.gemeentebreda.presentation.activities.NewReportActivity;

import static android.app.Activity.RESULT_OK;


public class NewReportLocationFragment extends Fragment implements NewLocationManager.LocationManagerListener {
        private LocationChangedListener mListener;
        private NewLocationManager lManager;
        
        private Location location, mediaLocation, currentLocation, chosenLocation;
        
        private ConstraintLayout chooseBTN, metaBTN, currentBTN, chosenBTN, deleteBTN;
        private View rootView;
        private TextView locationTV, currentMessage, metaMessage, chosenMessage;
        private FloatingActionButton confirmFAB;
        private Animation popupAnimation, popoutAnimation;
        
        private float alpha = 0;
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
            rootView = inflater.inflate(R.layout.fragment_new_report_location, container, false);
            
            if (alpha == 0.0f) {
                alpha = rootView.findViewById(R.id.location_BTN_delete).getAlpha();
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
            
            if(type == FetchAddressIntentService.CURRENT_LOCATION) {
                text = currentMessage;
                currentLocation = l;
            } else if (type == FetchAddressIntentService.MEDIA_LOCATION) {
                text = metaMessage;
                mediaLocation = l;
            } else if (type == FetchAddressIntentService.CHOOSE_LOCATION) {
                text = chosenMessage;
                chosenLocation = l;
            }
            
            
            if(text != null && l != null) {
                text.setText(l.getStreet() + ", " + l.getCity());
            }
            
        }
        
        @Override
        public void disableButton(int type) {
            ConstraintLayout btn = null;
            TextView tv = null;
            
            if(type == FetchAddressIntentService.CURRENT_LOCATION) {
                btn = currentBTN;
                tv = currentMessage;
            } else if (type == FetchAddressIntentService.MEDIA_LOCATION) {
                btn = metaBTN;
                tv = metaMessage;
            }
            
            if (btn != null && tv != null) {
                btn.setEnabled(false);
                btn.setAlpha(alpha);
                tv.setText(R.string.location_error);
            }
        }
        
        private void enableConfirmButton() {
            confirmFAB.setVisibility(View.VISIBLE);
            confirmFAB.startAnimation(popupAnimation);
        }
        
        private void enableButton(int type) {
            ConstraintLayout btn = null;
            
            if(type == FetchAddressIntentService.CURRENT_LOCATION) {
                btn = currentBTN;
            } else if(type == FetchAddressIntentService.MEDIA_LOCATION) {
                btn = metaBTN;
            } else if (type == FetchAddressIntentService.CHOOSE_LOCATION) {
                btn = chosenBTN;
            }
            
            if (btn != null) {
                btn.setEnabled(true);
                btn.setAlpha(1.0f);
            }
        }
        
        
        private void setAddress(Location l) {
            this.location = l;
            
            if (l != null) {
                locationTV.setText(l.getStreet() + ", " + l.getCity());
            }
        }
        
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == NEW_LOCATION_REQUEST && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Location temploc = extras.getParcelable("location");
                    
                    if (temploc != null) {
                        enableConfirmButton();
                        setAddress(temploc);
                        enableButton(FetchAddressIntentService.CHOOSE_LOCATION);
                        setReceivedLocation(temploc, FetchAddressIntentService.CHOOSE_LOCATION);
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
            }
            super.onResume();
        }
        
        
    }
