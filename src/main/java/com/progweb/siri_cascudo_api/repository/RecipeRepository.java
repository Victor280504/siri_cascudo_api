package com.progweb.siri_cascudo_api.repository;

import com.progweb.siri_cascudo_api.model.Recipe;
import com.progweb.siri_cascudo_api.model.RecipeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, RecipeId> {
    List<Recipe> findById_IdProduct(Long idProduct);
    @Override
    void deleteAll( Iterable<? extends Recipe> entities);
}
