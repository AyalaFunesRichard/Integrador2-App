package com.example.vistas.Commons;

import android.content.Context;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.example.vistas.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class CommonMethods implements Codes_Logs {

    public final int USUARIO_NOMBRE_MAX = 56;
    public final int USUARIO_NOMBRE_MIN = 6;

    public final int USUARIO_CORREO_MAX = 30;
    public final int USUARIO_CORREO_MIN = 6;

    public final int USUARIO_CONTRASENIA_MAX = 30;
    public final int USUARIO_CONTRASENIA_MIN = 6;

    public final int ITEM_NAME_MIN = 2;
    public final int ITEM_NAME_MAX = 30;

    public final String PATTERN_NAME = "^[a-zA-ZÀ-ÿ0-9\\u00f1\\u00d1._() ,¿?¡!-]+(\\s*[a-zA-ZÀ-ÿ0-9\\u00f1\\u00d1._() ,¿?¡!-]*)*[a-zA-ZÀ-ÿ0-9\\u00f1\\u00d1._() ,¿?¡!-]+$";
//    public final String PATTERN_NAME = "^[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+(\\s*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]*)*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+$";
//    public final String PATTERN_NAME = "[a-zA-ZÀ-ÿñÑ]+[a-zA-ZÀ-ÿñÑ. _-]*[a-zA-ZÀ-ÿñÑ]+$";
//    public final String PATTERN_NAME = "^[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+(\\s*[a-z A-ZÀ-ÿ\\u00f1\\u00d1._-]*)*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+$";
//    public final String PATTERN_NAME = "[A-Za-zñÑ][a-z]+";

//    public final String PATTERN_NAME = "[a-zA-Z0-9ñÑ ]{2,30}$";
    public final String PATTERN_EMAIL = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    private Context context;

    public CommonMethods(Context context) {
        this.context = context;
    }

    public int getMonth(String timeString) {
        int month = -1;

        try {
            String monthString = new SimpleDateFormat("MM").format(timeString);

            month = Integer.parseInt(monthString);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return month;
    }

    public int getYear(String timeString) {
        int year = -1;

        try {
            String monthString = new SimpleDateFormat("yyyy").format(timeString);

            year = Integer.parseInt(monthString);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return year;
    }

    // * Validations ->
    public String validate_NombreUsuario(String text, boolean alertError) {

        String rspt = text.trim();
        int length = rspt.length();

        if (length == 0) {
            if (alertError) show_toast(context.getString(R.string.error_txtNombre_empty));
            return null;
        }

        if (length < USUARIO_NOMBRE_MIN) {
            if (alertError) show_toast("EL nombre debe de ser mayor a 6 caracteres");
            return null;
        }

        if (length > USUARIO_NOMBRE_MAX) {
            if (alertError) show_toast(context.getString(R.string.error_txtNombre_long));
            return null;
        }

        boolean patternOk = (Pattern.compile(PATTERN_NAME).matcher(rspt).matches());
        if (!patternOk) {
            if (alertError) show_toast("Nombre no valido.");
            return null;
        }

        return rspt;
    }

    public String validate_EmailUsuario(String text, boolean alertError) {
        String rspt = text.trim();
        int length = rspt.length();

        if (length == 0) {
            if (alertError) show_toast(context.getString(R.string.error_txtCorreo_empty));
            return null;
        }

        if (length < USUARIO_CORREO_MIN) {
            if (alertError) show_toast(context.getString(R.string.error_txtCorreo_short));
            return null;
        }

        if (length > USUARIO_CORREO_MAX) {
            if (alertError) show_toast(context.getString(R.string.error_txtCorreo_long));
            return null;
        }

        boolean patternOk = (Pattern.compile(PATTERN_EMAIL).matcher(rspt).matches());
        if (!patternOk) {
            if (alertError) show_toast("Correo no valido.");
            return null;
        }

        return rspt;
    }

    public String validate_ContraseniaUsuario(String text, boolean alertError) {
        String rspt = text.trim();
        int length = rspt.length();

        if (length == 0) {
            if (alertError) show_toast(context.getString(R.string.error_txtContra_empty));
            return null;
        }

        if (length < USUARIO_CONTRASENIA_MIN) {
            if (alertError) show_toast(context.getString(R.string.error_txtContra_short));
            return null;
        }

        if (length > USUARIO_CONTRASENIA_MAX) {
            if (alertError) show_toast(context.getString(R.string.error_txtContra_long));
            return null;
        }

        return rspt;
    }

    public double validate_Presupuesto(String newPresupuesto) {

        double rspt = -1;

        try {
            newPresupuesto = newPresupuesto.trim();

            if (newPresupuesto.length() == 0) {
                show_toast("Debe completar el campo presupuesto");
                return -1;
            }

            if (newPresupuesto.length() > 6) {
                show_toast("Debe ingresar una cantidad menor a 6 digitos");
                return -1;
            }

            rspt = Double.parseDouble(newPresupuesto);

            if (rspt < 0) {
                show_toast("Solo números positivos o 0");
                return -1;
            }

        } catch (Exception ex) {
            ex.printStackTrace();

            show_toast("Debe ingresar numeros enteros o decimales");
        }

        return rspt;
    }

    public String validate_Nombre(String text, boolean alertError)  {

        String rspt = text.trim();
        int length = rspt.length();

        if (length == 0) {
            if (alertError) show_toast(context.getString(R.string.error_txtNombre_empty));
            return null;
        }

        if (length < ITEM_NAME_MIN) {
            if (alertError) show_toast("El nombre debe de ser mayor a 2 caracteres");
            return null;
        }

        if (length > ITEM_NAME_MAX) {
            if (alertError) show_toast(context.getString(R.string.error_txtNombre_long));
            return null;
        }

        boolean patternOk = (Pattern.compile(PATTERN_NAME).matcher(rspt).matches());
        if (!patternOk) {
            if (alertError) show_toast("Nombre no valido.");
            return null;
        }

        return rspt;
    }

    // * Show message to User or console ->
    public void show_toast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void show_alert(String message) {
        show_toast(message);
    }

    public void show_log__fireBase(String error) {
        Log.e(LOG_FIREBASE, error);
    }

    public void show_log(String tag, String error) {
        Log.e(tag, error);
    }

    // * For timming
    public String getTime_ForDataBase() {
        return new SimpleDateFormat(Code_DB.DATE_FORMART).format(new Date());
    }

    public String getMonthAndYear() {
        return new SimpleDateFormat("yyyy-MM").format(new Date());
    }

    public String getMonthAndYear(String str) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM").parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("yyyy-MM").format(date);
    }


}
