package com.jlg.cheesetracker.repository;

import com.jlg.cheesetracker.domain.Cheese;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Cheese entity.
 */
@SuppressWarnings("unused")
public interface CheeseRepository extends JpaRepository<Cheese,Long> {

}
