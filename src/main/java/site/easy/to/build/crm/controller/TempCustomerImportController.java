package site.easy.to.build.crm.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import site.easy.to.build.crm.model.CSVFile;
import site.easy.to.build.crm.model.TempCustomerImport;
import site.easy.to.build.crm.model.importcsv.Data1Validator;
import site.easy.to.build.crm.model.importcsv.Data2Validator;
import site.easy.to.build.crm.model.importcsv.Data3Validator;
import site.easy.to.build.crm.service.importcsv.Data1ValidatorService;
import site.easy.to.build.crm.service.importcsv.Data2ValidatorService;
import site.easy.to.build.crm.service.importcsv.Data3ValidatorService;
import site.easy.to.build.crm.util.FileConverter;


@Controller
@RequestMapping("/import")
public class TempCustomerImportController {
    public final Data1ValidatorService data1ValidatorService;
    public final Data2ValidatorService data2ValidatorService;
    public final Data3ValidatorService data3ValidatorService;

    @Autowired
    public TempCustomerImportController (Data1ValidatorService data1ValidatorService,Data2ValidatorService data2ValidatorService,
        Data3ValidatorService data3ValidatorService){
            this.data1ValidatorService = data1ValidatorService;
            this.data2ValidatorService = data2ValidatorService;
            this.data3ValidatorService = data3ValidatorService;
        }

    
    @GetMapping("/import-csv")
    @Transactional
    public String redirectImport(Model model) throws Exception {
        model.addAttribute("csvfile", new CSVFile());
        return "dbconfigurations/import-csv";
    }

    @PostMapping("/import-csv")
    public String importer(@Valid @ModelAttribute("csvfile") CSVFile csvfile,BindingResult result,Model model,Authentication authentication){
        if (result.hasErrors()) {
            System.out.println("ERROR DE VALIDATION :");
            result.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
            return "dbconfigurations/import-csv";
        } 
        try {
            // traitement fichier 1
            MultipartFile fichier1 = csvfile.getCsvFile1();
            File file1= FileConverter.convertMultipartFileToFile(fichier1);
            List<String> erreurs1=new ArrayList<>();
            List<Data1Validator> rep1=Data1Validator.lireEtVerifierCsv(erreurs1,file1);

            // traitement fichier 2
            MultipartFile fichier2 = csvfile.getCsvFile2();
            File file2= FileConverter.convertMultipartFileToFile(fichier2);
            List<String> erreurs2=new ArrayList<>();
            List<Data2Validator> rep2=Data2Validator.lireEtVerifierCsv(erreurs2,file2);

            // traitement fichier 3
            MultipartFile fichier3 = csvfile.getCsvFile3();
            File file3= FileConverter.convertMultipartFileToFile(fichier3);
            List<String> erreurs3=new ArrayList<>();
            List<Data3Validator> rep3=Data3Validator.lireEtVerifierCsv(erreurs3,file3);


            int count = 0;
            if (!erreurs1.isEmpty()){
                model.addAttribute("validationErrors1",erreurs1);
                count ++;
            }if (!erreurs2.isEmpty()) {
                model.addAttribute("validationErrors2",erreurs2);
                count ++;
            }if (!erreurs3.isEmpty()) {
                model.addAttribute("validationErrors3",erreurs3);
                count ++;
            }

            // si 0 erreur on procède à l'insertion
            if (count == 0) {
                try {
                    data2ValidatorService.insertListData(authentication, rep2);
                    data1ValidatorService.insertListData(authentication, rep1);
                    data3ValidatorService.insertListData(authentication, rep3);
                } catch (Exception e) {
                   e.printStackTrace();
                }
            }

            // insertion des données

            return "dbconfigurations/import-csv";
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        return "dbconfigurations/import-csv";
    }
}
