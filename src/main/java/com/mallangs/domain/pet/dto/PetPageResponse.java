package com.mallangs.domain.pet.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PetPageResponse {
    private int currentPage;
    private int totalPages;
    private int pageSize;
    private List<PetResponse> pets;

    public PetPageResponse(Page<PetResponse> page) {
        this.currentPage = page.getNumber() + 1;
        this.totalPages = page.getTotalPages();
        this.pageSize = page.getSize();
        this.pets = page.getContent();
    }
}