package site.easy.to.build.crm.model;

import lombok.Data;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class TempCustomerImport {
    private int id;
    private String password;
    private String username;
    private String token;
    private boolean passwordSet;
    private int customerId;
    private String name;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String country;
    private int userId;
    private String description;
    private String position;
    private String twitter;
    private String facebook;
    private String youtube;
    private Timestamp createdAt;
    private String email;
    private int profileId;

    public void setId(String id, List<String> erreurs, int ligne) {
        try {
            this.id = Integer.parseInt(id);
        } catch (NumberFormatException ex) {
            erreurs.add("Erreur ligne " + ligne + ": ID invalide -> " + id);
        }
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setPasswordSet(String passwordSet, List<String> erreurs, int ligne) {
        if (passwordSet.equals("0") || passwordSet.equals("1")) {
            this.passwordSet = passwordSet.equals("1");
        } else {
            erreurs.add("Erreur ligne " + ligne + ": Valeur de password_set invalide -> " + passwordSet);
        }
    }

    public void setCustomerId(String customerId, List<String> erreurs, int ligne) {
        try {
            this.customerId = Integer.parseInt(customerId);
        } catch (NumberFormatException ex) {
            erreurs.add("Erreur ligne " + ligne + ": Customer ID invalide -> " + customerId);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setUserId(String userId, List<String> erreurs, int ligne) {
        try {
            this.userId = Integer.parseInt(userId);
        } catch (NumberFormatException ex) {
            erreurs.add("Erreur ligne " + ligne + ": User ID invalide -> " + userId);
        }
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public void setCreatedAt(String createdAt, List<String> erreurs, int ligne) {
        try {
            this.createdAt = Timestamp.valueOf(createdAt);
        } catch (IllegalArgumentException ex) {
            erreurs.add("Erreur ligne " + ligne + ": Date invalide (format attendu: YYYY-MM-DD HH:MI:SS) -> " + createdAt);
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfileId(String profileId, List<String> erreurs, int ligne) {
        try {
            this.profileId = Integer.parseInt(profileId);
        } catch (NumberFormatException ex) {
            erreurs.add("Erreur ligne " + ligne + ": Profile ID invalide -> " + profileId);
        }
    }

    public static List<TempCustomerImport> lireEtVerifierCsv(List<String> erreurs, File fichier) {
        List<TempCustomerImport> rep = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            int numeroLigne = 0;
            while ((ligne = br.readLine()) != null) {
                numeroLigne++;
                if (numeroLigne == 1) continue; // Ignorer l'en-tête
                
                String[] valeurs = ligne.split(",");
                if (valeurs.length < 20) { // Vérifier le nombre de colonnes
                    erreurs.add("Erreur ligne " + numeroLigne + ": Nombre de colonnes insuffisant.");
                    continue;
                }
                
                TempCustomerImport customer = new TempCustomerImport();
                customer.setId(valeurs[0], erreurs, numeroLigne);
                customer.setPassword(valeurs[1]);
                customer.setUsername(valeurs[2]);
                customer.setToken(valeurs[3]);
                customer.setPasswordSet(valeurs[4], erreurs, numeroLigne);
                customer.setCustomerId(valeurs[5], erreurs, numeroLigne);
                customer.setName(valeurs[6]);
                customer.setPhone(valeurs[7]);
                customer.setAddress(valeurs[8]);
                customer.setCity(valeurs[9]);
                customer.setState(valeurs[10]);
                customer.setCountry(valeurs[11]);
                customer.setUserId(valeurs[12], erreurs, numeroLigne);
                customer.setDescription(valeurs[13]);
                customer.setPosition(valeurs[14]);
                customer.setTwitter(valeurs[15]);
                customer.setFacebook(valeurs[16]);
                customer.setYoutube(valeurs[17]);
                customer.setCreatedAt(valeurs[18], erreurs, numeroLigne);
                customer.setEmail(valeurs[19]);
                customer.setProfileId(valeurs[20], erreurs, numeroLigne);
                
                rep.add(customer);
            }
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
        return rep;
    }
}
