package com.example.vistas.Commons;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.vistas.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonMethods implements Codes_Logs {

    public final int USUARIO_NOMBRE_MAX = 56;
    public final int USUARIO_NOMBRE_MIN = 6;
    public final int USUARIO_CORREO_MAX = 30;
    public final int USUARIO_CORREO_MIN = 6;
    public final int USUARIO_CONTRASENIA_MAX = 30;
    public final int USUARIO_CONTRASENIA_MIN = 6;

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

    public String validate_NombreUsuario(String text, boolean alertError) {

        String rspt = text.trim();
        int length = rspt.length();

        if (length == 0) {
            if (alertError) show_toast(context.getString(R.string.error_txtNombre_empty));
            return null;
        }

        if (length < USUARIO_NOMBRE_MIN) {
            if (alertError) show_toast(context.getString(R.string.error_txtNombre_short));
            return null;
        }

        if (length > USUARIO_NOMBRE_MAX) {
            if (alertError) show_toast(context.getString(R.string.error_txtNombre_long));
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

    public double validate_Presupuesto(String newPresupuesto) {

        double rspt = -1;

        try {
            newPresupuesto = newPresupuesto.trim();

            if (newPresupuesto.length() == 0) {
                show_toast("Debe completar el campo presupuesto");
                return -1;
            }

            if (newPresupuesto.length() > 6) {
                show_toast("Debe ingresar un numero menor a 6 digitos");
                return -1;
            }

            rspt = Double.parseDouble(newPresupuesto);

            if (rspt < 0) {
                show_toast("Solo numero positivos o 0");
                return -1;
            }

        } catch (Exception ex) {
            ex.printStackTrace();

            show_toast("Debe ingresar numeros enteros o decimales");
        }

        return rspt;
    }

//    Month month = LocalDate.now().getMonth();
//
//    monthName = month.getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
//
//    monthName = (monthName.charAt(0) + "").toUpperCase() + monthName.substring(1);


}
