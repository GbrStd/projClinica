package gbrstd.clinica.controller;

import gbrstd.clinica.model.Clinica;
import gbrstd.clinica.model.Endereco;
import gbrstd.clinica.model.Especialista;
import gbrstd.clinica.service.ClinicaService;
import gbrstd.clinica.service.EspecialistaService;
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
public class EspecialistaController {

    private final ClinicaService clinicaService;
    private final EspecialistaService especialistaService;

    public EspecialistaController(ClinicaService clinicaService, EspecialistaService especialistaService) {
        this.clinicaService = clinicaService;
        this.especialistaService = especialistaService;
    }

    @GetMapping("/clinica/{clinicaId}/especialista")
    public String listAllEspecialistas(@PathVariable("clinicaId") Long clinicaId, Model model) {
        model.addAttribute("especialistas", clinicaService.findById(clinicaId).getEspecialistas());
        model.addAttribute("clinicaId", clinicaId);
        return "especialista/listEspecialistas";
    }

    @GetMapping("/clinica/{clinicaId}/especialista/create")
    public String createEspecialistaForm(@PathVariable("clinicaId") Long clinicaId,
                                         HttpSession session,
                                         Model model) {

        final Clinica clinica = clinicaService.findById(clinicaId);
        session.setAttribute("currentClinica", clinica);

        model.addAttribute("especialistaForm", new Especialista());
        model.addAttribute("enderecoForm", new Endereco());
        model.addAttribute("clinicaId", clinicaId);

        return "especialista/createEspecialistaForm";
    }

    @GetMapping("/especialista/{especialistaId}/edit")
    public String editEspecialistaForm(@PathVariable("especialistaId") Long id,
                                       HttpSession session,
                                       Model model) {

        Especialista especialista = especialistaService.findById(id);

        session.setAttribute("enderecoId", especialista.getEndereco().getId());
        session.setAttribute("currentClinica", especialista.getClinica());

        model.addAttribute("especialistaForm", especialista);
        model.addAttribute("clinicaId", especialista.getClinica().getId());

        return "especialista/editEspecialistaForm";
    }

    @PostMapping("/especialista/{especialistaId}/delete")
    public String deleteEspecialista(@PathVariable("especialistaId") Long id) {
        final Especialista especialista = especialistaService.delete(id);
        return "redirect:/clinica/" + especialista.getClinica().getId() + "/especialista";
    }

    @PostMapping("/clinica/{clinicaId}/especialista/create")
    public String createEspecialistaSubmit(@ModelAttribute("especialistaForm") @Valid Especialista especialista,
                                           BindingResult especialistaResult,
                                           @ModelAttribute("enderecoForm") @Valid Endereco endereco,
                                           BindingResult enderecoResult,
                                           @PathVariable("clinicaId") Long clinicaId,
                                           HttpSession session,
                                           SessionStatus status,
                                           Model model) {

        if (especialistaResult.hasErrors() || enderecoResult.hasErrors()) {
            model.addAttribute("especialistaForm", especialista);
            model.addAttribute("enderecoForm", endereco);
            model.addAttribute("clinicaId", clinicaId);

            return "especialista/createEspecialistaForm";
        }

        especialista.setEndereco(endereco);
        especialista.setClinica((Clinica) session.getAttribute("currentClinica"));

        especialistaService.save(especialista);

        status.setComplete();
        session.removeAttribute("currentClinica");

        return "redirect:/clinica/" + clinicaId + "/especialista";
    }

    @PostMapping("/especialista/{especialistaId}/edit")
    public String editEspecialistaSubmit(@ModelAttribute("especialistaForm") @Valid Especialista especialista,
                                         BindingResult especialistaResult,
                                         HttpSession session,
                                         SessionStatus status,
                                         Model model) {

        Clinica clinica = (Clinica) session.getAttribute("currentClinica");

        if (especialistaResult.hasErrors()) {
            model.addAttribute("especialistaForm", especialista);
            model.addAttribute("clinicaId", clinica.getId());

            return "especialista/editEspecialistaForm";
        }

        especialista.setClinica(clinica);
        especialista.getEndereco().setId((Long) session.getAttribute("enderecoId"));

        especialistaService.save(especialista);

        status.setComplete();
        session.removeAttribute("enderecoId");
        session.removeAttribute("tempClinica");

        return "redirect:/clinica/" + clinica.getId() + "/especialista";
    }

}
