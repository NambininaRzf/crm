package site.easy.to.build.crm.model.dto;

import site.easy.to.build.crm.entity.TauxStorage;

public class ConfigRequest {
    private String username;
    private String token;
    private TauxStorage tauxUpdate;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setTauxUpdate(TauxStorage taux){
        this.tauxUpdate = taux;
    }
    public TauxStorage getTauxStorage(){
        return this.tauxUpdate;
    }
}
