package site.easy.to.build.crm.model.importcsv;


import lombok.Data;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class Data3Validator {
    private String customerEmail;
    private double budget;

    public void setCustomerEmail(String email, List<String> erreurs, int ligne) {
        if (email == null || email.trim().isEmpty()) {
            erreurs.add("Erreur ligne " + ligne + ": Email ne doit pas être vide.");
            return;
        }

        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            erreurs.add("Erreur ligne " + ligne + ": Email invalide -> " + email);
        } else {
            this.customerEmail = email;
        }
    }

    public void setBudget(String budgetStr, List<String> erreurs, int ligne) {
        try {
            double budgetValue = Double.parseDouble(budgetStr);
            if (budgetValue < 0) {
                erreurs.add("Erreur ligne " + ligne + ": Le budget doit être positif.");
            } else {
                this.budget = budgetValue;
            }
        } catch (NumberFormatException e) {
            erreurs.add("Erreur ligne " + ligne + ": Montant du budget invalide -> " + budgetStr);
        }
    }

    public static List<Data3Validator> lireEtVerifierCsv(List<String> erreurs, File fichier) {
        List<Data3Validator> donnees = new ArrayList<>();
        Pattern pattern = Pattern.compile("(?:\"([^\"]*)\")|([^,]+)"); // Gérer les valeurs entre guillemets

        try (BufferedReader br = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            int numeroLigne = 0;

            while ((ligne = br.readLine()) != null) {
                numeroLigne++;
                if (numeroLigne == 1) { // Ignorer l'en-tête
                    continue;
                }

                List<String> valeurs = new ArrayList<>();
                Matcher matcher = pattern.matcher(ligne);
                while (matcher.find()) {
                    valeurs.add(matcher.group(1) != null ? matcher.group(1) : matcher.group(2));
                }

                if (valeurs.size() < 2) {
                    erreurs.add("Erreur ligne " + numeroLigne + ": Format invalide, colonnes manquantes");
                    continue;
                }

                Data3Validator data = new Data3Validator();
                data.setCustomerEmail(valeurs.get(0), erreurs, numeroLigne);
                data.setBudget(valeurs.get(1), erreurs, numeroLigne);
                donnees.add(data);
            }
        } catch (IOException ioex) {
            erreurs.add("Erreur lors de la lecture du fichier : " + ioex.getMessage());
        }

        return donnees;
    }
}
