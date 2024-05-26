package com.polling_app.polling_app.repository;

import com.polling_app.polling_app.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface OptionRepository extends JpaRepository<Option, UUID>, JpaSpecificationExecutor<Option> {
}
