package com.example.sumsub.service;
import com.example.sumsub.entity.Applicant;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant,String> {
    @Query(value = "select a from Applicant a where a.externalUserId=:external_user_id")
    Optional<Applicant> findById(@NotNull String external_user_id);
}
