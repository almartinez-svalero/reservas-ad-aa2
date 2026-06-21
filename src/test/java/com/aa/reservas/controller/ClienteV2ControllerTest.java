package com.aa.reservas.controller;

import com.aa.reservas.exception.GlobalExceptionHandler;
import com.aa.reservas.model.Cliente;
import com.aa.reservas.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteV2Controller.class)
@Import(GlobalExceptionHandler.class)
class ClienteV2ControllerTest {
    @Autowired MockMvc mockMvc;
    @MockBean ClienteService service;

    @Test
    void findAll_v2DevuelveSalidaResumida() throws Exception {
        when(service.findAll(any(), any(), any())).thenReturn(List.of(cliente()));

        mockMvc.perform(get("/api/v2/clientes"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].telefonoContacto").value("+34666000111"))
            .andExpect(jsonPath("$[0].nivel").value("VIP"))
            .andExpect(jsonPath("$[0].deudaPendiente").value(false));
    }

    @Test
    void create_v2PermiteFechaRegistroOpcional() throws Exception {
        when(service.create(any(Cliente.class))).thenReturn(cliente());

        mockMvc.perform(post("/api/v2/clientes")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
{"nombre":"Laura","email":"laura@email.com","telefono":"+34666000111","numeroVisitas":5,"saldoPendiente":0,"vip":true}
"""))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nivel").value("VIP"));
    }

    @Test
    void update_v2DevuelveDtoResumido() throws Exception {
        when(service.update(eq(1L), any(Cliente.class))).thenReturn(cliente());

        mockMvc.perform(put("/api/v2/clientes/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
{"nombre":"Laura","email":"laura@email.com","telefono":"+34666000111","numeroVisitas":5,"saldoPendiente":0,"vip":true}
"""))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.telefonoContacto").value("+34666000111"));
    }

    @Test
    void delete_v2DevuelveConfirmacion() throws Exception {
        mockMvc.perform(delete("/api/v2/clientes/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.recurso").value("cliente"))
            .andExpect(jsonPath("$.mensaje").value("Cliente eliminado correctamente en API v2"));

        verify(service).delete(1L);
    }

    private Cliente cliente() {
        return new Cliente(1L, "Laura", "laura@email.com", "+34666000111", 5, 0f, true, LocalDate.now());
    }
}
