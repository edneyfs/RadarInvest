package br.com.edneysiqueira.radarinvest.application.service;

import br.com.edneysiqueira.radarinvest.api.dto.AdminDashboardDTO;
import br.com.edneysiqueira.radarinvest.domain.repository.ItemWatchlistRepository;
import br.com.edneysiqueira.radarinvest.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UsuarioRepository usuarioRepository;
    private final ItemWatchlistRepository itemWatchlistRepository;

    public AdminDashboardDTO getDashboardStats() {
        long totalUsers = usuarioRepository.countByRoleNot("ADMIN");
        // Online users: seen in the last 10 minutes (excluding Admins)
        long onlineUsers = usuarioRepository.countByLastSeenAfterAndRoleNot(LocalDateTime.now().minusMinutes(10), "ADMIN");
        long monitoredTickers = itemWatchlistRepository.countDistinctTickers();

        return new AdminDashboardDTO(totalUsers, onlineUsers, monitoredTickers);
    }
}
