package com.example.clinica.controller;

import com.example.clinica.model.Clinica;
import com.example.clinica.model.Consulta;
import com.example.clinica.service.ClinicaService;
import com.example.clinica.service.ConsultaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class ConsultaController {

    private final ConsultaService consultaService;
    private final ClinicaService clinicaService;

    public ConsultaController(ConsultaService consultaService, ClinicaService clinicaService) {
        this.consultaService = consultaService;
        this.clinicaService = clinicaService;
    }

    @GetMapping("/clinica/{clinicaId}/consulta")
    public String listAllConsultas(@PathVariable("clinicaId") Long clinicaId, Model model) {
        model.addAttribute("consultas", clinicaService.findById(clinicaId).getConsultas());
        return "consulta/listConsultas";
    }

    @GetMapping("/clinica/{clinicaId}/consulta/create")
    public String createConsultaForm(@PathVariable("clinicaId") Long clinicaId,
                                     HttpSession session,
                                     Model model) {

        Clinica clinica = clinicaService.findById(clinicaId);
        session.setAttribute("currentClinica", clinica);

        final Consulta consulta = new Consulta();
        consulta.setClinica(clinica);

        model.addAttribute("consultaForm", consulta);
        model.addAttribute("especialistas", clinica.getEspecialistas());
        model.addAttribute("pacientes", clinica.getPacientes());
        model.addAttribute("clinicaId", clinicaId);

        return "consulta/createConsultaForm";
    }

    @GetMapping("/consulta/{consultaId}/edit")
    public String editConsultaForm(@PathVariable("consultaId") Long id,
                                   HttpSession session,
                                   Model model) {

        Consulta consulta = consultaService.findById(id);

        session.setAttribute("currentClinica", consulta.getClinica());

        model.addAttribute("consultaForm", consulta);
        model.addAttribute("especialistas", consulta.getClinica().getEspecialistas());
        model.addAttribute("pacientes", consulta.getClinica().getPacientes());
        model.addAttribute("clinicaId", consulta.getClinica().getId());

        return "consulta/editConsultaForm";
    }

    @PostMapping("/consulta/{id}/delete")
    public String deleteConsulta(@PathVariable("id") Long id) {
        Consulta consulta = consultaService.delete(id);
        return "redirect:/clinica/" + consulta.getClinica().getId() + "/consulta";
    }

    @PostMapping("/clinica/{clinicaId}/consulta/create")
    public String createConsultaSubmit(@PathVariable("clinicaId") Long clinicaId,
                                       @ModelAttribute("consultaForm") @Valid Consulta consulta,
                                       BindingResult consultaResult,
                                       HttpSession session,
                                       SessionStatus status,
                                       Model model) {

        consulta.setClinica((Clinica) session.getAttribute("currentClinica"));

        if (consultaResult.hasErrors()) {
            model.addAttribute("consultaForm", consulta);
            model.addAttribute("especialistas", consulta.getClinica().getEspecialistas());
            model.addAttribute("pacientes", consulta.getClinica().getPacientes());
            model.addAttribute("clinicaId", clinicaId);

            return "consulta/createConsultaForm";
        }

        consultaService.save(consulta);

        status.setComplete();
        session.removeAttribute("currentClinica");

        return "redirect:/clinica/" + clinicaId + "/consulta";
    }

    @PostMapping("/consulta/{consultaId}/edit")
    public String editConsultaSubmit(@ModelAttribute("consultaForm") @Valid Consulta consulta,
                                     BindingResult consultaResult,
                                     HttpSession session,
                                     SessionStatus status,
                                     Model model) {

        consulta.setClinica((Clinica) session.getAttribute("currentClinica"));

        if (consultaResult.hasErrors()) {
            model.addAttribute("consultaForm", consulta);
            model.addAttribute("especialistas", consulta.getClinica().getEspecialistas());
            model.addAttribute("pacientes", consulta.getClinica().getPacientes());
            model.addAttribute("clinicaId", consulta.getClinica().getId());

            return "consulta/editConsultaForm";
        }

        consultaService.save(consulta);

        status.setComplete();
        session.removeAttribute("currentClinica");

        return "redirect:/clinica/" + consulta.getClinica().getId() + "/consulta";
    }

}
