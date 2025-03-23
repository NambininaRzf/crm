package site.easy.to.build.crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import site.easy.to.build.crm.model.CSVFile;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/dbconfiguration")
public class DBConfigurationsController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/clear-data")
    @Transactional
    public String clearData() throws Exception {
        // Charger le fichier SQL
        String sqlFilePath = "src/main/resources/sql/clear-data.sql";
        List<String> sqlLines = Files.readAllLines(Paths.get(sqlFilePath));

        // Concaténer et exécuter chaque requête SQL
        StringBuilder sqlQuery = new StringBuilder();
        for (String line : sqlLines) {
            line = line.trim();
            if (!line.isEmpty() && !line.startsWith("--")) { // Ignorer commentaires et lignes vides
                sqlQuery.append(line).append(" ");
                if (line.endsWith(";")) {
                    entityManager.createNativeQuery(sqlQuery.toString()).executeUpdate();
                    sqlQuery.setLength(0); // Réinitialiser après exécution
                }
            }
        }

        System.out.println("Données supprimées avec succès !");
        return "redirect:/";
    }

        // @GetMapping("/import-csv")
        // @Transactional
        // public String redirectImport(Model model) throws Exception {
        //     model.addAttribute("csvfile", new CSVFile());
        //     return "dbconfigurations/import-csv";
        // }


    public void importData() {
        String enableStrictMode = "SET SESSION sql_mode = 'STRICT_TRANS_TABLES';";
        String enableLocalInfile = "SET GLOBAL local_infile = 1;";

        String loadDataSql = "LOAD DATA LOCAL INFILE 'N:\\\\ITU\\\\S6\\\\EVAL\\\\RepriseCode\\\\crm\\\\conf-files\\\\importData\\\\customers_data.csv' " +
                "INTO TABLE temp_customer_import " +
                "FIELDS TERMINATED BY ',' " +
                "ENCLOSED BY '\"' " +
                "LINES TERMINATED BY '\\n' " +
                "IGNORE 1 ROWS;";

        try {
            executeNativeQuery(enableStrictMode);
            executeNativeQuery(enableLocalInfile);
            executeNativeQuery(loadDataSql);

            // Récupérer les warnings après exécution
            List<Object[]> warnings = getMySQLWarnings();
            if (!warnings.isEmpty()) {
                logWarnings(warnings);
            }

            System.out.println("Import terminé !");
        } catch (Exception e) {
            logError(e);
        }
    }

    private void executeNativeQuery(String sql) {
        try {
            Query query = entityManager.createNativeQuery(sql);
            query.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'exécution de la requête SQL: " + sql, e);
        }
    }

    private List<Object[]> getMySQLWarnings() {
        try {
            return entityManager.createNativeQuery("SHOW WARNINGS;").getResultList();
        } catch (Exception e) {
            logError(e);
            return List.of();
        }
    }

    private void logWarnings(List<Object[]> warnings) {
        try (PrintWriter out = new PrintWriter(new FileWriter("N:\\ITU\\S6\\EVAL\\RepriseCode\\crm\\conf-files\\importData\\error.log", true))) {
            out.println("⚠️ WARNINGS MySQL :");
            for (Object[] warning : warnings) {
                out.printf("Niveau: %s | Code: %s | Message: %s%n", warning[0], warning[1], warning[2]);
            }
            out.println("---------------------------------------------------");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void logError(Exception e) {
        try (PrintWriter out = new PrintWriter(new FileWriter("N:\\ITU\\S6\\EVAL\\RepriseCode\\crm\\conf-files\\importData\\error.log", true))) {
            e.printStackTrace(out);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    // @PostMapping("/import-csv")
    // @Transactional
    // public String treatImport(@Valid @ModelAttribute("csvfile") CSVFile csvfile,BindingResult result,Model model) throws Exception {
    //     if (result.hasErrors()) {
    //         importData();
    //         System.out.println("ERROR DE VALIDATION");
    //         return "dbconfigurations/import-csv";
    //     } 
    //     return "dbconfigurations/import-csv";
    // }
}
