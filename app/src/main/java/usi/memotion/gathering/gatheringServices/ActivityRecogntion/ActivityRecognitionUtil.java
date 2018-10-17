package usi.memotion.gathering.gatheringServices.ActivityRecogntion;

import com.google.android.gms.location.DetectedActivity;

public class ActivityRecognitionUtil{
    public static String getActivityName(int activity) {
        switch (activity) {
            case DetectedActivity.IN_VEHICLE:
                return "IN_VEHICLE";
            case DetectedActivity.ON_BICYCLE:
                return "ON_BICYCLE";
            case DetectedActivity.ON_FOOT:
                return "ON_FOOT";
            case DetectedActivity.RUNNING:
                return "RUNNING";
            case DetectedActivity.STILL:
                return "STILL";
            case DetectedActivity.WALKING:
                return "WALKING";
            default:
                return "UNKNOWN";
        }
    }

    public static String getTransitionName(int transition){
        if(transition==0)
            return "ENTER";
        return "EXIT";
    }

    public static boolean isValidActivity(int activity){
        if (activity == DetectedActivity.IN_VEHICLE ||
                activity == DetectedActivity.ON_BICYCLE ||
                activity == DetectedActivity.ON_FOOT ||
                activity == DetectedActivity.RUNNING ||
                activity == DetectedActivity.STILL ||
                activity == DetectedActivity.WALKING)
            return true;
        return false;
    }
}
