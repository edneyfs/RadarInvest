package br.com.edneysiqueira.radarinvest.infrastructure.annotation;

public enum SensitiveType {
    HIDE, // Substitui por *** (Padrão)
    MASK_ALL_BUT_FIRST_3, // Mostra tudo, menos os 3 primeiros caracteres (123***)
    MASK_ALL_BUT_LAST_3, // Mascara tudo, menos os 3 últimos caracteres (***789)
    MASK_CPF // Formato específico para CPF (***.***.123-45)
}
