package com.cesi.cube;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Utils {
    public void faireAppelGET(Context context,String action, VolleyCallback callback) {
        String url = "http://172.20.10.3:8000/api/ActionTypes?"+action;

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("$$$ CESI $$$", response);
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("$$$ CESI $$$", "error");
                        if (error instanceof NetworkError) {
                            Log.e("$$$ CESI $$$", "Erreur de réseau, par exemple pas de connexion Internet");
                            //
                        } else if (error instanceof ServerError) {
                            Log.e("$$$ CESI $$$", "Erreur du serveur, par exemple une réponse invalide ou un problème côté serveur");
                            //
                        } else if (error instanceof AuthFailureError) {
                            Log.e("$$$ CESI $$$", "Erreur d'authentification, par exemple des problèmes avec les informations d'identification");
                            //
                        } else if (error instanceof ParseError) {
                            Log.e("$$$ CESI $$$", "Erreur d'analyse, par exemple une réponse JSON mal formée");
                            //
                        } else if (error instanceof NoConnectionError) {
                            Log.e("$$$ CESI $$$", "Pas de connexion disponible, par exemple lorsque vous êtes hors ligne");
                            //
                        } else if (error instanceof TimeoutError) {
                            Log.e("$$$ CESI $$$", "Erreur de délai d'attente, par exemple lorsque la requête prend trop de temps");
                            //
                        }
                    }
                });

        requestQueue.add(stringRequest);
    }

    public interface VolleyCallback {
        void onSuccess(String response);
        void onError();
    }
}
