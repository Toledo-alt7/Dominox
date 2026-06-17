package br.maua.dominox;

import java.util.prefs.Preferences;

public class SessionManager {

    private static final Preferences prefs   = Preferences.userRoot().node("sessão/dominox");
    private static final String KEY_USER     = "usuário_salvo";
    private static final String KEY_REMEMBER = "manter_login";

    // salva o e-mail e marca manter_login = true
    public static void saveSession(String email) {
        prefs.put(KEY_USER, email);
        prefs.putBoolean(KEY_REMEMBER, true);
    }

    // Remove a sessão salva (logout ou checkbox desmarcado)
    public static void clearSession() {
        prefs.remove(KEY_USER);
        prefs.putBoolean(KEY_REMEMBER, false);
    }

    // retorna e-mail salvo, ou null se não houver sessão ativa 
    public static String getSavedUser() {
        if (prefs.getBoolean(KEY_REMEMBER, false)) {
            return prefs.get(KEY_USER, null);
        }
        return null;
    }
    // retorna true ou false baseado no getSavedUser()
    public static boolean hasActiveSession() {
        return getSavedUser() != null;
    }
}
