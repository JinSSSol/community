package com.zerobase.community.user.repository;

import com.zerobase.community.user.entity.User;
import com.zerobase.community.user.model.constrains.SocialType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUserEmail(String userEmail);

	Optional<User> findByRefreshToken(String refreshToken);

	Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

}
