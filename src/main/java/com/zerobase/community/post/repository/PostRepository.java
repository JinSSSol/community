package com.zerobase.community.post.repository;

import com.zerobase.community.post.entity.Post;
import com.zerobase.community.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

}
