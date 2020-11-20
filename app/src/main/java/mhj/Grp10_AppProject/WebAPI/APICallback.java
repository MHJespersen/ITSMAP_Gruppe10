package mhj.Grp10_AppProject.WebAPI;

import org.json.JSONObject;

//Idea inspired from https://stackoverflow.com/questions/3398363/how-to-define-callbacks-in-android
public interface APICallback {
    void OnDataCallback(JSONObject object);
}
