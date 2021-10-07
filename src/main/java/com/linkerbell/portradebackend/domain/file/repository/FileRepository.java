package com.linkerbell.portradebackend.domain.file.repository;

import com.linkerbell.portradebackend.domain.file.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
