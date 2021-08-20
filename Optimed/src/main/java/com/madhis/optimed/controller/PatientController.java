package com.madhis.optimed.controller;

import com.madhis.optimed.entity.Consult;
import com.madhis.optimed.entity.Patient;
import com.madhis.optimed.service.ConsultService;
import com.madhis.optimed.service.PatientService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller

public class PatientController {
    
    @Autowired
    private PatientService patientService;

    @Autowired
    private ConsultService consultService;
    
       @RequestMapping(value = "/",method = {RequestMethod.GET}) 
        public String index(){
            return "index";
        }
    
       @RequestMapping(value = "/patients",method = {RequestMethod.GET}) 
        public String viewAllPatients(Model model){
            List<Patient> listPatients = patientService.fetchPatientList();
            model.addAttribute("listPatients",listPatients);
            return "patients";
        }
    
        @RequestMapping(value="/new_patient",method = {RequestMethod.GET})
        public String newPatientForm(Model model){
            Patient patient = new Patient();
            model.addAttribute("patient", patient);
            return "new_patient";
        }
   
	@RequestMapping(value="/consult/{id}",method = {RequestMethod.GET})
        public String showConsultForm(Model model,@PathVariable(value = "id") Long patientId){
		model.addAttribute(patientService.getPatientById(patientId));
		return "consult";
	}	
        
	@RequestMapping(value="/delete/{id}")
	public String deletePatientId(@PathVariable(value="id") Long patientId){
            patientService.deletePatientById(patientId);
            return "redirect:/patients";  
	    
	}               
       
	@PostMapping("/savepatients")
        public Patient savePatient(@RequestBody Patient patient){
            return patientService.savePatient(patient);
	}

	//save patient form and redirect to consults page
        @RequestMapping(value="/save_patient",method = {RequestMethod.POST})
        public String submitForm(Model model, @ModelAttribute("patient") Patient patient) {
    	    Consult consult = new Consult(); 
	    model.addAttribute("consult",consult);
            patientService.savePatient(patient);
	    return "consult";
        }

	//dispense 
	@RequestMapping(value="/save_consult/{id}",method = {RequestMethod.POST})
	    public String dispenseForm(@ModelAttribute("consult") Consult consult, @PathVariable(value="id") Long patientId){ 
		  
		  Patient patient = patientService.getPatientById(patientId);
		  patient.getConsults().add(consult);
    	          consultService.addConsult(consult);
		  return "dispense";
        }
}
