package gbrstd.clinica.controller;

import gbrstd.clinica.mail.EmailServiceImpl;
import gbrstd.clinica.model.Clinica;
import gbrstd.clinica.model.Endereco;
import gbrstd.clinica.model.Paciente;
import gbrstd.clinica.service.ClinicaService;
import gbrstd.clinica.service.PacienteService;
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
public class PacienteController {

    private final ClinicaService clinicaService;
    private final PacienteService pacienteService;

    private final EmailServiceImpl emailService;

    public PacienteController(ClinicaService clinicaService, PacienteService pacienteService, EmailServiceImpl emailService) {
        this.clinicaService = clinicaService;
        this.pacienteService = pacienteService;
        this.emailService = emailService;
    }

    @GetMapping("/clinica/{clinicaId}/paciente")
    public String listAllPacientes(@PathVariable("clinicaId") Long clinicaId, Model model) {
        model.addAttribute("pacientes", clinicaService.findById(clinicaId).getPacientes());
        model.addAttribute("clinicaId", clinicaId);
        return "paciente/listPacientes";
    }


    @GetMapping("/clinica/{clinicaId}/paciente/create")
    public String createPacienteForm(@PathVariable("clinicaId") Long clinicaId,
                                     HttpSession session,
                                     Model model) {

        final Clinica clinica = clinicaService.findById(clinicaId);
        session.setAttribute("currentClinica", clinica);

        model.addAttribute("pacienteForm", new Paciente());
        model.addAttribute("enderecoForm", new Endereco());
        model.addAttribute("clinicaId", clinicaId);

        return "paciente/createPacienteForm";
    }

    @GetMapping("/paciente/{pacienteId}/edit")
    public String editPacienteForm(@PathVariable("pacienteId") Long id,
                                   HttpSession session,
                                   Model model) {

        Paciente paciente = pacienteService.findById(id);

        session.setAttribute("enderecoId", paciente.getEndereco().getId());
        session.setAttribute("currentClinica", paciente.getClinica());

        model.addAttribute("pacienteForm", paciente);
        model.addAttribute("clinicaId", paciente.getClinica().getId());

        return "paciente/editPacienteForm";
    }

    @PostMapping("/paciente/{pacienteId}/delete")
    public String deletePaciente(@PathVariable("pacienteId") Long id) {
        Paciente paciente = pacienteService.delete(id);
        return "redirect:/clinica/" + paciente.getClinica().getId() + "/paciente";
    }

    @PostMapping("/clinica/{clinicaId}/paciente/create")
    public String createPacienteSubmit(@ModelAttribute("pacienteForm") @Valid Paciente paciente,
                                       BindingResult pacienteResult,
                                       @ModelAttribute("enderecoForm") @Valid Endereco endereco,
                                       BindingResult enderecoResult,
                                       @PathVariable("clinicaId") Long clinicaId,
                                       HttpSession session,
                                       SessionStatus status,
                                       Model model) {

        if (pacienteResult.hasErrors() || enderecoResult.hasErrors()) {
            model.addAttribute("pacienteForm", paciente);
            model.addAttribute("enderecoForm", endereco);
            model.addAttribute("clinicaId", clinicaId);

            return "paciente/createPacienteForm";
        }

        paciente.setEndereco(endereco);
        paciente.setClinica((Clinica) session.getAttribute("currentClinica"));

        pacienteService.save(paciente);

        status.setComplete();
        session.removeAttribute("currentClinica");

        emailService.sendSimpleMail(paciente.getEmail(), "Cadastro de Paciente", "Olá " + paciente.getNome() + ", seu cadastro foi realizado com sucesso!");

        return "redirect:/clinica/" + clinicaId + "/paciente";
    }

    @PostMapping("/paciente/{pacienteId}/edit")
    public String editPacienteSubmit(@ModelAttribute("pacienteForm") @Valid Paciente paciente,
                                     BindingResult pacienteResult,
                                     HttpSession session,
                                     SessionStatus status,
                                     Model model) {

        Clinica clinica = (Clinica) session.getAttribute("currentClinica");

        if (pacienteResult.hasErrors()) {
            model.addAttribute("pacienteForm", paciente);
            model.addAttribute("clinicaId", clinica.getId());

            return "paciente/editPacienteForm";
        }

        paciente.setClinica(clinica);
        paciente.getEndereco().setId((Long) session.getAttribute("enderecoId"));

        pacienteService.save(paciente);

        status.setComplete();
        session.removeAttribute("enderecoId");
        session.removeAttribute("tempClinica");

        return "redirect:/clinica/" + clinica.getId() + "/paciente";
    }


}
