package site.easy.to.build.crm.model.importcsv;

import lombok.Data;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class Data1Validator {
    private String customerEmail;
    private String subjectOrName;
    private String type;
    private String status;
    private double expense;

    private static final Set<String> VALID_STATUSES = Set.of("meeting-to-schedule", "open", "archived");
    private static final Set<String> VALID_TYPE = Set.of("lead", "ticket");

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

    public void setSubjectOrName(String subjectOrName, List<String> erreurs, int ligne) {
        if (subjectOrName == null || subjectOrName.trim().isEmpty()) {
            erreurs.add("Erreur ligne " + ligne + ": Le sujet ou nom ne doit pas être vide.");
        } else {
            this.subjectOrName = subjectOrName.trim();
        }
    }

    public void setType(String type, List<String> erreurs, int ligne) {
        if (type == null || !VALID_TYPE.contains(type.trim())) {
            erreurs.add("Erreur ligne " + ligne + ": Type invalide -> " + type);
        } else {
            this.type = type.trim();
        }
    }

    public void setStatus(String status, List<String> erreurs, int ligne) {
        if (status == null || !VALID_STATUSES.contains(status.trim())) {
            erreurs.add("Erreur ligne " + ligne + ": Statut invalide -> " + status);
        } else {
            this.status = status.trim();
        }
    }

    public void setExpense(String expenseStr, List<String> erreurs, int ligne) {
        try {
            String sanitizedExpense = expenseStr.replace(",", "."); // Gérer les nombres avec virgule
            System.out.println("STRING :"+expenseStr);
            System.out.println("DOUBLE :"+sanitizedExpense);
            double expenseValue = Double.parseDouble(sanitizedExpense);
            if (expenseValue < 0) {
                erreurs.add("Erreur ligne " + ligne + ": Montant doit être positif.");
            } else {
                this.expense = expenseValue;
            }
        } catch (NumberFormatException e) {
            erreurs.add("Erreur ligne " + ligne + ": Montant invalide -> " + expenseStr);
        }
    }

    public static List<Data1Validator> lireEtVerifierCsv(List<String> erreurs, File fichier) {
        List<Data1Validator> donnees = new ArrayList<>();
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

                if (valeurs.size() < 5) {
                    erreurs.add("Erreur ligne " + numeroLigne + ": Format invalide, colonnes manquantes");
                    continue;
                }

                Data1Validator data = new Data1Validator();
                data.setCustomerEmail(valeurs.get(0), erreurs, numeroLigne);
                data.setSubjectOrName(valeurs.get(1), erreurs, numeroLigne);
                data.setType(valeurs.get(2), erreurs, numeroLigne);
                data.setStatus(valeurs.get(3), erreurs, numeroLigne);
                data.setExpense(valeurs.get(4), erreurs, numeroLigne);
                donnees.add(data);
            }
        } catch (IOException ioex) {
            erreurs.add("Erreur lors de la lecture du fichier : " + ioex.getMessage());
        }

        return donnees;
    }

    // insrtion des données
}

