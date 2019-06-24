package com.cmc.pulpov1;

public  class  IdentificadorUtils {
    private static String mailC;

    public static String crearIdentificacionMail(String mail){

        mailC = mail.replace(".", "").replace("#", "").replace("#", "").replace("$", "");
       // Log.w("LogPulpo.TAG", "El mail corregido es************ " + mailC);
        return mailC.trim();
    }



}
