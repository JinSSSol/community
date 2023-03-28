package com.zerobase.community.file.repository;

import com.zerobase.community.file.entity.File;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
	Optional<List<File>> findAllByPostId(Long postId);

}
