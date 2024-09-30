package com.example.project_2.model.DTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.project_2.model.Client;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ClientDTO extends UserDTO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final int NUMBER_OF_SALARIES = 3;

    @Schema(description = "RG do cliente", example = "18983332")
    private String RG;

    @Schema(description = "CPF do cliente", example = "123.456.789-00")
    private String CPF;

    @Schema(description = "Endereço do cliente", example = "Rua Example, 123")
    private String address;

    @Schema(description = "Profissão do cliente", example = "Engenheiro")
    private String profession;

    @Schema(description = "Empregador do cliente", example = "Empresa XYZ")
    private String employer;

    @Schema(description = "Média dos últimos três salários do cliente", example = "5233.33")
    private Double lastThreeSalaries;

    public ClientDTO() {
        super();
    }

    public ClientDTO(Long id, String name, String email, String user_token, String role, String RG, String CPF,
            String address,
            String profession, String employer, Double lastThreeSalaries) {
        super(id, name, email, user_token, role);
        this.RG = RG;
        this.CPF = CPF;
        this.address = address;
        this.profession = profession;
        this.employer = employer;
        this.lastThreeSalaries = lastThreeSalaries;
    }

    // Getters e Setters

    public String getRG() {
        return RG;
    }

    public void setRG(String RG) {
        this.RG = RG;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public Double getLastThreeSalaries() {
        return lastThreeSalaries;
    }

    public void setLastThreeSalaries(Double lastThreeSalaries) {
        this.lastThreeSalaries = lastThreeSalaries;
    }

    public ClientDTO createClient(Client client) {
        try {
            System.out.println("Creating client: " + client.toString());
            String userSql = "INSERT INTO app_user (name, email, password, role) VALUES (?, ?, ?, ?) RETURNING id";
            Long userId = jdbcTemplate.queryForObject(userSql, Long.class,
                    client.getName(), client.getEmail(),
                    client.getPassword(), "CLIENT");

            Double averageSalary = calculateAverageSalary(client.getLastThreeSalaries());

            String clientSql = "INSERT INTO client (id, cpf, rg, address, profession, employer, last_three_salaries) VALUES (?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(
                    clientSql,
                    userId,
                    client.getCPF(),
                    client.getRG(),
                    client.getAddress(),
                    client.getProfession(),
                    client.getEmployer(),
                    averageSalary);

            // Create a new ClientDTO with the inserted data
            ClientDTO newClientDTO = new ClientDTO();
            newClientDTO.setId(userId);
            newClientDTO.setName(client.getName());
            newClientDTO.setEmail(client.getEmail());
            newClientDTO.setCPF(client.getCPF());
            newClientDTO.setRG(client.getRG());
            newClientDTO.setAddress(client.getAddress());
            newClientDTO.setProfession(client.getProfession());
            newClientDTO.setEmployer(client.getEmployer());
            newClientDTO.setLastThreeSalaries(averageSalary);

            return newClientDTO;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateClient(Long clientId, Map<String, Object> fields) {
        if (fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException("Nenhum campo fornecido para atualização");
        }

        // Filtrar campos nulos e criar conjunto de parâmetros
        Map<String, Object> nonNullFields = fields.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (nonNullFields.isEmpty()) {
            throw new IllegalArgumentException("Todos os campos fornecidos são nulos");
        }

        // Construir a query SQL dinamicamente
        StringBuilder sqlBuilder = new StringBuilder("UPDATE client SET ");
        sqlBuilder.append(nonNullFields.keySet().stream()
                .map(key -> key + " = :" + key)
                .collect(Collectors.joining(", ")));
        sqlBuilder.append(" WHERE id = :id");

        // Adicionar o ID do cliente aos parâmetros
        nonNullFields.put("id", clientId);

        // Criar o objeto de parâmetros nomeados
        MapSqlParameterSource parameters = new MapSqlParameterSource(nonNullFields);

        // Executar a atualização
        int rowsAffected = namedParameterJdbcTemplate.update(sqlBuilder.toString(), parameters);

        return rowsAffected > 0;
    }

    public static ClientDTO fromClient(Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setName(client.getName());
        dto.setEmail(client.getEmail());
        dto.setUserToken(client.getUserToken());
        dto.setRG(client.getRG());
        dto.setCPF(client.getCPF());
        dto.setAddress(client.getAddress());
        dto.setProfession(client.getProfession());
        dto.setEmployer(client.getEmployer());
        dto.setLastThreeSalaries(calculateAverageSalary(client.getLastThreeSalaries()));
        return dto;
    }

    private static double calculateAverageSalary(double[] salaries) {
        if (salaries == null || salaries.length == 0) {
            return 0.0;
        }
        double sum = 0;
        for (double salary : salaries) {
            sum += salary;
        }
        return sum / NUMBER_OF_SALARIES;
    }
}
