package br.com.edneysiqueira.radarinvest.api.controller;

import br.com.edneysiqueira.radarinvest.api.dto.AdminDashboardDTO;
import br.com.edneysiqueira.radarinvest.application.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("apiAdminController")
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Endpoints Administrativos")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    @Operation(summary = "Obter estatísticas do dashboard", description = "Retorna total de usuários, usuários online e ativos monitorados.")
    public AdminDashboardDTO getDashboard() {
        return adminService.getDashboardStats();
    }
}
