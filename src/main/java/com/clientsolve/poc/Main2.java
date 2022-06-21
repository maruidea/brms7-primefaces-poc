package com.clientsolve.poc;
import com.juliusbaer.itasia.crm.service.AccountServiceImpl;
import com.myspace.mortgage_app.Applicant;
import com.myspace.mortgage_app.Application;
import com.myspace.mortgage_app.Property;


public class Main2 {

    public static void main(String[] args) {
        AccountServiceImpl accountService = new AccountServiceImpl();

        Application application = new Application();
        
        Applicant applicant = new Applicant();
        applicant.setAnnualincome(150000);
        
        Property property = new Property();
        property.setSaleprice(100000);
        property.setAge(1);
        property.setLocale("Urban");
        
        application.setApplicant(applicant);
        application.setProperty(property);
        
        Application app = accountService.testProcess(application);
        System.out.println(app.getMortgageamount());

        applicant.setAnnualincome(75000);
        property.setSaleprice(50000);
        property.setAge(1);
        property.setLocale("Rural");
        
        app = accountService.testProcess(application);
        System.out.println(app.getMortgageamount());
    }
}
