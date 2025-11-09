package com.sampoom.backend.api.part.service;

import com.sampoom.backend.api.part.dto.BomPayload;
import com.sampoom.backend.api.part.entity.Part;
import com.sampoom.backend.api.part.repository.PartRepository;
import com.sampoom.backend.common.exception.BadRequestException;
import com.sampoom.backend.common.exception.NotFoundException;
import com.sampoom.backend.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartService {
    private final PartRepository partRepository;

    public void createPart(BomPayload payload) {
        if (partRepository.existsById(payload.getPartId()))
            throw new BadRequestException(ErrorStatus.DUPLICATE_PART_ID.getMessage());

        Part part = Part.builder()
                .id(payload.getPartId())
                .name(payload.getPartName())
                .code(payload.getPartCode())
                .complexity(payload.getComplexity())
                .status(payload.getStatus())
                .deleted(payload.getDeleted())
                .build();
        partRepository.save(part);
    }

    public void updatePart(BomPayload payload) {
        Part part = partRepository.findById(payload.getPartId()).orElseThrow(
                () -> new NotFoundException(ErrorStatus.PART_NOT_FOUND.getMessage())
        );

        part.setName(payload.getPartName());
        part.setCode(payload.getPartCode());
        part.setComplexity(payload.getComplexity());
        part.setStatus(payload.getStatus());
        part.setDeleted(payload.getDeleted());
        partRepository.save(part);
    }
}
