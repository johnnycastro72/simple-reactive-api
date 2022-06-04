package com.example.simplereactiveapi.recipe.usecases;

import com.example.simplereactiveapi.recipe.entity.Recipe;
import com.example.simplereactiveapi.recipe.dto.RecipeDTO;
import com.example.simplereactiveapi.recipe.repository.RecipeRepository;
import com.example.simplereactiveapi.recipe.mapper.RecipeMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PutRecipeUseCase {

    private final RecipeRepository repository;
    private final RecipeMapper mapper;

    public PutRecipeUseCase(RecipeRepository repository, RecipeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Mono<RecipeDTO> putRecipe(RecipeDTO recipeDTO) {
        return validateItExists(recipeDTO.getId())
                .flatMap(recipe -> repository.save(mapper.toEntity(recipeDTO)))
                .map(mapper::toRecipeDTO);
    }

    private Mono<Recipe> validateItExists(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(() -> new IllegalStateException("User does not exits  " + id)));
    }
}

