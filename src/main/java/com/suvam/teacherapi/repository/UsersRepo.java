package com.suvam.teacherapi.repository;

import com.suvam.teacherapi.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepo extends JpaRepository<Users, Long> {
}
