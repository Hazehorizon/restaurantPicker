package org.hazehorizon.restaurantPicker.model.repository;

import java.util.Optional;

import org.hazehorizon.restaurantPicker.model.RoleEntity;
import org.hazehorizon.restaurantPicker.model.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
	Optional<UserEntity> findByLogin(String login);
	@Query(value = "select rl from RoleEntity rl where rl.code = ?1")
	Optional<RoleEntity> findRoleByCode(String code);
}
