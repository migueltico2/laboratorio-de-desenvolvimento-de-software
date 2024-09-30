package com.example.project_2.model.DTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.project_2.model.Client;
import com.example.project_2.util.AuthUtil;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Component
public class ClientDTO extends UserDTO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RowMapper<ContractDTO> contractRowMapper;

    @Autowired
    private AuthUtil authUtil;

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

    @Schema(description = "Últimos três salários do cliente", example = "[5000.00, 5200.00, 5500.00]")
    private double[] lastThreeSalaries;

    public ClientDTO() {
        super();
    }

    public ClientDTO(Long id, String name, String email, String user_token, String role, String RG, String CPF,
            String address,
            String profession, String employer, double[] lastThreeSalaries) {
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

    public double[] getLastThreeSalaries() {
        return lastThreeSalaries;
    }

    public void setLastThreeSalaries(double[] lastThreeSalaries) {
        this.lastThreeSalaries = lastThreeSalaries;
    }

    public ClientDTO createClient(Client client) {
        try {
            System.out.println("Creating client: " + client.toString());
            String userSql = "INSERT INTO app_user (name, email, password, role) VALUES (?, ?, ?, ?) RETURNING id";
            Long userId = jdbcTemplate.queryForObject(userSql, Long.class,
                    client.getName(), client.getEmail(),
                    client.getPassword(), "CLIENT");

            String clientSql = "INSERT INTO client (rg, cpf, address, profession, employer, user_id, salary_one, salary_two, salary_three) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING *";
            jdbcTemplate.update(
                    clientSql,
                    client.getRG(),
                    client.getCPF(),
                    client.getAddress(),
                    client.getProfession(),
                    client.getEmployer(),
                    userId,
                    client.getLastThreeSalaries()[0],
                    client.getLastThreeSalaries()[1],
                    client.getLastThreeSalaries()[2]);

            return ClientDTO.fromClient(client);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public int requestRental(RentalDTO rentalDTO, String token) {
        String sql = "INSERT INTO contract (considerations, start_date, end_date, value, agent_id, vehicle_id, owner_id, client_id, status) "
                +
                "VALUES (?, ?::date, ?::date, ?, ?, ?, " +
                "(SELECT owner_id FROM vehicle v WHERE v.id = ?), " +
                "(SELECT c.id FROM client c JOIN app_user u ON c.user_id = u.id WHERE u.user_token = ?::uuid), " +
                "'PENDING') RETURNING id";

        return jdbcTemplate.update(sql,
                "",
                rentalDTO.getStartDate(),
                rentalDTO.getEndDate(),
                rentalDTO.getValue(),
                null,
                rentalDTO.getVehicleId(),
                rentalDTO.getVehicleId(),
                token);
    }

    public List<ContractDTO> getClientContracts(String token) {
        String sql = "SELECT * FROM contract WHERE contract.client_id = (SELECT id FROM app_user WHERE user_token = UUID(?))";
        return jdbcTemplate.query(sql, contractRowMapper, token);
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
        dto.setLastThreeSalaries(client.getLastThreeSalaries());
        return dto;
    }

    public Client toClient() {
        Client client = new Client();
        client.setId(this.getId());
        client.setName(this.getName());
        client.setEmail(this.getEmail());
        client.setUserToken(this.getUserToken());
        client.setRG(this.getRG());
        client.setCPF(this.getCPF());
        client.setAddress(this.getAddress());
        client.setProfession(this.getProfession());
        client.setEmployer(this.getEmployer());
        client.setLastThreeSalaries(this.getLastThreeSalaries());
        return client;
    }
}
