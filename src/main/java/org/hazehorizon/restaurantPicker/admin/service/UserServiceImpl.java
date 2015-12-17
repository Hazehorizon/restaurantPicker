package org.hazehorizon.restaurantPicker.admin.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.hazehorizon.restaurantPicker.common.service.AbstractService;
import org.hazehorizon.restaurantPicker.model.RoleEntity;
import org.hazehorizon.restaurantPicker.model.UserEntity;
import org.hazehorizon.restaurantPicker.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceImpl extends AbstractService implements UserService {

	@Autowired
	UserRepository repository;

	@Override
	public Page<UserEntity> findAll(Integer pageNumber, Integer pageCount) {
		return repository.findAll(createPageable(pageNumber, pageCount));
	}

	@Override
	public UserEntity read(String login) {
		Optional<UserEntity> user = repository.findByLogin(login);
		checkOptional(user, UserEntity.class, login);
		return user.get();
	}

	@Override
	public UserEntity save(UserEntity user) {
		user.getRoles().stream().forEach(role -> {
			Optional<RoleEntity> dictionaryRole = repository.findRoleByCode(role.getCode());
			checkOptional(dictionaryRole, RoleEntity.class, role.getCode());
			role.setId(dictionaryRole.get().getId());
		});
		return repository.save(user);
	}

	@Override
	public void delete(String login) {
		Optional<UserEntity> user = repository.findByLogin(login);
		if (user.isPresent()) {
			repository.delete(user.get());
			logger.info("User is deleted");
		}
	}
}
