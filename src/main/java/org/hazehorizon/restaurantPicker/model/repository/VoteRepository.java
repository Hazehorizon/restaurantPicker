package org.hazehorizon.restaurantPicker.model.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.hazehorizon.restaurantPicker.model.RestaurantEntity;
import org.hazehorizon.restaurantPicker.model.VoteEntity;
import org.hazehorizon.restaurantPicker.model.VoterEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface VoteRepository extends CrudRepository<VoteEntity, Long> {
	Optional<VoteEntity> findByVoterCodeAndDate(String voterCode, LocalDate date);
	@Query(value = "select vr from VoterEntity vr where vr.code = ?1")
	Optional<VoterEntity> findVoter(String voterCode);
	@Query(value = "select vt.restaurant.id, count(vt) from VoteEntity vt where vt.restaurant in ?1 and vt.date = ?2 group by vt.restaurant.id")
	List<Object[]> countVotesByRestarauntsAndDate(List<RestaurantEntity> restaurantIds, LocalDate date);
}