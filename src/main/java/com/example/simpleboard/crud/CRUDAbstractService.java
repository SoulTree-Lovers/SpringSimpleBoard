package com.example.simpleboard.crud;

import com.example.simpleboard.common.Api;
import com.example.simpleboard.common.Pagination;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * DTO -> Entity -> DTO
 * */
public abstract class CRUDAbstractService<DTO, ENTITY> implements CRUDInterface<DTO> {

    @Autowired(required = false)
    private JpaRepository<ENTITY, Long> jpaRepository;

    @Autowired(required = false)
    private Converter<DTO, ENTITY> converter;

    @Override
    public DTO create(DTO dto) {
        // dto -> entity
        var entity = converter.toEntity(dto);

        // entity -> save
        jpaRepository.save(entity);

        // save -> dto
        var returnDto = converter.toDto(entity);

        return returnDto;
    }

    @Override
    public Optional<DTO> read(Long id) {
        var optionalEntity = jpaRepository.findById(id);

        var dto = optionalEntity.map(
                it -> {
                    return converter.toDto(it);
                }
        ).orElseGet(() -> null);

        return Optional.ofNullable(dto);
    }

    @Override
    public DTO update(DTO dto) {
        var entity = converter.toEntity(dto);
        jpaRepository.save(entity);
        var returnDto = converter.toDto(entity);
        return returnDto;
    }

    @Override
    public void delete(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Api<List<DTO>> list(Pageable pageable) {
        var list = jpaRepository.findAll(pageable);

        // Page 설정
        var pagination = Pagination.builder()
                .page(list.getNumber())
                .size(list.getSize())
                .currentElements(list.getNumberOfElements())
                .totalElements(list.getTotalElements())
                .totalPage(list.getTotalPages())
                .build();

        // Entity 리스트를 DTO 리스트로 변경
        var dtoList = list.stream()
                .map(
                        it -> {
                            return converter.toDto(it);
                        }
                ).collect(Collectors.toList());

        // Api로 묶어서 전송
        var response = Api.<List<DTO>> builder()
                .body(dtoList)
                .pagination(pagination)
                .build();

        return response;
    }
}
