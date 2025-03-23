package site.easy.to.build.crm.model;


import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.AssertTrue;

public class CSVFile {

    MultipartFile csvFile;

    @AssertTrue(message = "Insert a file")
    public boolean isFileNotEmpty() {
        return csvFile != null && !csvFile.isEmpty();
    }

    // Vérifie si le fichier a une extension .csv
    @AssertTrue(message = "Only CSV file are allowed")
    public boolean isCsvFile() {
        if (csvFile != null && !csvFile.isEmpty()) {
            String extension = FilenameUtils.getExtension(csvFile.getOriginalFilename());
            return "csv".equalsIgnoreCase(extension);
        }
        return true; // Si aucun fichier n'est sélectionné, ne pas déclencher cette erreur
    }
    public CSVFile(){}
    public CSVFile(MultipartFile file) throws Exception{ this.setCsvFile(file);}

    public MultipartFile getCsvFile(){
        return csvFile;
    }

    public void setCsvFile(MultipartFile file) throws Exception{
        csvFile = file;
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!extension.equalsIgnoreCase("csv")) {
            throw new Exception("Only scv file is allowed");
        }

    }
}
