package io.github.rtheodoro4201.eventconnect.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUserService {

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication != null) && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // Assumindo que seu UserDetails implementa alguma forma de obter o ID
            // Isso pode variar dependendo da sua implementação de segurança
            // Exemplo: return ((MeuUsuarioPersonalizado) userDetails).getId();
            return null; // Substitua pela sua lógica real
        }
        return null; // Ou lançar uma exceção se não houver usuário autenticado
    }
}
