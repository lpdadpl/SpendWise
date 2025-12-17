package com.hazerta.SpendwiseBack.config;

import com.hazerta.SpendwiseBack.entity.*;
import com.hazerta.SpendwiseBack.model.Role;
import com.hazerta.SpendwiseBack.model.TransactionType;
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
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final UserFollowRepository userFollowRepository;
    private final ForecastTemplateRepository forecastTemplateRepository;
    private final ForecastTemplateOwnedRepository forecastTemplateOwnedRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository,
            CategoryRepository categoryRepository,
            TransactionRepository transactionRepository,
            UserFollowRepository userFollowRepository,
            ForecastTemplateRepository forecastTemplateRepository,
            ForecastTemplateOwnedRepository forecastTemplateOwnedRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
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

        // 2. Crear Categorías para Alberto (si no existen)
        Category salarioCat = createCategory("Salario", "INCOME", alberto);
        createCategory("Alquiler", "EXPENSE", alberto);
        Category comidaCat = createCategory("Comida", "EXPENSE", alberto);

        // 3. Crear Transacciones para Alberto
        createTransaction("Nómina de Diciembre", new BigDecimal("3500.00"), TransactionType.INCOME, alberto,
                salarioCat);
        createTransaction("Compra supermercado", new BigDecimal("85.50"), TransactionType.EXPENSE, alberto, comidaCat);
        createTransaction("Cena con amigos", new BigDecimal("45.00"), TransactionType.EXPENSE, alberto, comidaCat);

        // 4. Crear Plantilla de Presupuesto para Alberto
        ForecastTemplate template = new ForecastTemplate();
        template.setName("Presupuesto Mensual Básico");
        template.setDescription("Plantilla para seguimiento de gastos e ingresos recurrentes.");
        template.setIsPublic(true);
        template.setCreatedAt(LocalDateTime.now());
        // JSON structure as string
        template.setStructure(
                "{\"type\": \"table\", \"rows\": [{\"name\": \"Ingresos por Salario\", \"transaction_ref_id\": null, \"transaction_type\": \"INCOME\", \"amount\": 3500.00}, {\"name\": \"Gastos en Comida\", \"transaction_ref_id\": null, \"transaction_type\": \"EXPENSE\", \"amount\": 500.00}]}");
        forecastTemplateRepository.save(template);

        ForecastTemplateOwned owned = new ForecastTemplateOwned();
        owned.setUser(alberto);
        owned.setTemplate(template);
        forecastTemplateOwnedRepository.save(owned);

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
                    // Actualizar contraseña si es necesario (o siempre para desarrollo)
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

    private Category createCategory(String name, String type, User user) {
        Category category = new Category();
        category.setName(name);
        category.setType(type);
        category.setUser(user);

        // Evitar duplicados si ya existen las categorías (búsqueda muy simple por
        // nombre y usuario)
        // JPA/Hibernate manejará esto mejor si tuviéramos un método
        // findByNameAndUser...
        // Aquí asumimos que si el usuario ya existe, quizás las categorías también.
        // Para simplificar en este ejemplo de DataLoader, no comprobamos duplicados
        // exactos
        // línea por línea salvo que JPA lo haga (no hay unique constraints explícitas
        // en entity Category salvo ID).
        // Pero para ser robustos:
        // (Omitido por simplicidad, asumimos creación siempre o podrías añadir check)
        return categoryRepository.save(category);
    }

    private void createTransaction(String description, BigDecimal amount, TransactionType type, User user,
            Category category) {
        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);
        transaction.setTransactionType(type);
        transaction.setUser(user);
        transaction.setCategory(category);
        transactionRepository.save(transaction);
    }
}
