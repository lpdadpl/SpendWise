package com.hazerta.SpendwiseBack.config;

import com.hazerta.SpendwiseBack.entity.*;
import com.hazerta.SpendwiseBack.model.Role;
import com.hazerta.SpendwiseBack.model.TransactionKind;
import com.hazerta.SpendwiseBack.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@Profile("!prod")
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TransactionTypeRepository transactionTypeRepository;
    private final TransactionRepository transactionRepository;
    private final UserFollowRepository userFollowRepository;
    private final ForecastTemplateRepository forecastTemplateRepository;
    private final ForecastTemplateOwnedRepository forecastTemplateOwnedRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository,
            TransactionTypeRepository transactionTypeRepository,
            TransactionRepository transactionRepository,
            UserFollowRepository userFollowRepository,
            ForecastTemplateRepository forecastTemplateRepository,
            ForecastTemplateOwnedRepository forecastTemplateOwnedRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.transactionTypeRepository = transactionTypeRepository;
        this.transactionRepository = transactionRepository;
        this.userFollowRepository = userFollowRepository;
        this.forecastTemplateRepository = forecastTemplateRepository;
        this.forecastTemplateOwnedRepository = forecastTemplateOwnedRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("Cargando/Actualizando datos iniciales...");

        // 1. Crear o Actualizar Usuarios
        User admin = createOrUpdateUser("AdminUser", "admin@spendwise.com", "EscoAdmin3001", Role.ADMIN);
        User alberto = createOrUpdateUser("AlbertoUser", "alberto@spendwise.com", "EscoAlberto3001", Role.USER);

        // 2. Crear Tipos de Transacción (antes Categorías) para Alberto (si no existen)
        TransactionType salarioType = createTransactionType("Salario", TransactionKind.INCOME, alberto);
        createTransactionType("Alquiler", TransactionKind.EXPENSE, alberto);
        TransactionType comidaType = createTransactionType("Comida", TransactionKind.EXPENSE, alberto);

        // 3. Crear Transacciones para Alberto
        createTransaction("Nómina de Diciembre", new BigDecimal("3500.00"), TransactionKind.INCOME, alberto,
                salarioType);
        createTransaction("Compra supermercado", new BigDecimal("85.50"), TransactionKind.EXPENSE, alberto, comidaType);
        createTransaction("Cena con amigos", new BigDecimal("45.00"), TransactionKind.EXPENSE, alberto, comidaType);

        // 4. Crear Plantilla de Presupuesto para Alberto
        boolean templateExists = forecastTemplateOwnedRepository.findByUser_Username(alberto.getUsername())
                .stream()
                .anyMatch(o -> "Presupuesto Mensual Básico".equals(o.getTemplate().getName()));

        if (!templateExists) {
            ForecastTemplate template = new ForecastTemplate();
            template.setName("Presupuesto Mensual Básico");
            template.setDescription("Plantilla para seguimiento de gastos e ingresos recurrentes.");
            template.setIsPublic(true);
            template.setCreatedAt(LocalDateTime.now());
            template.setStructure(
                    "{\"type\": \"table\", \"rows\": [{\"name\": \"Ingresos por Salario\", \"transaction_ref_id\": null, \"transaction_kind\": \"INCOME\", \"amount\": 3500.00}, {\"name\": \"Gastos en Comida\", \"transaction_ref_id\": null, \"transaction_kind\": \"EXPENSE\", \"amount\": 500.00}]}");
            forecastTemplateRepository.save(template);

            ForecastTemplateOwned owned = new ForecastTemplateOwned();
            owned.setUser(alberto);
            owned.setTemplate(template);
            forecastTemplateOwnedRepository.save(owned);
            System.out.println("Plantilla 'Presupuesto Mensual Básico' creada para " + alberto.getUsername());
        } else {
            System.out.println("La plantilla 'Presupuesto Mensual Básico' ya existe para " + alberto.getUsername());
        }

        // 5. Crear User Follow (Alberto sigue a Admin)
        UserFollow follow = new UserFollow();
        follow.setFollower(alberto);
        follow.setFollowed(admin);
        follow.setCreatedAt(LocalDateTime.now());
        userFollowRepository.save(follow);

        System.out.println("Datos iniciales cargados exitosamente.");
    }

    private User createOrUpdateUser(String username, String email, String password, Role role) {
        return userRepository.findByUsernameOrEmail(username, email)
                .map(existingUser -> {
                    System.out.println("Actualizando usuario: " + username);
                    existingUser.setPassword(passwordEncoder.encode(password));
                    existingUser.setRole(role);
                    existingUser.setEmail(email);
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    System.out.println("Creando usuario: " + username);
                    User newUser = new User();
                    newUser.setUsername(username);
                    newUser.setEmail(email);
                    newUser.setPassword(passwordEncoder.encode(password));
                    newUser.setRole(role);
                    newUser.setCreatedAt(LocalDateTime.now());
                    return userRepository.save(newUser);
                });
    }

    private TransactionType createTransactionType(String name, TransactionKind kind, User user) {
        TransactionType type = new TransactionType();
        type.setName(name);
        type.setKind(kind);
        type.setUser(user);
        return transactionTypeRepository.save(type);
    }

    private void createTransaction(String description, BigDecimal amount, TransactionKind kind, User user,
            TransactionType transactionType) {
        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);
        transaction.setTransactionKind(kind);
        transaction.setUser(user);
        transaction.setTransactionType(transactionType);
        transactionRepository.save(transaction);
    }
}
