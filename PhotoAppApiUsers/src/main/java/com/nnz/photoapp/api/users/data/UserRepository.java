package com.nnz.photoapp.api.users.data;

import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<UserEntity, Long> {

}
