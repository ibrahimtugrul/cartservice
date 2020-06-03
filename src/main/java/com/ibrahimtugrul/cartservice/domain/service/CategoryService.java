package com.ibrahimtugrul.cartservice.domain.service;

import com.ibrahimtugrul.cartservice.domain.converter.CategoryToVoConverter;
import com.ibrahimtugrul.cartservice.domain.entity.Category;
import com.ibrahimtugrul.cartservice.domain.exception.EntityNotFoundException;
import com.ibrahimtugrul.cartservice.domain.repository.CategoryRepository;
import com.ibrahimtugrul.cartservice.domain.vo.CategoryCreateVo;
import com.ibrahimtugrul.cartservice.domain.vo.CategoryVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryToVoConverter categoryToVoConverter;

    public long create(final CategoryCreateVo categoryCreateVo) {
        final Category category = Category.builder()
                .title(categoryCreateVo.getTitle())
                .parentId(categoryCreateVo.getParentId())
                .build();

        categoryRepository.save(category);
        return category.getId();
    }

    public List<CategoryVo> listAll() {
        final List<Category> categoryList = categoryRepository.findAll();
        return categoryList.stream().map(category -> categoryToVoConverter.convert(category)).collect(Collectors.toList());
    }

    public CategoryVo retrieve(final Long categoryId) {
        final Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new EntityNotFoundException("category"));
        return categoryToVoConverter.convert(category);
    }

    public void delete(final Long categoryId) {
        final Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new EntityNotFoundException("category"));
        categoryRepository.delete(category);
    }
}