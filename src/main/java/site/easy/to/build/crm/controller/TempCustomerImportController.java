package site.easy.to.build.crm.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import site.easy.to.build.crm.util.FileConverter;


@Controller
@RequestMapping("/import")
public class TempCustomerImportController {
    @GetMapping("/import-csv")
    @Transactional
    public String redirectImport(Model model) throws Exception {
        model.addAttribute("csvfile", new CSVFile());
        return "dbconfigurations/import-csv";
    }

    @PostMapping("/import-csv")
    public String importer(@Valid @ModelAttribute("csvfile") CSVFile csvfile,BindingResult result,Model model){
        if (result.hasErrors()) {
            System.out.println("ERROR DE VALIDATION :");
            result.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
            return "dbconfigurations/import-csv";
        } 
        try {
            MultipartFile fichier = csvfile.getCsvFile();
            File file= FileConverter.convertMultipartFileToFile(fichier);
            List<String> erreurs=new ArrayList<>();
            List<TempCustomerImport> rep=TempCustomerImport.lireEtVerifierCsv(erreurs,file);
            if (!erreurs.isEmpty()){
                model.addAttribute("validationErrors",erreurs);
                return "dbconfigurations/import-csv";
            }
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        return "dbconfigurations/import-csv";
    }
}
