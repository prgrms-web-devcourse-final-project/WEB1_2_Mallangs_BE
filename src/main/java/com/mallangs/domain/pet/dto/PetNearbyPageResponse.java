package com.mallangs.domain.pet.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;
import java.util.List;

@Getter
public class PetNearbyPageResponse {
    private int currentPage;
    private int totalPages;
    private int pageSize;
    private List<PetNearbyResponse> pets;

    public PetNearbyPageResponse(Page<PetNearbyResponse> page) {
        this.currentPage = page.getNumber() + 1;
        this.totalPages = page.getTotalPages();
        this.pageSize = page.getSize();
        this.pets = page.getContent();
    }
}