package com.tripper.mapper;

import com.tripper.dto.OutgoingMessageDTO;
import com.tripper.repository.MessageView;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Mapper interface for converting Message entities to Message DTOs.
 * This interface uses MapStruct to generate the implementation at compile time.
 * The componentModel is set to "spring" to allow for dependency injection in Spring applications.
 *
 * @see MessageView
 * @see OutgoingMessageDTO
 *
 * @author vitorlfreitas
 */
@Mapper(componentModel = "spring")
public interface MessageMapper {

    // Singleton instance of the mapper, created by MapStruct
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    // Method to convert a single Message entity to an OutgoingMessageDTO
    OutgoingMessageDTO toDto(MessageView message);

    // Method to convert a list of Message entities to a list of OutgoingMessageDTOs
    List<OutgoingMessageDTO> toDtoList(List<MessageView> messages);
}
