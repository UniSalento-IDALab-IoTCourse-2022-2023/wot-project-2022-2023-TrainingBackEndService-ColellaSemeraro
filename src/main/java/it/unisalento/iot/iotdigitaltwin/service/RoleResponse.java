package it.unisalento.iot.iotdigitaltwin.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RoleResponse {

    public RoleResponse() {
        // Costruttore vuoto
    }

    private String role;
    private int status;

    public RoleResponse(int value, String authenticationFailed) {
        this.status = value;
        this.role = authenticationFailed;
    }

    // getter e setter per il campo role

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @JsonCreator
    public static RoleResponse createRoleResponse(@JsonProperty("role") String role, @JsonProperty("status") int status) {
        RoleResponse response = new RoleResponse();
        response.setRole(role);
        response.setStatus(status);
        return response;
    }
}

