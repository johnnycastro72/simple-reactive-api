package com.example.simplereactiveapi.recipe.routes;

import com.example.simplereactiveapi.recipe.dto.RecipeDTO;
import com.example.simplereactiveapi.recipe.entity.Recipe;
import com.example.simplereactiveapi.recipe.mapper.RecipeMapper;
import com.example.simplereactiveapi.recipe.repository.RecipeRepository;
import com.example.simplereactiveapi.recipe.usecases.GetRecipesUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.when;

@WebFluxTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {GetRecipesRoute.class, GetRecipesUseCase.class, RecipeMapper.class})
class GetRecipesRouteTest {
    @MockBean
    private RecipeRepository repo;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testGetRecipes() {
        Recipe recipe1 = new Recipe();
        recipe1.setId("1");
        recipe1.setName("First Recipe");
        recipe1.setPrice(100);
        Recipe recipe2 = new Recipe();
        recipe2.setId("2");
        recipe2.setName("Second Recipe");
        recipe2.setPrice(200);

        when(repo.findAll()).thenReturn(Flux.just(recipe1, recipe2));

        webTestClient.get()
                .uri("/v1/api/recipe/")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(RecipeDTO.class)
                .value(userResponse -> {
                            Assertions.assertThat(userResponse.get(0).getId()).isEqualTo(recipe1.getId());
                            Assertions.assertThat(userResponse.get(0).getName()).isEqualTo(recipe1.getName());
                            Assertions.assertThat(userResponse.get(0).getPrice()).isEqualTo(recipe1.getPrice());
                            Assertions.assertThat(userResponse.get(1).getId()).isEqualTo(recipe2.getId());
                            Assertions.assertThat(userResponse.get(1).getName()).isEqualTo(recipe2.getName());
                            Assertions.assertThat(userResponse.get(1).getPrice()).isEqualTo(recipe2.getPrice());
                        }
                );
    }

}