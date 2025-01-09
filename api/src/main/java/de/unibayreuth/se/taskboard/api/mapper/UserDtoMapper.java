package de.unibayreuth.se.taskboard.api.mapper;

import de.unibayreuth.se.taskboard.api.dtos.UserDto;
import de.unibayreuth.se.taskboard.business.domain.User;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;





@Mapper(componentModel = "spring")
@ConditionalOnMissingBean // prevent IntelliJ warning about duplicate beans
@NoArgsConstructor
public abstract class UserDtoMapper {

    public abstract UserDto fromBusiness(User source);
    public abstract User toBusiness(UserDto source);


    }

