package com.example.memorialpark.web.Repository;

import com.example.memorialpark.web.entity.user.Provider;
import com.example.memorialpark.web.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByProviderAndProviderId(Provider provider, String providerId);
    Optional<User> findByEmail(String email);

}
