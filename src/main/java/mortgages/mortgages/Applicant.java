package mortgages.mortgages;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * This class was automatically generated by the data modeler tool.
 */
@ManagedBean
@SessionScoped
public class Applicant extends Version implements java.io.Serializable {

    static final long serialVersionUID = 1L;

    private Integer age;

    private java.util.Date applicationDate;

    private Boolean approved;

    private String creditRating;

    private String name;
    
    private List<LoanApplication> loanApplicationList;

    public List<LoanApplication> getLoanApplicationList() {
        return loanApplicationList;
    }

    public void setLoanApplicationList(List<LoanApplication> loanApplicationList) {
        this.loanApplicationList = loanApplicationList;
    }

    public Applicant() {
    }
    
    public Applicant(Integer age, java.util.Date applicationDate, String creditRating, String name, Boolean approved) {
        this.age = age;
        this.applicationDate = applicationDate;
        this.creditRating = creditRating;
        this.name = name;
        this.approved = approved;
    }

    public Integer getAge() {
        return this.age;
    }
    
    public void setAge(Integer age) {
        this.age = age;
    }

    public java.util.Date getApplicationDate() {
        return this.applicationDate;
    }
    
    public void setApplicationDate(java.util.Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Boolean getApproved() {
        return this.approved;
    }
    
    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public String getCreditRating() {
        return this.creditRating;
    }
    
    public void setCreditRating(String creditRating) {
        this.creditRating = creditRating;
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

}