package site.easy.to.build.crm.model;


import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.AssertTrue;

public class CSVFile {

    MultipartFile csvFile1;
    MultipartFile csvFile2;
    MultipartFile csvFile3;

    @AssertTrue(message = "Insert a file")
    public boolean isFile1NotEmpty() {
        return csvFile1 != null && !csvFile1.isEmpty();
    }
    @AssertTrue(message = "Insert a file")
    public boolean isFile2NotEmpty() {
        return csvFile2 != null && !csvFile2.isEmpty();
    }
    @AssertTrue(message = "Insert a file")
    public boolean isFile3NotEmpty() {
        return csvFile3 != null && !csvFile3.isEmpty();
    }

    // Vérifie si le fichier a une extension .csv
    @AssertTrue(message = "Only CSV file are allowed")
    public boolean isCsvFile1() {
        if (csvFile1 != null && !csvFile1.isEmpty()) {
            String extension = FilenameUtils.getExtension(csvFile1.getOriginalFilename());
            return "csv".equalsIgnoreCase(extension);
        }
        return true; // Si aucun fichier n'est sélectionné, ne pas déclencher cette erreur
    }
    @AssertTrue(message = "Only CSV file are allowed")
    public boolean isCsvFile2() {
        if (csvFile2 != null && !csvFile2.isEmpty()) {
            String extension = FilenameUtils.getExtension(csvFile2.getOriginalFilename());
            return "csv".equalsIgnoreCase(extension);
        }
        return true; // Si aucun fichier n'est sélectionné, ne pas déclencher cette erreur
    }
    @AssertTrue(message = "Only CSV file are allowed")
    public boolean isCsvFile3() {
        if (csvFile3 != null && !csvFile3.isEmpty()) {
            String extension = FilenameUtils.getExtension(csvFile3.getOriginalFilename());
            return "csv".equalsIgnoreCase(extension);
        }
        return true; // Si aucun fichier n'est sélectionné, ne pas déclencher cette erreur
    }
    public CSVFile(){}
    public CSVFile(MultipartFile file1,MultipartFile file2,MultipartFile file3) throws Exception{ 
        this.setCsvFile1(file1);
        this.setCsvFile2(file2);
        this.setCsvFile3(file3);
    }

    public MultipartFile getCsvFile1(){
        return csvFile1;
    }
    public MultipartFile getCsvFile2(){
        return csvFile2;
    }
    public MultipartFile getCsvFile3(){
        return csvFile3;
    }

    public void setCsvFile1(MultipartFile file) throws Exception{
        csvFile1 = file;
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!extension.equalsIgnoreCase("csv")) {
            throw new Exception("Only scv file is allowed");
        }

    }
    public void setCsvFile2(MultipartFile file) throws Exception{
        csvFile2 = file;
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!extension.equalsIgnoreCase("csv")) {
            throw new Exception("Only scv file is allowed");
        }

    }
    public void setCsvFile3(MultipartFile file) throws Exception{
        csvFile3 = file;
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!extension.equalsIgnoreCase("csv")) {
            throw new Exception("Only scv file is allowed");
        }

    }
}
