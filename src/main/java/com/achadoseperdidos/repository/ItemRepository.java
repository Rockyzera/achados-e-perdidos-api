package com.achadoseperdidos.repository;

import com.achadoseperdidos.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

    boolean existsByCategoria_Id(Long categoriaId);

    boolean existsByLocal_Id(Long localId);
}
