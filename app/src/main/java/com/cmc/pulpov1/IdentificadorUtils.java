package com.cmc.pulpov1;

import android.util.Log;

public  class  IdentificadorUtils {
    private static String mailC;

    public static String crearIdentificacionMail(String mail){

        mailC = mail.replace(".", "").replace("#", "").replace("#", "").replace("$", "");
        Log.w("PULPOLOG", "El mail corregido es************ " + mailC);
        return mailC.trim();
    }



}
