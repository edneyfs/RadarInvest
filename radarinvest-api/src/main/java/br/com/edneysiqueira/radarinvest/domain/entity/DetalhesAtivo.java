package br.com.edneysiqueira.radarinvest.domain.entity;

import java.math.BigDecimal;

public class DetalhesAtivo {
    private String ticker;
    private String nome;
    private String setor;
    private BigDecimal cotacao;
    private BigDecimal dy;
    private BigDecimal pvp;
    private BigDecimal variacao12m;
    private BigDecimal vacancia;
    private BigDecimal valorPatrimonialPorCota;
    private String tipoFundo;
    private String cnpj;
    private Integer numeroCotistas;
    private BigDecimal liquidezDiaria;

    // Getters and Setters
    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public BigDecimal getCotacao() {
        return cotacao;
    }

    public void setCotacao(BigDecimal cotacao) {
        this.cotacao = cotacao;
    }

    public BigDecimal getDy() {
        return dy;
    }

    public void setDy(BigDecimal dy) {
        this.dy = dy;
    }

    public BigDecimal getPvp() {
        return pvp;
    }

    public void setPvp(BigDecimal pvp) {
        this.pvp = pvp;
    }

    public BigDecimal getVariacao12m() {
        return variacao12m;
    }

    public void setVariacao12m(BigDecimal variacao12m) {
        this.variacao12m = variacao12m;
    }

    public BigDecimal getVacancia() {
        return vacancia;
    }

    public void setVacancia(BigDecimal vacancia) {
        this.vacancia = vacancia;
    }

    public BigDecimal getValorPatrimonialPorCota() {
        return valorPatrimonialPorCota;
    }

    public void setValorPatrimonialPorCota(BigDecimal valorPatrimonialPorCota) {
        this.valorPatrimonialPorCota = valorPatrimonialPorCota;
    }

    public String getTipoFundo() {
        return tipoFundo;
    }

    public void setTipoFundo(String tipoFundo) {
        this.tipoFundo = tipoFundo;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Integer getNumeroCotistas() {
        return numeroCotistas;
    }

    public void setNumeroCotistas(Integer numeroCotistas) {
        this.numeroCotistas = numeroCotistas;
    }

    public BigDecimal getLiquidezDiaria() {
        return liquidezDiaria;
    }

    public void setLiquidezDiaria(BigDecimal liquidezDiaria) {
        this.liquidezDiaria = liquidezDiaria;
    }
}
